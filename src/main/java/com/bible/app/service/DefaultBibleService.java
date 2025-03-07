package com.bible.app.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.bible.app.Constants;
import com.bible.app.concordance.Item;
import com.bible.app.creator.Bible;
import com.bible.app.creator.bible.Luther1912Strong;
import com.bible.app.model.Parallel;
import com.bible.app.model.Passage;
import com.bible.app.model.Search;
import com.bible.app.model.SearchResult;
import com.bible.app.model.Section;
import com.bible.app.model.Word;
import com.bible.app.text.Book;
import com.bible.app.text.Chapter;
import com.bible.app.text.Verse;

@Service
@SessionScope
public class DefaultBibleService implements BibleService {

    private final BiblesService defaultBiblesService;
    private final Bible luther1912Strong;
    private Bible activeBible;

    public DefaultBibleService(BiblesService defaultBiblesService) {
        this.defaultBiblesService = defaultBiblesService;
        this.activeBible = defaultBiblesService.getBibleMap().get(Constants.BIBLE_LUTHER_1912);
        this.luther1912Strong = defaultBiblesService.getBibleMap().get(Constants.BIBLE_LUTHER_1912_STRONG);
    }

    @Override
    public Bible getActiveBible() {
        return activeBible;
    }

    @Override
    public void setActiveBible(String bibleName) {
        activeBible = defaultBiblesService.getBibleMap().get(bibleName);
    }

    @Override
    public ArrayList<String> getBooksAsList() {
        return activeBible.getBooksAsList();
    }

    @Override
    public ArrayList<String> getChaptersAsList() {
        return activeBible.getChaptersAsList();
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
        return activeBible.getVersesAsListOfLists();
    }

    @Override
    public ArrayList<Verse> getVerses(Passage passage) {
        return activeBible.getVerses(activeBible, passage);
    }

    @Override
    public ArrayList<Verse> getVersesFromLuther1912Strong(Passage passage) {
        return luther1912Strong.getVerses(luther1912Strong, passage);
    }

    @Override
    public SearchResult search(Search search) {
        return activeBible.search(search);
    }

    @Override
    public List<Word> countWords(Section section) {
        return activeBible.countWords(section);
    }

    @Override
    public boolean passageExists(Passage passage) {
        return activeBible.passageExists(passage);
    }

    @Override
    public boolean sectionIsValid(Section section) {
        return activeBible.sectionIsValid(section);
    }

    @Override
    public LinkedHashMap<String, Item> getConcordance() {
        if (luther1912Strong != null)
            return ((Luther1912Strong) luther1912Strong).getConcordance();
        return null;
    }

    @Override
    public Parallel getParallel(Passage passage, String bibleName) {
        Parallel parallel = new Parallel();
        for (Bible bible : defaultBiblesService.getBibleMap().values()) {
            setParallelForBible(passage, parallel, bible);
        }
        return parallel;
    }

    private void setParallelForBible(Passage passage, Parallel parallel, Bible bible) {
        if (!(bible instanceof Luther1912Strong)) {
            Book book = bible.getBookMap().get(passage.getBook());
            if (book != null) {
                Chapter chapter = book.getChapter().get(passage.getChapter());
                if (chapter != null) {
                    Verse verse = chapter.getVerses().get(passage.getVerse());
                    if (verse != null) {
                        parallel.addVerse(verse);
                        parallel.addBibleName(bible.getName());
                    }
                }
            }
        }
    }
}
