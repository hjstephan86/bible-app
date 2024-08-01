package com.bible.app.model;

public class Passage {
	private String book;
	private int chapter;
	private int verse;

	public Passage() {
	}

	public Passage(String book, int chapter) {
		super();
		this.book = book;
		this.chapter = chapter;
	}

	public Passage(String book, int chapter, int verse) {
		super();
		this.book = book;
		this.chapter = chapter;
		this.verse = verse;
	}

	public String getBook() {
		return book;
	}

	public void setBook(String book) {
		this.book = book;
	}

	public int getChapter() {
		return chapter;
	}

	public void setChapter(int chapter) {
		this.chapter = chapter;
	}

	public int getVerse() {
		return verse;
	}

	public void setVerse(int verse) {
		this.verse = verse;
	}
	
	public String toString() {
		return book + " " +  chapter + (verse > 0 ? ", " + verse : "");
	}
}
