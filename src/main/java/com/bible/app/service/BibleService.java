package com.bible.app.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.bible.app.concordance.Item;
import com.bible.app.creator.Bible;
import com.bible.app.model.Passage;
import com.bible.app.model.Search;
import com.bible.app.model.SearchResult;
import com.bible.app.model.Section;
import com.bible.app.model.Word;
import com.bible.app.text.Verse;

public interface BibleService {

    Bible getActiveBible();

    void setActive(String bibleName);

    ArrayList<String> getBooksAsList();

    ArrayList<String> getChaptersAsList();

    ArrayList<String> getBooksAsListFromLuther1912Strong();

    ArrayList<String> getChaptersAsListFromLuther1912Strong();

    ArrayList<ArrayList<String>> getVersesAsListOfLists();

    ArrayList<Verse> getVerses(Passage passage);

    ArrayList<Verse> getVersesFromLuther1912Strong(Passage passage);

    SearchResult search(Search search);

    List<Word> countWords(Section section);

    boolean passageExists(Passage passage);

    boolean sectionIsValid(Section section);

    LinkedHashMap<String, Item> getConcordance();

}