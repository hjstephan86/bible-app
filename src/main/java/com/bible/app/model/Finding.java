package com.bible.app.model;

public class Finding {
	private Passage passage;
	private String verseText;
	private int verseHitCount;

	public Passage getPassage() {
		return passage;
	}

	public void setPassage(Passage passage) {
		this.passage = passage;
	}

	public String getVerseText() {
		return verseText;
	}

	public void setVerseText(String verseText) {
		this.verseText = verseText;
	}

	public int getVerseHitCount() {
		return verseHitCount;
	}

	public void setVerseHitCount(int hitCount) {
		this.verseHitCount = hitCount;
	}
}
