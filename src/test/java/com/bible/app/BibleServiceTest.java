package com.bible.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import com.bible.app.model.Finding;
import com.bible.app.model.Search;
import com.bible.app.model.SearchResult;
import com.bible.app.model.Section;
import com.bible.app.model.Word;
import com.bible.app.service.BibleService;

@SpringBootTest
public class BibleServiceTest {

    @InjectMocks
    BibleService bibleService;

    @Test
    public void testSearchDefault() {
        int expectedCount = 2347;
        int expectedSearchResults = 1989;

        String expectedBook = "Offenbarung";
        int expectedChapter = 21;
        int expectedVerse = 12;

        String expectedVerseText = "Und sie hatte eine große und hohe Mauer und hatte zwölf Tore und auf den Toren zwölf Engel, und Namen darauf geschrieben, nämlich der zwölf Geschlechter der <b>Kinder</b> Israel.";

        Search search = new Search();

        String searchString = "Kinder";
        String sectionString = "1. Mose 1";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = bibleService.search(search);

        assertEquals(expectedCount, searchResult.getCount());
        assertEquals(expectedSearchResults, searchResult.getFindings().size());

        Finding finding = searchResult.getFindings().get(expectedSearchResults - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());
    }

    @Test
    public void testSearchBook() {
        int expectedCount = 138;
        int expectedSearchResults = 121;

        String expectedBook = "1. Mose";
        int expectedChapter = 50;
        int expectedVerse = 25;

        String expectedVerseText = "Darum nahm er einen Eid von den <b>Kinder</b>n Israel und sprach: Wenn euch Gott heimsuchen wird, so führet meine Gebeine von dannen.";

        Search search = new Search();

        String searchString = "Kinder";
        String sectionString = "1. Mose";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = bibleService.search(search);

        assertEquals(expectedCount, searchResult.getCount());
        assertEquals(expectedSearchResults, searchResult.getFindings().size());

        Finding finding = searchResult.getFindings().get(expectedSearchResults - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());
    }

    @Test
    public void testSearchAlle() {
        int expectedCount = 2347;
        int expectedSearchResults = 1989;

        String expectedBook = "Offenbarung";
        int expectedChapter = 21;
        int expectedVerse = 12;

        String expectedVerseText = "Und sie hatte eine große und hohe Mauer und hatte zwölf Tore und auf den Toren zwölf Engel, und Namen darauf geschrieben, nämlich der zwölf Geschlechter der <b>Kinder</b> Israel.";

        Search search = new Search();

        String searchString = "Kinder";
        String sectionString = "Alle";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = bibleService.search(search);

        assertEquals(expectedCount, searchResult.getCount());
        assertEquals(expectedSearchResults, searchResult.getFindings().size());

        Finding finding = searchResult.getFindings().get(expectedSearchResults - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());
    }

    @Test
    public void testSearchAT() {
        int expectedCount = 2185;
        int expectedSearchResults = 1838;

        String expectedBook = "Maleachi";
        int expectedChapter = 3;
        int expectedVerse = 24;

        String expectedVerseText = "Der soll das Herz der Väter bekehren zu den <b>Kinder</b>n und das Herz der <b>Kinder</b> zu ihren Vätern, dass ich nicht komme und das Erdreich mit dem Bann schlage.";

        Search search = new Search();

        String searchString = "Kinder";
        String sectionString = "AT";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = bibleService.search(search);

        assertEquals(expectedCount, searchResult.getCount());
        assertEquals(expectedSearchResults, searchResult.getFindings().size());

        Finding finding = searchResult.getFindings().get(expectedSearchResults - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());
    }

    @Test
    public void testSearchNT() {
        int expectedCount = 162;
        int expectedSearchResults = 151;

        String expectedBook = "Offenbarung";
        int expectedChapter = 21;
        int expectedVerse = 12;

        String expectedVerseText = "Und sie hatte eine große und hohe Mauer und hatte zwölf Tore und auf den Toren zwölf Engel, und Namen darauf geschrieben, nämlich der zwölf Geschlechter der <b>Kinder</b> Israel.";

        Search search = new Search();

        String searchString = "Kinder";
        String sectionString = "NT";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = bibleService.search(search);

        assertEquals(expectedCount, searchResult.getCount());
        assertEquals(expectedSearchResults, searchResult.getFindings().size());

        Finding finding = searchResult.getFindings().get(expectedSearchResults - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());
    }

    @Test
    public void testCountWords() {
        int expectedWordSize = 22416;

        String expectedName = "HERR";
        int expectedCount = 3727;

        Section section = new Section();
        section.setBookFrom("1. Mose");
        section.setChapterFrom(1);
        section.setVerseFrom(1);

        section.setBookTo("Offenbarung");
        section.setChapterTo(22);
        section.setVerseTo(21);

        List<Word> wordList = bibleService.countWords(section);

        assertEquals(expectedWordSize, wordList.size());

        Word word = wordList.get(25);
        assertEquals(word.getName(), expectedName);
        assertEquals(word.getCount(), expectedCount);
    }

    @Test
    public void testIsSectionValid() {
        Section section = new Section();
        section.setBookFrom("1. Mose");
        section.setChapterFrom(1);
        section.setVerseFrom(1);

        section.setBookTo("1. Mose");
        section.setChapterTo(2);
        section.setVerseTo(2);
        assertEquals(bibleService.sectionIsValid(section), true);

        section.setChapterTo(1);
        section.setVerseTo(1);
        assertEquals(bibleService.sectionIsValid(section), true);

        section.setVerseFrom(2);
        assertEquals(bibleService.sectionIsValid(section), false);

        section.setBookFrom("2. Mose");
        assertEquals(bibleService.sectionIsValid(section), false);
    }
}