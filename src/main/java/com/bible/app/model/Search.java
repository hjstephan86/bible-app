package com.bible.app.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class Search {

	@Schema(description = "The search string to search for", example = "Kinder", required = true)
	private String search;

	@Schema(description = "The section to search in, i.e., \"Alle\", \"AT\" or \"NT\" or simply a bible book, e.g., \"3. Mose\" or \"Jona\"", example = "AT", required = true)
	private String section;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String toString() {
		return "\"" + search + "\" in " + section;
	}
}
