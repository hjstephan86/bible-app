package com.bible.app.creator.bible;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

import com.bible.app.creator.Bible;
import com.bible.app.text.Book;
import com.bible.app.text.Chapter;
import com.bible.app.text.Verse;

public class Elberfelder1905 extends Bible {

	public Elberfelder1905(String name) throws IOException {
		this.name = name;
		readBible();
		readIgnore();

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
	public void readBible() throws IOException {
		bookMap = new LinkedHashMap<String, Book>();
		try (InputStream inputStream = getClass().getResourceAsStream("/bible/Elberfelder1905.txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			Book oldBook = null, newBook = null;
			while ((line = br.readLine()) != null) {
				// Read book position
				int bookPosition = Integer.parseInt(line.substring(0, line.indexOf("#")));
				// Read books and store them.
				line = line.substring(line.indexOf("#") + 1);
				String book = line.substring(0, line.indexOf("#"));
				if (!bookMap.containsKey(book)) {
					newBook = new Book(book, bookPosition);
					bookMap.put(book, newBook);
					if (oldBook != null) {
						oldBook.setNextBook(newBook);
					}
				} else {
					oldBook = newBook;
				}
				// Read chapters and store them.
				line = line.substring(line.indexOf("#") + 1);
				int chapter = Integer.parseInt(line.substring(0, line.indexOf("#")));
				if (!bookMap.get(book).getChapter().containsKey(chapter)) {
					bookMap.get(book).getChapter().put(chapter, new Chapter(chapter));
				}
				// Read verses and store them.
				line = line.substring(line.indexOf("#") + 1);
				int verseNumber = Integer.parseInt(line.substring(0, line.indexOf("#")));
				if (!bookMap.get(book).getChapter().get(chapter).getVerses().containsKey(verseNumber)) {
					bookMap.get(book).getChapter().get(chapter).getVerses().put(verseNumber, new Verse(verseNumber));
					String verseText = line.substring(line.indexOf("#") + 1);
					bookMap.get(book).getChapter().get(chapter).getVerses().get(verseNumber).setText(verseText);
				}
			}
		}
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
