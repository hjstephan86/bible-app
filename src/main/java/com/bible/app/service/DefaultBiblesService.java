package com.bible.app.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bible.app.Constants;
import com.bible.app.creator.Bible;
import com.bible.app.creator.BibleFactory;

@Service
public class DefaultBiblesService implements BiblesService {

	private Map<String, Bible> bibleMap;

	public DefaultBiblesService() throws IOException {
		bibleMap = new LinkedHashMap<String, Bible>();

		BibleFactory bibleFactory = new BibleFactory();
		Bible luther1912 = bibleFactory.getBible(Constants.BIBLE_LUTHER_1912);
		Bible luterh1912strong = bibleFactory.getBible(Constants.BIBLE_LUTHER_1912_STRONG);
		Bible elberfelder1905 = bibleFactory.getBible(Constants.BIBLE_ELBERFELDER);
		Bible menge1939 = bibleFactory.getBible(Constants.BIBLE_MENGE);
		Bible schlachter1951 = bibleFactory.getBible(Constants.BIBLE_SCHLACHTER);
		Bible web = bibleFactory.getBible(Constants.BIBLE_WORLD_ENG);
		Bible asv = bibleFactory.getBible(Constants.BIBLE_AMERICAN_STD);

		bibleMap.put(luther1912.getName(), luther1912);
		bibleMap.put(luterh1912strong.getName(), luterh1912strong);
		bibleMap.put(elberfelder1905.getName(), elberfelder1905);
		bibleMap.put(menge1939.getName(), menge1939);
		bibleMap.put(schlachter1951.getName(), schlachter1951);
		bibleMap.put(web.getName(), web);
		bibleMap.put(asv.getName(), asv);
	}

	@Override
	public Map<String, Bible> getBibleMap() {
		return bibleMap;
	}

	@Override
	public ArrayList<String> getBiblesAsList() {
		ArrayList<String> biblesAsList = new ArrayList<String>();
		for (String bibleName : bibleMap.keySet()) {
			if (!bibleName.equals(Constants.BIBLE_LUTHER_1912_STRONG)) {
				biblesAsList.add(bibleName);
			}
		}
		return biblesAsList;
	}
}