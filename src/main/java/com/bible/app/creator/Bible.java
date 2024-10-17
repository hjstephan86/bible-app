package com.bible.app.creator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.bible.app.Constants;
import com.bible.app.model.Finding;
import com.bible.app.model.Passage;
import com.bible.app.model.Search;
import com.bible.app.model.SearchResult;
import com.bible.app.model.Section;
import com.bible.app.model.Word;
import com.bible.app.text.Book;
import com.bible.app.text.Chapter;
import com.bible.app.text.Verse;

public abstract class Bible {

	protected Map<String, Book> bookMap;

	protected HashSet<String> ignore;

	protected ArrayList<String> booksAsList;

	protected ArrayList<String> chaptersAsList;

	protected ArrayList<ArrayList<String>> versesAsListOfLists;

	protected String name;

	protected void readBible() throws IOException {
	}

	protected void readIgnore() throws IOException {
	}

	public Map<String, Book> getBookMap() {
		return bookMap;
	}

	public ArrayList<Verse> getVerses(Passage passage) {
		return new ArrayList<Verse>(
				bookMap.get(passage.getBook()).getChapter().get(passage.getChapter()).getVerses().values());
	}

	public ArrayList<String> getBooksAsList() {
		return booksAsList;
	}

	public ArrayList<String> getChaptersAsList() {
		return chaptersAsList;
	}

	public ArrayList<ArrayList<String>> getVersesAsListOfLists() {
		return versesAsListOfLists;
	}

	public String getName() {
		return name;
	}

	public boolean passageExists(Passage passage) {
		return (bookMap.get(passage.getBook()) != null
				&& bookMap.get(passage.getBook()).getChapter().get(passage.getChapter()) != null);
	}

	public boolean sectionIsValid(Section section) {
		return ((bookMap.get(section.getBookFrom()) != null && bookMap.get(section.getBookTo()) != null
				&& bookMap.get(section.getBookFrom()).getChapter().get(section.getChapterFrom()) != null
				&& bookMap.get(section.getBookTo()).getChapter().get(section.getChapterTo()) != null
				&& bookMap.get(section.getBookFrom()).getChapter().get(section.getChapterFrom()).getVerses()
						.get(section.getVerseFrom()) != null
				&& bookMap.get(section.getBookTo()).getChapter().get(section.getChapterTo()).getVerses()
						.get(section.getVerseTo()) != null)

				&&

				((bookMap.get(section.getBookFrom()).getPosition() == bookMap.get(section.getBookTo()).getPosition()
						&& bookMap.get(section.getBookFrom()).getChapter().get(section.getChapterFrom())
								.getChapter() == bookMap.get(section.getBookTo()).getChapter()
										.get(section.getChapterTo()).getChapter()
						&& bookMap.get(section.getBookFrom()).getChapter().get(section.getChapterFrom()).getVerses()
								.get(section.getVerseFrom())
								.getNumber() <= bookMap.get(section.getBookTo()).getChapter()
										.get(section.getChapterTo()).getVerses().get(section.getVerseTo()).getNumber())
						|| (bookMap.get(section.getBookFrom()).getPosition() == bookMap.get(section.getBookTo())
								.getPosition()
								&& bookMap.get(section.getBookFrom()).getChapter().get(section.getChapterFrom())
										.getChapter() < bookMap.get(section.getBookTo()).getChapter()
												.get(section.getChapterTo()).getChapter())
						|| (bookMap.get(section.getBookFrom()).getPosition() < bookMap.get(section.getBookTo())
								.getPosition())));
	}

	public SearchResult search(Search search) {
		SearchResult searchResult = new SearchResult();
		List<Finding> findings = new ArrayList<Finding>();
		int count = 0;

		Section section = getCurrentSection(search);

		Passage currentPassage = new Passage();
		currentPassage.setBook(section.getBookFrom());
		currentPassage.setChapter(section.getChapterFrom());
		currentPassage.setVerse(section.getVerseFrom());

		if (isSearchValid(search.getSearch())) {
			String searchText = getSearchText(search.getSearch());
			boolean matchExact = false;
			boolean matchCase = false;
			if (matchExactAndMatchCase(searchText)) {
				matchExact = true;
				matchCase = true;
			} else if (matchExact(searchText)) {
				matchExact = true;
			} else if (matchCase(searchText)) {
				matchCase = true;
			}

			searchText = matchExact && matchCase ? searchText.substring(2, searchText.length() - 2) : searchText;
			searchText = !matchExact && matchCase ? searchText.substring(1, searchText.length() - 1) : searchText;
			searchText = matchExact && !matchCase ? searchText.substring(1, searchText.length() - 1) : searchText;

			if (searchText.length() > 0) {
				do {
					String verseText = bookMap.get(currentPassage.getBook()).getChapter()
							.get(currentPassage.getChapter())
							.getVerses().get(currentPassage.getVerse()).getText();

					List<Integer> indices = getListOfMatchingIndices(verseText, searchText, matchCase, matchExact);
					if (indices.size() > 0) {
						count += indices.size();
						Finding finding = new Finding();
						finding.setPassage(
								new Passage(currentPassage.getBook(), currentPassage.getChapter(),
										currentPassage.getVerse()));
						finding.setVerseText(getFormattedVerseText(indices, verseText, searchText));
						findings.add(finding);
					}
					goToNextPassage(currentPassage);
				} while (!toPassageReached(currentPassage, section));
			}
		}

		searchResult.setFindings(findings);
		searchResult.setCount(count);
		return searchResult;
	}

	private boolean matchCase(String searchText) {
		return searchText.startsWith(Constants.SEARCH_MATCH_CASE_SYMBOL)
				&& searchText.endsWith(Constants.SEARCH_MATCH_CASE_SYMBOL)
				&& searchText.length() > 1;
	}

	private boolean matchExact(String searchText) {
		return searchText.startsWith(Constants.SEARCH_MATCH_EXACT_SYMBOL)
				&& searchText.endsWith(Constants.SEARCH_MATCH_EXACT_SYMBOL)
				&& searchText.length() > 1;
	}

	private boolean matchExactAndMatchCase(String searchText) {
		return ((searchText.startsWith(Constants.SEARCH_MATCH_CASE_SYMBOL + Constants.SEARCH_MATCH_EXACT_SYMBOL) &&
				searchText.endsWith(Constants.SEARCH_MATCH_EXACT_SYMBOL + Constants.SEARCH_MATCH_CASE_SYMBOL))
				|| (searchText.startsWith(Constants.SEARCH_MATCH_EXACT_SYMBOL + Constants.SEARCH_MATCH_CASE_SYMBOL)
						&&
						searchText.endsWith(
								Constants.SEARCH_MATCH_CASE_SYMBOL + Constants.SEARCH_MATCH_EXACT_SYMBOL)))
				&& searchText.length() > 3;
	}

	public List<Word> countWords(Section section) {
		HashMap<String, Word> words = new HashMap<String, Word>();

		Passage currentPassage = new Passage();
		currentPassage.setBook(section.getBookFrom());
		currentPassage.setChapter(section.getChapterFrom());
		currentPassage.setVerse(section.getVerseFrom());

		do {
			String[] arrWords = getSplittedVerseText(currentPassage);
			for (String s : arrWords) {
				if (words.containsKey(s)) {
					words.get(s).setCount(words.get(s).getCount() + 1);
				} else {
					words.put(s, new Word(s, 1, ignore(s)));
				}
			}
			goToNextPassage(currentPassage);
		} while (!toPassageReached(currentPassage, section));

		List<Word> wordList = new ArrayList<Word>(words.values());
		Collections.sort(wordList);
		return wordList;
	}

	private boolean ignore(String s) {
		return s.length() == 0 || ignore.contains(s.toLowerCase());
	}

	private boolean isSearchValid(String search) {
		return search.length() > 0 && !search.equals("\"");
	}

	private String getFormattedVerseText(List<Integer> indices, String verseText, String searchText) {
		for (int i = 0; i < indices.size(); i++) {
			verseText = insertString(verseText, "<b>", indices.get(i) + (i * 7));
			verseText = insertString(verseText, "</b>", indices.get(i) + (i * 7) + searchText.length() + 3);
		}
		return verseText;
	}

	private String insertString(String originalString, String stringToBeInserted, int index) {
		return originalString.substring(0, index) + stringToBeInserted + originalString.substring(index);
	}

	private List<Integer> getListOfMatchingIndices(String verseText, String searchText, boolean matchCase,
			boolean matchExact) {
		List<Integer> indices = new ArrayList<Integer>();
		verseText = matchCase ? verseText : verseText.toLowerCase();
		searchText = matchCase ? searchText : searchText.toLowerCase();
		int index = verseText.indexOf(searchText);
		while (index >= 0) {
			if (matchExact) {
				if (matchExact(index, verseText, searchText)) {
					indices.add(index);
				}
				index = verseText.indexOf(searchText, index + 1);
			} else {
				indices.add(index);
				index = verseText.indexOf(searchText, index + 1);
			}
		}
		return indices;
	}

	private boolean matchExact(int index, String verseText, String searchText) {
		char charBeforeMatch = index > 0 ? verseText.charAt(index - 1) : ' ';
		char charAfterMatch = (index + searchText.length()) <= verseText.length() - 1
				? verseText.charAt(index + searchText.length())
				: ' ';
		String regex = "[!@#$%^&*()_+\\-=\\[\\]{};':„\"\\\\|,.<>/?`~]";
		return (Character.toString(charBeforeMatch).matches(regex) || charBeforeMatch == ' ')
				&& (Character.toString(charAfterMatch).matches(regex) || charAfterMatch == ' ');
	}

	private String getSearchText(String search) {
		String searchText = new String();

		StringTokenizer tokenizer = new StringTokenizer(search);
		while (tokenizer.hasMoreElements())
			searchText += tokenizer.nextToken() + " ";

		return searchText.substring(0, searchText.length() - 1);
	}

	private Section getCurrentSection(Search search) {
		Section section = new Section();
		if (bookMap.containsKey(search.getSection())) {
			section = getSection(bookMap.get(search.getSection()));
		} else {
			Book bookTo = null;
			switch (search.getSection()) {
				case "Alle":
					section.setBookFrom("1. Mose");
					section.setChapterFrom(1);
					section.setVerseFrom(1);
					bookTo = bookMap.get("Offenbarung");
					section.setBookTo(bookTo.getName());
					section.setChapterTo(getLastChapter(bookTo).getChapter());
					section.setVerseTo(getLastVerse(getLastChapter(bookTo)).getNumber());
					break;
				case "AT":
					section.setBookFrom("1. Mose");
					section.setChapterFrom(1);
					section.setVerseFrom(1);
					bookTo = bookMap.get("Maleachi");
					section.setBookTo(bookTo.getName());
					section.setChapterTo(getLastChapter(bookTo).getChapter());
					section.setVerseTo(getLastVerse(getLastChapter(bookTo)).getNumber());
					break;
				case "NT":
					section.setBookFrom("Matthäus");
					section.setChapterFrom(1);
					section.setVerseFrom(1);
					bookTo = bookMap.get("Offenbarung");
					section.setBookTo(bookTo.getName());
					section.setChapterTo(getLastChapter(bookTo).getChapter());
					section.setVerseTo(getLastVerse(getLastChapter(bookTo)).getNumber());
					break;
				default:
					section.setBookFrom("1. Mose");
					section.setChapterFrom(1);
					section.setVerseFrom(1);
					bookTo = bookMap.get("Offenbarung");
					section.setBookTo(bookTo.getName());
					section.setChapterTo(getLastChapter(bookTo).getChapter());
					section.setVerseTo(getLastVerse(getLastChapter(bookTo)).getNumber());
					break;
			}
		}
		return section;
	}

	private void goToNextPassage(Passage currentPassage) {
		if (getNextVerse(currentPassage) != null) {
			currentPassage.setVerse(getNextVerse(currentPassage).getNumber());
		} else if (getNextChapter(currentPassage) != null) {
			currentPassage.setChapter(getNextChapter(currentPassage).getChapter());
			currentPassage.setVerse(1);
		} else if (getNextBook(currentPassage) != null) {
			currentPassage.setBook(getNextBook(currentPassage).getName());
			currentPassage.setChapter(1);
			currentPassage.setVerse(1);
		} else {
		}
	}

	private Book getNextBook(Passage currentPassage) {
		return bookMap.get(currentPassage.getBook()).getNextBook();
	}

	private Chapter getNextChapter(Passage currentPassage) {
		return bookMap.get(currentPassage.getBook()).getChapter().get(currentPassage.getChapter() + 1);
	}

	private Verse getNextVerse(Passage currentPassage) {
		return bookMap.get(currentPassage.getBook()).getChapter().get(currentPassage.getChapter()).getVerses()
				.get(currentPassage.getVerse() + 1);
	}

	private Section getSection(Book book) {
		Section section = new Section();
		section.setBookFrom(book.getName());
		section.setChapterFrom(1);
		section.setVerseFrom(1);
		section.setBookTo(book.getName());
		section.setChapterTo(getLastChapter(book).getChapter());
		section.setVerseTo(getLastVerse(getLastChapter(book)).getNumber());
		return section;
	}

	private Chapter getLastChapter(Book book) {
		return book.getChapter().get(book.getChapter().size());
	}

	private Verse getLastVerse(Chapter chapter) {
		return chapter.getVerses().get(chapter.getVerses().size());
	}

	private String[] getSplittedVerseText(Passage currentPassage) {
		String verseText = bookMap.get(currentPassage.getBook()).getChapter().get(currentPassage.getChapter())
				.getVerses().get(currentPassage.getVerse()).getText();
		return verseText.replaceAll("[\\(\\)\\.\\;\\:\\,\\!\\?\\\"\\“\\”\\=\\–\\d]", "").split(" ");
	}

	private boolean toPassageReached(Passage currentPassage, Section section) {
		return (getBookPosition(currentPassage.getBook()) == getBookPosition(section.getBookTo())
				&& currentPassage.getChapter() == section.getChapterTo()
				&& currentPassage.getVerse() > section.getVerseTo())
				|| (getBookPosition(currentPassage.getBook()) == getBookPosition(section.getBookTo())
						&& currentPassage.getChapter() > section.getChapterTo())
				|| (getBookPosition(currentPassage.getBook()) > getBookPosition(section.getBookTo())
						|| (getBookPosition(currentPassage.getBook()) == 65 && currentPassage.getChapter() == 22
								&& currentPassage.getVerse() == 21));
	}

	private int getBookPosition(String book) {
		return bookMap.get(book).getPosition();
	}
}
