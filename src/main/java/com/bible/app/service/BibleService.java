package com.bible.app.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.bible.app.creator.Bible;
import com.bible.app.creator.BibleCreator;
import com.bible.app.creator.bible.Luther1912Strong;
import com.bible.app.model.Passage;
import com.bible.app.model.Search;
import com.bible.app.model.SearchResult;
import com.bible.app.model.Section;
import com.bible.app.model.Word;
import com.bible.app.text.Verse;

@Service
@SessionScope
public class BibleService {

	private Bible active;

	private Map<String, Bible> bibleMap;

	private Bible luther1912Strong;

	public BibleService() throws IOException {
		bibleMap = new LinkedHashMap<String, Bible>();

		Bible luther1912 = BibleCreator.getBible("Luther 1912");
		luther1912Strong = BibleCreator.getBible("Luther 1912 Strong");
		Bible elberfelder1905 = BibleCreator.getBible("Elberfelder 1905");
		Bible menge1939 = BibleCreator.getBible("Menge 1939");
		Bible schlachter1951 = BibleCreator.getBible("Schlachter 1951");
		Bible web = BibleCreator.getBible("World English Bible");
		Bible asv = BibleCreator.getBible("American Standard Version");

		bibleMap.put(luther1912.getName(), luther1912);
		bibleMap.put(elberfelder1905.getName(), elberfelder1905);
		bibleMap.put(menge1939.getName(), menge1939);
		bibleMap.put(schlachter1951.getName(), schlachter1951);
		bibleMap.put(web.getName(), web);
		bibleMap.put(asv.getName(), asv);

		active = luther1912;
	}

	public ArrayList<String> getBiblesAsList() {
		return new ArrayList<String>(bibleMap.keySet());
	}

	public Bible getActive() {
		return active;
	}

	public void setActive(String bibleName) {
		active = bibleMap.get(bibleName);
	}

	public ArrayList<String> getBooksAsList() {
		return active.getBooksAsList();
	}

	public ArrayList<String> getChaptersAsList() {
		return active.getChaptersAsList();
	}

	public ArrayList<String> getBooksAsListFromLuther1912Strong() {
		return luther1912Strong.getBooksAsList();
	}

	public ArrayList<String> getChaptersAsListFromLuther1912Strong() {
		return luther1912Strong.getChaptersAsList();
	}

	public ArrayList<ArrayList<String>> getVersesAsListOfLists() {
		return active.getVersesAsListOfLists();
	}

	public ArrayList<Verse> getVerses(Passage passage) {
		return active.getVerses(passage);
	}

	public ArrayList<Verse> getVersesFromLuther1912Strong(Passage passage) {
		return luther1912Strong.getVerses(passage);
	}

	public SearchResult search(Search search) {
		return active.search(search);
	}

	public List<Word> countWords(Section section) {
		return active.countWords(section);
	}

	public boolean passageExists(Passage passage) {
		return active.passageExists(passage);
	}

	public boolean sectionIsValid(Section section) {
		return active.sectionIsValid(section);
	}

	public Object getConcordance() {
		if (luther1912Strong != null)
			return ((Luther1912Strong) luther1912Strong).getConcordance();
		return null;
	}
}