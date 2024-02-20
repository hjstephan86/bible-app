package com.bible.app.text;

import java.util.LinkedHashMap;
import java.util.Map;

public class Book {
	private String name;
	private int position;
	private Book nextBook;
	private Map<Integer, Chapter> chapter = new LinkedHashMap<Integer, Chapter>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Book(String name, int position) {
		super();
		this.name = name;
		this.position = position;
	}

	public Map<Integer, Chapter> getChapter() {
		return chapter;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Book getNextBook() {
		return nextBook;
	}

	public void setNextBook(Book nextBook) {
		this.nextBook = nextBook;
	}
}
