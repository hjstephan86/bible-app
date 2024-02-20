package com.bible.app.text;

import java.util.LinkedHashMap;
import java.util.Map;

public class Chapter {
	private int chapter;
	private Map<Integer, Verse> verses = new LinkedHashMap<Integer, Verse>();

	public Map<Integer, Verse> getVerses() {
		return verses;
	}

	public Chapter(int chapter) {
		super();
		this.chapter = chapter;
	}

	public int getChapter() {
		return chapter;
	}

	public void setChapter(int chapter) {
		this.chapter = chapter;
	}

}
