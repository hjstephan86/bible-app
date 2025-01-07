package com.bible.app.creator.bible;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bible.app.Constants;
import com.bible.app.creator.Bible;
import com.bible.app.creator.BibleFactory;
import com.bible.app.text.Book;
import com.bible.app.text.Chapter;
import com.bible.app.text.Verse;

public class Parser {

    public static void main(String[] args) throws IOException {
        BibleFactory bibleFactory = new BibleFactory();
        Bible luther1912 = bibleFactory.getBible(Constants.BIBLE_LUTHER_1912);

        Parser parser = new Parser();
        parser.parseFromBibleSupersearch(luther1912.getBookMap());
    }

    private void parseFromBibleSupersearch(Map<String, Book> referenceBookMap) throws IOException {
        LinkedHashMap<String, Book> bookMap = new LinkedHashMap<String, Book>();
        parseBookMap(bookMap);
        updateBookNames(referenceBookMap, bookMap);
        exportBible(bookMap);
    }

    private void parseBookMap(LinkedHashMap<String, Book> bookMapSyn) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/bible/Chinese-Union-orig.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            Book oldBook = null, newBook = null;
            int bookPosition = 0;
            while ((line = br.readLine()) != null) {
                String arr[] = line.split("[: ]+");
                String book = "";
                int index = 0;
                int k = 0;
                do {
                    book += arr[index];
                    k += arr[index].length() + 1;
                    index++;
                } while (!arr[index].matches("[0-9]+"));
                if (!bookMapSyn.containsKey(book)) {
                    newBook = new Book(book, bookPosition);
                    bookMapSyn.put(book, newBook);
                    if (oldBook != null) {
                        oldBook.setNextBook(newBook);
                    }
                    bookPosition++;
                } else {
                    oldBook = newBook;
                }
                // Read chapters and store them.
                int chapter = Integer.parseInt(arr[index]);
                if (!bookMapSyn.get(book).getChapter().containsKey(chapter)) {
                    bookMapSyn.get(book).getChapter().put(chapter, new Chapter(chapter));
                }
                k += String.valueOf(chapter).length() + 1;
                index++;
                // Read verses and store them.
                int verseNumber = Integer.parseInt(arr[index]);
                if (!bookMapSyn.get(book).getChapter().get(chapter).getVerses().containsKey(verseNumber)) {
                    bookMapSyn.get(book).getChapter().get(chapter).getVerses().put(verseNumber,
                            new Verse(verseNumber));
                    k += String.valueOf(verseNumber).length() + 1;
                    index++;
                    String verseText = line.substring(k);
                    bookMapSyn.get(book).getChapter().get(chapter).getVerses().get(verseNumber).setText(verseText);
                }
            }
        }
    }

    private void updateBookNames(Map<String, Book> referenceBookMap, LinkedHashMap<String, Book> bookMap) {
        Iterator<Map.Entry<String, Book>> iteratorSyn = bookMap.entrySet().iterator();
        Iterator<Map.Entry<String, Book>> iterator = referenceBookMap.entrySet().iterator();
        while (iteratorSyn.hasNext() && iterator.hasNext()) {
            Map.Entry<String, Book> entrySyn = iteratorSyn.next();
            Book bookSyn = entrySyn.getValue();
            Map.Entry<String, Book> entry = iterator.next();
            Book book = entry.getValue();
            bookSyn.setName(book.getName());
        }
    }

    private void exportBible(LinkedHashMap<String, Book> bookMap) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Chinese-Union.txt"))) {
            for (Book b : bookMap.values()) {
                for (Chapter c : b.getChapter().values()) {
                    for (Verse v : c.getVerses().values()) {
                        String line = b.getPosition() + "#" + b.getName() + "#" + c.getChapter() +
                                "#" + v.getNumber()
                                + "#" + v.getText();
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}