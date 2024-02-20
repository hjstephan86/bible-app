package com.bible.app.creator.bible;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import com.bible.app.concordance.Description;
import com.bible.app.concordance.Item;
import com.bible.app.creator.Bible;
import com.bible.app.text.Book;
import com.bible.app.text.Chapter;
import com.bible.app.text.Verse;

public class Luther1912Strong extends Bible {

	private Map<String, Item> concordance;

	public Luther1912Strong(String name) throws IOException {
		this.name = name;
		readBible();
		readIgnore();
		readConcordance();

		booksAsList = new ArrayList<String>(bookMap.keySet());
		chaptersAsList = new ArrayList<String>();
		versesAsListOfLists = new ArrayList<ArrayList<String>>();

		for (Book b : bookMap.values()) {
			chaptersAsList.add("" + b.getChapter().size());
			ArrayList<String> versesOfEachChapter = new ArrayList<String>();
			for (Chapter c : b.getChapter().values()) {
				versesOfEachChapter.add("" + c.getVerses().size());
			}
			versesAsListOfLists.add(versesOfEachChapter);
		}
	}

	@Override
	public void readBible() {
		bookMap = new LinkedHashMap<String, Book>();
		try (InputStream stream = getClass().getResourceAsStream("/bible/strong/Luther1912-Strong.xml")) {
			XMLInputFactory inputFactory = XMLInputFactory.newFactory();
			inputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);

			XMLStreamReader reader = inputFactory.createXMLStreamReader(stream);

			Book oldBook = null, newBook = null;
			Chapter chapter = null;
			Verse verse = null;
			String verseText = "";
			while (reader.hasNext()) {
				switch (reader.next()) {
				case XMLStreamConstants.START_ELEMENT:
					if (reader.getName().toString().equals("BIBLEBOOK")) {
						String bookName = reader.getAttributeValue(0);
						if (!bookMap.containsKey(bookName)) {
							newBook = new Book(bookName, Integer.parseInt(reader.getAttributeValue(1)) - 1);
							bookMap.put(bookName, newBook);
							if (oldBook != null) {
								oldBook.setNextBook(newBook);
							}
							oldBook = newBook;
						}
					}
					if (reader.getName().toString().equals("CHAPTER")) {
						int chapterNum = Integer.parseInt(reader.getAttributeValue(0));
						if (!newBook.getChapter().containsKey(chapterNum)) {
							chapter = new Chapter(chapterNum);
							newBook.getChapter().put(chapterNum, chapter);
						}
					}
					if (reader.getName().toString().equals("VERS")) {
						int verseNum = Integer.parseInt(reader.getAttributeValue(0));
						if (!chapter.getVerses().containsKey(verseNum)) {
							verse = new Verse(verseNum);
							chapter.getVerses().put(verseNum, verse);
						}
					}
					if (reader.getName().toString().equals("gr")) {
						String gr = "<gr " + reader.getAttributeName(0) + "=\"" + reader.getAttributeValue(0) + "\">"
								+ reader.getElementText() + "</gr>";
						verseText += gr;
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					if (reader.getName().toString().equals("VERS")) {
						verse.setText(ReplaceGrWithHyperlink(verseText, newBook));
						verseText = "";
					}
					// System.out.println("End " + reader.getName());
					break;
				case XMLStreamConstants.CHARACTERS:
				case XMLStreamConstants.SPACE:
					String text = reader.getText();
					if (verse != null) {
						verseText += text;
					}
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void readConcordance() {
		concordance = new LinkedHashMap<String, Item>();
		try (InputStream stream = getClass().getResourceAsStream("/bible/strong/Luther1912-Konkordanz.xml")) {
			XMLInputFactory inputFactory = XMLInputFactory.newFactory();
			inputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);

			XMLStreamReader reader = inputFactory.createXMLStreamReader(stream);

			Item item = null;
			Description description = null;
			String paragraph = "";
			while (reader.hasNext()) {
				switch (reader.next()) {
				case XMLStreamConstants.START_ELEMENT:
					if (reader.getName().toString().equals("item")) {
						if (item != null) {
							concordance.put(item.getId(), item);
						}
						item = new Item();
						item.setId(reader.getAttributeValue(0));
					} else if (reader.getName().toString().equals("title") && item != null && description == null) {
						item.setTitle(reader.getElementText());
					} else if (reader.getName().toString().equals("description") && item != null) {
						description = new Description();
					} else if (reader.getName().toString().equals("title") && description != null) {
						description.setTitle(reader.getElementText());
					} else if (reader.getName().toString().equals("reflink")) {
						description.getReflinks().add(reader.getAttributeValue(0));
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					if (reader.getName().toString().equals("paragraph") && item != null) {
						item.setParagraph(paragraph);
						paragraph = "";
					}
					if (reader.getName().toString().equals("description") && item != null) {
						item.getDescription().add(description);
						description = null;
					}
					// System.out.println("End " + reader.getName());
					break;
				case XMLStreamConstants.CHARACTERS:
				case XMLStreamConstants.SPACE:
					String text = reader.getText();
					if (!text.trim().isEmpty() && !text.equals("; ") && item != null) {
						paragraph += text;
					}
					break;
				}
			}
		} catch (Exception e) {
			if (!e.getMessage().contains("ParseError at [row,col]:[343651,3]"))
				System.out.println(e.toString());
		}
	}

	private String ReplaceGrWithHyperlink(String oldVerseText, Book book) {
		String concordancePrefix = book.getPosition() < 39 ? "H" : "G";
		String verseText = oldVerseText.replaceAll("<gr str=\"",
				"<a href=\"#\" onclick=\"showConcordanceEntry('" + concordancePrefix);
		verseText = verseText.replaceAll("\">", "', this);event.preventDefault();\">");
		verseText = verseText.replaceAll("</gr>", "</a>");
		return verseText;
	}

	public Map<String, Item> getConcordance() {
		return concordance;
	}

	public void readIgnore() throws IOException {
		ignore = new HashSet<String>();
		try (InputStream inputStream = getClass().getResourceAsStream("/ignore/ignore-Deu.txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.replaceAll("\\s", "");
				if (!ignore.contains(line.toLowerCase())) {
					ignore.add(line.toLowerCase());
				}
			}
		}
	}
}
