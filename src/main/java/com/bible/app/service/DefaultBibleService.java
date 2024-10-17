package com.bible.app.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.bible.app.Constants;
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
public class DefaultBibleService implements BibleService {

	private Map<String, Bible> bibleMap;
	private Bible active;
	private Bible luther1912Strong;

	public DefaultBibleService() throws IOException {
		bibleMap = new LinkedHashMap<String, Bible>();

		Bible luther1912 = BibleCreator.getBible(Constants.BIBLE_LUTHER_1912);
		luther1912Strong = BibleCreator.getBible(Constants.BIBLE_LUTHER_1912_STRONG);
		Bible elberfelder1905 = BibleCreator.getBible(Constants.BIBLE_ELBERFELDER);
		Bible menge1939 = BibleCreator.getBible(Constants.BIBLE_MENGE);
		Bible schlachter1951 = BibleCreator.getBible(Constants.BIBLE_SCHLACHTER);
		Bible web = BibleCreator.getBible(Constants.BIBLE_WORLD_ENG);
		Bible asv = BibleCreator.getBible(Constants.BIBLE_AMERICAN_STD);

		bibleMap.put(luther1912.getName(), luther1912);
		bibleMap.put(elberfelder1905.getName(), elberfelder1905);
		bibleMap.put(menge1939.getName(), menge1939);
		bibleMap.put(schlachter1951.getName(), schlachter1951);
		bibleMap.put(web.getName(), web);
		bibleMap.put(asv.getName(), asv);

		active = luther1912;
	}

	@Override
	public Map<String, Bible> getBibleMap() {
		return bibleMap;
	}

	@Override
	public ArrayList<String> getBiblesAsList() {
		return new ArrayList<String>(bibleMap.keySet());
	}

	@Override
	public Bible getActive() {
		return active;
	}

	@Override
	public void setActive(String bibleName) {
		active = bibleMap.get(bibleName);
	}

	@Override
	public ArrayList<String> getBooksAsList() {
		return active.getBooksAsList();
	}

	@Override
	public ArrayList<String> getChaptersAsList() {
		return active.getChaptersAsList();
	}

	@Override
	public ArrayList<String> getBooksAsListFromLuther1912Strong() {
		return luther1912Strong.getBooksAsList();
	}

	@Override
	public ArrayList<String> getChaptersAsListFromLuther1912Strong() {
		return luther1912Strong.getChaptersAsList();
	}

	@Override
	public ArrayList<ArrayList<String>> getVersesAsListOfLists() {
		return active.getVersesAsListOfLists();
	}

	@Override
	public ArrayList<Verse> getVerses(Passage passage) {
		return active.getVerses(passage);
	}

	@Override
	public ArrayList<Verse> getVersesFromLuther1912Strong(Passage passage) {
		return luther1912Strong.getVerses(passage);
	}

	@Override
	public SearchResult search(Search search) {
		return active.search(search);
	}

	@Override
	public List<Word> countWords(Section section) {
		return active.countWords(section);
	}

	@Override
	public boolean passageExists(Passage passage) {
		return active.passageExists(passage);
	}

	@Override
	public boolean sectionIsValid(Section section) {
		return active.sectionIsValid(section);
	}

	@Override
	public Object getConcordance() {
		if (luther1912Strong != null)
			return ((Luther1912Strong) luther1912Strong).getConcordance();
		return null;
	}
}