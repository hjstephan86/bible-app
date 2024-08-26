package com.bible.app.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class Passage {

	@Schema(description = "The bible book to read the section in", example = "3. Mose", required = true)
	private String book;

	@Schema(description = "The chapter in the bible book to read the section in", example = "2", required = true)
	private int chapter;

	@Schema(description = "The verse in the chapter in the bible book to read the section in", required = false)
	private int verse;

	public Passage() {
	}

	public Passage(String book, int chapter) {
		this.book = book;
		this.chapter = chapter;
	}

	public Passage(String book, int chapter, int verse) {
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
		return book + " " + chapter + (verse > 0 ? ", " + verse : "");
	}
}
