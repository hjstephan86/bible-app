package com.bible.app.model;

public class Section {
	private String bookFrom;
	private int chapterFrom;
	private int verseFrom;
	private String bookTo;
	private int chapterTo;
	private int verseTo;

	public Section() {
	}

	public Section(String bookFrom, int chapterFrom, int verseFrom, String bookTo, int chapterTo, int verseTo) {
		super();
		this.bookFrom = bookFrom;
		this.chapterFrom = chapterFrom;
		this.verseFrom = verseFrom;
		this.bookTo = bookTo;
		this.chapterTo = chapterTo;
		this.verseTo = verseTo;
	}

	public String getBookFrom() {
		return bookFrom;
	}

	public void setBookFrom(String bookFrom) {
		this.bookFrom = bookFrom;
	}

	public int getChapterFrom() {
		return chapterFrom;
	}

	public void setChapterFrom(int chapterFrom) {
		this.chapterFrom = chapterFrom;
	}

	public int getVerseFrom() {
		return verseFrom;
	}

	public void setVerseFrom(int verseFrom) {
		this.verseFrom = verseFrom;
	}

	public String getBookTo() {
		return bookTo;
	}

	public void setBookTo(String bookTo) {
		this.bookTo = bookTo;
	}

	public int getChapterTo() {
		return chapterTo;
	}

	public void setChapterTo(int chapterTo) {
		this.chapterTo = chapterTo;
	}

	public int getVerseTo() {
		return verseTo;
	}

	public void setVerseTo(int verseTo) {
		this.verseTo = verseTo;
	}
	
	public String toString() {
		return bookFrom + " " + " " + chapterFrom + ", " + verseFrom + " : " + bookTo + " " + " " + chapterTo + ", " + verseTo;
	}
}
