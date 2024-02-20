package com.bible.app.concordance;

import java.util.LinkedList;
import java.util.List;

public class Description {
	private String title;
	private List<String> reflinks;

	public Description() {
		reflinks = new LinkedList<String>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getReflinks() {
		return reflinks;
	}

	public void setReflinks(List<String> reflinks) {
		this.reflinks = reflinks;
	}
}
