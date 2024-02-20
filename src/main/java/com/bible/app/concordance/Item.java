package com.bible.app.concordance;

import java.util.LinkedList;
import java.util.List;

public class Item {
	private String Id;
	private String title;
	private String paragraph;
	private List<Description> description;

	public Item() {
		description = new LinkedList<Description>();
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getParagraph() {
		return paragraph;
	}

	public void setParagraph(String paragraph) {
		this.paragraph = paragraph;
	}

	public List<Description> getDescription() {
		return description;
	}

	public void setDescription(List<Description> description) {
		this.description = description;
	}

}
