package com.bible.app.model;

public class Word implements Comparable<Word> {
	private String name;
	/**
	 * Counts how often this word occurs.
	 */
	private int count;
	private boolean ignore;
	private int index;

	public Word(String name, int count, boolean ignore) {
		super();
		this.name = name;
		this.count = count;
		this.ignore = ignore;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public int compareTo(Word o) {
		if (o.getCount() == this.getCount())
			return 0;
		return o.getCount() < this.getCount() ? -1 : 1;
	}

	public boolean isIgnore() {
		return ignore;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
