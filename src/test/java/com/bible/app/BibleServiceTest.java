package com.bible.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bible.app.model.Finding;
import com.bible.app.model.Parallel;
import com.bible.app.model.Passage;
import com.bible.app.model.Search;
import com.bible.app.model.SearchResult;
import com.bible.app.model.Section;
import com.bible.app.model.Word;
import com.bible.app.service.BibleService;

@SpringBootTest
public class BibleServiceTest {

    @Autowired
    private BibleService defaultBibleService;

    @Test
    public void testSearchDefault() {
        int expectedHitCount = 2347;
        int expectedVerseHits = 1989;

        String expectedBook = "Offenbarung";
        int expectedChapter = 21;
        int expectedVerse = 12;

        String expectedVerseText = "Und sie hatte eine große und hohe Mauer und hatte zwölf Tore und auf den Toren zwölf Engel, und Namen darauf geschrieben, nämlich der zwölf Geschlechter der <b>Kinder</b> Israel.";

        Search search = new Search();

        String searchString = "Kinder";
        String sectionString = "1. Mose 1";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = defaultBibleService.search(search);

        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        Finding finding = searchResult.getFindings().get(expectedVerseHits - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());
    }

    @Test
    public void testSearchBook() {
        int expectedHitCount = 138;
        int expectedVerseHits = 121;

        String expectedBook = "1. Mose";
        int expectedChapter = 50;
        int expectedVerse = 25;

        String expectedVerseText = "Darum nahm er einen Eid von den <b>Kinder</b>n Israel und sprach: Wenn euch Gott heimsuchen wird, so führet meine Gebeine von dannen.";

        Search search = new Search();

        String searchString = "Kinder";
        String sectionString = "1. Mose";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = defaultBibleService.search(search);

        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        Finding finding = searchResult.getFindings().get(expectedVerseHits - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());
    }

    @Test
    public void testSearchAlle() {
        int expectedHitCount = 2347;
        int expectedVerseHits = 1989;

        String expectedBook = "Offenbarung";
        int expectedChapter = 21;
        int expectedVerse = 12;

        String expectedVerseText = "Und sie hatte eine große und hohe Mauer und hatte zwölf Tore und auf den Toren zwölf Engel, und Namen darauf geschrieben, nämlich der zwölf Geschlechter der <b>Kinder</b> Israel.";

        Search search = new Search();

        String searchString = "Kinder";
        String sectionString = "Alle";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = defaultBibleService.search(search);

        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        Finding finding = searchResult.getFindings().get(expectedVerseHits - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());
    }

    @Test
    public void testSearchAlleInvalid() {
        int expectedHitCount = 0;
        int expectedVerseHits = 0;

        Search search = new Search();

        String searchString = "<html>";
        String sectionString = "Alle";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = defaultBibleService.search(search);

        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        // Now search only one character
        search = new Search();

        searchString = "e";
        sectionString = "Alle";
        search.setSearch(searchString);
        search.setSection(sectionString);

        searchResult = defaultBibleService.search(search);

        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());
    }

    @Test
    public void testSearchAlleCaseSensitive() {
        int expectedHitCount = 2347;
        int expectedVerseHits = 1989;

        String expectedBook = "Offenbarung";
        int expectedChapter = 21;
        int expectedVerse = 12;

        String expectedVerseText = "Und sie hatte eine große und hohe Mauer und hatte zwölf Tore und auf den Toren zwölf Engel, und Namen darauf geschrieben, nämlich der zwölf Geschlechter der <b>Kinder</b> Israel.";

        Search search = new Search();

        String searchString = "Kinder";
        String sectionString = "Alle";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = defaultBibleService.search(search);

        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        Finding finding = searchResult.getFindings().get(expectedVerseHits - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());

        // Now, search for 'kinder'
        expectedHitCount = 54;
        expectedVerseHits = 54;

        expectedVerseText = "welches nicht kundgetan ist in den vorigen Zeiten den Menschen<b>kinder</b>n, wie es nun offenbart ist seinen heiligen Aposteln und Propheten durch den Geist,";
        expectedBook = "Epheser";
        expectedChapter = 3;
        expectedVerse = 5;

        searchString = Constants.SEARCH_MATCH_CASE_SYMBOL + "kinder" + Constants.SEARCH_MATCH_CASE_SYMBOL;
        search.setSearch(searchString);

        searchResult = defaultBibleService.search(search);

        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        finding = searchResult.getFindings().get(expectedVerseHits - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());

    }

    @Test
    public void testSearchAlleMatchExcact() {
        int expectedHitCount = 48575;
        int expectedVerseHits = 23914;

        String expectedBook = "Offenbarung";
        int expectedChapter = 22;
        int expectedVerse = 19;

        String expectedVerseText = "<b>Und</b> wenn jemand davontut von den Worten des Buchs dieser Weissagung, so wird Gott abtun sein Teil vom Holz des Lebens <b>und</b> von der heiligen Stadt, davon in diesem Buch geschrieben ist.";

        Search search = new Search();

        String searchString = "Und";
        String sectionString = "Alle";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = defaultBibleService.search(search);

        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        Finding finding = searchResult.getFindings().get(expectedVerseHits - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());

        // Now, search for "Und"
        expectedHitCount = 46323;
        expectedVerseHits = 23529;

        expectedVerseText = "<b>Und</b> wenn jemand davontut von den Worten des Buchs dieser Weissagung, so wird Gott abtun sein Teil vom Holz des Lebens <b>und</b> von der heiligen Stadt, davon in diesem Buch geschrieben ist.";
        expectedBook = "Offenbarung";
        expectedChapter = 22;
        expectedVerse = 19;

        searchString = Constants.SEARCH_MATCH_EXACT_SYMBOL + "Und" + Constants.SEARCH_MATCH_EXACT_SYMBOL;
        search.setSearch(searchString);

        searchResult = defaultBibleService.search(search);

        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        finding = searchResult.getFindings().get(expectedVerseHits - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());
    }

    @Test
    public void testSearchAlleMatchExcactCaseSensitive() {
        int expectedHitCount = 48575;
        int expectedVerseHits = 23914;

        String expectedBook = "Offenbarung";
        int expectedChapter = 22;
        int expectedVerse = 19;
        String expectedVerseText = "<b>Und</b> wenn jemand davontut von den Worten des Buchs dieser Weissagung, so wird Gott abtun sein Teil vom Holz des Lebens <b>und</b> von der heiligen Stadt, davon in diesem Buch geschrieben ist.";

        Search search = new Search();

        String searchString = "Und";
        String sectionString = "Alle";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = defaultBibleService.search(search);

        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        Finding finding = searchResult.getFindings().get(expectedVerseHits - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());

        // Now, search for "'Und'"
        expectedHitCount = 7730;
        expectedVerseHits = 7072;

        expectedVerseText = "<b>Und</b> wenn jemand davontut von den Worten des Buchs dieser Weissagung, so wird Gott abtun sein Teil vom Holz des Lebens und von der heiligen Stadt, davon in diesem Buch geschrieben ist.";
        expectedBook = "Offenbarung";
        expectedChapter = 22;
        expectedVerse = 19;

        searchString = Constants.SEARCH_MATCH_EXACT_SYMBOL + Constants.SEARCH_MATCH_CASE_SYMBOL + "Und"
                + Constants.SEARCH_MATCH_CASE_SYMBOL + Constants.SEARCH_MATCH_EXACT_SYMBOL;
        search.setSearch(searchString);

        searchResult = defaultBibleService.search(search);

        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        finding = searchResult.getFindings().get(expectedVerseHits - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());
    }

    @Test
    public void testSearchAlleEmpty() {
        int expectedHitCount = 0;
        int expectedVerseHits = 0;

        Search search = new Search();

        String searchString = "";
        String sectionString = "Alle";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = defaultBibleService.search(search);

        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());
    }

    @Test
    public void testFloodSearchAlleKinder() {
        int expectedHitCount = 795;
        int expectedVerseHits = 337;

        String expectedBook = "Offenbarung";
        int expectedChapter = 7;
        int expectedVerse = 4;

        String expectedVerseText = "Und ich hörte die Zahl derer, die versiegelt wurden: hundertvierundvierzigtausend, die versiegelt waren von <b>alle</b>n Geschlechtern der <b>Kinder</b> Israel:";

        Search search = new Search();

        String searchString = "f Alle Kinder";
        String sectionString = "Alle";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = defaultBibleService.search(search);

        assertEquals(searchResult.isFloodSearch(), true);
        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        Finding finding = searchResult.getFindings().get(expectedVerseHits - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());

        // Now search for f "Alle" Kinder
        expectedHitCount = 0;
        expectedVerseHits = 0;

        search = new Search();
        searchString = "f \"Alle\" Kinder";
        sectionString = "Alle";
        search.setSearch(searchString);
        search.setSection(sectionString);

        searchResult = defaultBibleService.search(search);

        assertEquals(searchResult.isFloodSearch(), true);
        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());
    }

    @Test
    public void testFloodSearchAlle() {
        int expectedHitCount = 1440;
        int expectedVerseHits = 129;

        String expectedBook = "Offenbarung";
        int expectedChapter = 21;
        int expectedVerse = 3;
        String expectedVerseText = "Und ich hörte eine große Stimme von dem Stuhl, die sprach: <b>Sie</b>he da, die Hütte Gott<b>es</b> bei den Menschen! und <b>er</b> <b>wir</b>d bei <b>ihn</b>en wohnen, und <b>sie</b> w<b>er</b>den sein Volk sein, und <b>er</b> selbst, Gott mit <b>ihn</b>en, <b>wir</b>d ihr Gott sein;";

        Search search = new Search();

        String searchString = "f er sie es wir ihn";
        String sectionString = "Alle";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = defaultBibleService.search(search);

        assertEquals(searchResult.isFloodSearch(), true);
        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        Finding finding = searchResult.getFindings().get(expectedVerseHits - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());
    }

    @Test
    public void testFloodSearchAlleJoh316() {
        int expectedHitCount = 0;
        int expectedVerseHits = 0;

        Search search = new Search();

        String searchString = "f Also hat Gott die Welt geliebt";
        String sectionString = "Alle";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = defaultBibleService.search(search);

        assertEquals(searchResult.isFloodSearch(), false);
        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        // Now search for f Also hat Gott die Welt
        expectedHitCount = 6;
        expectedVerseHits = 1;

        String expectedBook = "Johannes";
        int expectedChapter = 3;
        int expectedVerse = 16;
        String expectedVerseText = "<b>Also</b> <b>hat</b> <b>Gott</b> <b>die</b> <b>Welt</b> geliebt, dass er seinen eingeborenen Sohn gab, auf dass alle, <b>die</b> an ihn glauben, nicht verloren werden, sondern das ewige Leben haben.";

        search = new Search();
        searchString = "f Also hat Gott die Welt";
        sectionString = "Alle";
        search.setSearch(searchString);
        search.setSection(sectionString);

        searchResult = defaultBibleService.search(search);

        assertEquals(searchResult.isFloodSearch(), true);
        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        Finding finding = searchResult.getFindings().get(expectedVerseHits - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());
    }

    @Test
    public void testFloodSearchJerLandLand() {
        int expectedHitCount = 27;
        int expectedVerseHits = 7;

        String expectedBook = "Jeremia";
        int expectedChapter = 33;
        int expectedVerse = 11;
        String expectedVerseText = "wird man dennoch wiederum <b>höre</b>n Geschrei von Freude und Wonne, die Stimme <b>des</b> Bräutigams und der Braut und die Stimme derer, die da sagen: „Danket dem HERRN Zebaoth; denn er ist freundlich, und seine Güte währet ewiglich“, wenn sie Dankopfer bringen zum Hause <b>des</b> HERRN. Denn ich will <b>des</b> <b>Land</b>es Gefängnis wenden wie von Anfang, spricht der HERR.";

        Search search = new Search();

        String searchString = "f Land Land höre des";
        String sectionString = "Jeremia";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = defaultBibleService.search(search);

        assertEquals(searchResult.isFloodSearch(), true);
        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        Finding finding = searchResult.getFindings().get(expectedVerseHits - 5);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());
    }

    @Test
    public void testFloodSearchAlleInvalid() {
        int expectedHitCount = 0;
        int expectedVerseHits = 0;

        Search search = new Search();

        String searchString = "f a b c d e";
        String sectionString = "Alle";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = defaultBibleService.search(search);

        assertEquals(searchResult.isFloodSearch(), false);
        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        // Now search for f "Alle Kinder"
        search = new Search();

        searchString = "f \"Alle Kinder\"";
        sectionString = "Alle";
        search.setSearch(searchString);
        search.setSection(sectionString);

        searchResult = defaultBibleService.search(search);

        assertEquals(searchResult.isFloodSearch(), false);
        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        // Now search for f 'Alle Kinder'
        search = new Search();

        searchString = "f \'Alle Kinder\'";
        sectionString = "Alle";
        search.setSearch(searchString);
        search.setSection(sectionString);

        searchResult = defaultBibleService.search(search);

        assertEquals(searchResult.isFloodSearch(), false);
        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());
    }

    @Test
    public void testSearchAlleCaseSensitiveEmpty() {
        int expectedHitCount = 0;
        int expectedVerseHits = 0;

        Search search = new Search();

        String searchString = "\"\"";
        String sectionString = "Alle";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = defaultBibleService.search(search);

        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());
    }

    @Test
    public void testSearchAT() {
        int expectedHitCount = 2185;
        int expectedVerseHits = 1838;

        String expectedBook = "Maleachi";
        int expectedChapter = 3;
        int expectedVerse = 24;

        String expectedVerseText = "Der soll das Herz der Väter bekehren zu den <b>Kinder</b>n und das Herz der <b>Kinder</b> zu ihren Vätern, dass ich nicht komme und das Erdreich mit dem Bann schlage.";

        Search search = new Search();

        String searchString = "Kinder";
        String sectionString = "AT";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = defaultBibleService.search(search);

        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        Finding finding = searchResult.getFindings().get(expectedVerseHits - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());
    }

    @Test
    public void testSearchNT() {
        int expectedHitCount = 162;
        int expectedVerseHits = 151;

        String expectedBook = "Offenbarung";
        int expectedChapter = 21;
        int expectedVerse = 12;

        String expectedVerseText = "Und sie hatte eine große und hohe Mauer und hatte zwölf Tore und auf den Toren zwölf Engel, und Namen darauf geschrieben, nämlich der zwölf Geschlechter der <b>Kinder</b> Israel.";

        Search search = new Search();

        String searchString = "Kinder";
        String sectionString = "NT";
        search.setSearch(searchString);
        search.setSection(sectionString);

        SearchResult searchResult = defaultBibleService.search(search);

        assertEquals(expectedHitCount, searchResult.getHitCount());
        assertEquals(expectedVerseHits, searchResult.getFindings().size());

        Finding finding = searchResult.getFindings().get(expectedVerseHits - 1);
        assertEquals(expectedBook, finding.getPassage().getBook());
        assertEquals(expectedChapter, finding.getPassage().getChapter());
        assertEquals(expectedVerse, finding.getPassage().getVerse());
        assertEquals(expectedVerseText, finding.getVerseText());
    }

    @Test
    public void testCountWords() {
        int expectedWordSize = 22416;

        String expectedName = "HERR";
        int expectedHitCount = 3727;

        Section section = new Section();
        section.setBookFrom("1. Mose");
        section.setChapterFrom(1);
        section.setVerseFrom(1);

        section.setBookTo("Offenbarung");
        section.setChapterTo(22);
        section.setVerseTo(21);

        List<Word> wordList = defaultBibleService.countWords(section);

        assertEquals(expectedWordSize, wordList.size());

        Word word = wordList.get(25);
        assertEquals(word.getName(), expectedName);
        assertEquals(word.getCount(), expectedHitCount);
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
        assertEquals(defaultBibleService.sectionIsValid(section), true);

        section.setChapterTo(1);
        section.setVerseTo(1);
        assertEquals(defaultBibleService.sectionIsValid(section), true);

        section.setVerseFrom(2);
        assertEquals(defaultBibleService.sectionIsValid(section), false);

        section.setBookFrom("2. Mose");
        assertEquals(defaultBibleService.sectionIsValid(section), false);
    }

    @Test
    public void testPassageExists() {
        String book = "1. Mose";
        int chapter = 1;
        int verse = 1;

        Passage passage = new Passage(book, chapter);
        assertTrue(defaultBibleService.passageExists(passage));

        passage = new Passage(book, chapter, verse);
        assertTrue(defaultBibleService.passageExists(passage));

        passage = new Passage(book, chapter, 100);
        assertFalse(defaultBibleService.passageExists(passage));
    }

    @Test
    public void testGetParallelPassageExists() {
        String book = "1. Mose";
        int chapter = 1;
        int verse = 1;

        Passage passage = new Passage(book, chapter, verse);
        Parallel parallel = defaultBibleService.getParallel(passage, defaultBibleService.getActiveBible().getName());

        assertTrue(parallel != null);
        assertTrue(parallel.getVerses().size() == 8);
        assertTrue(parallel.getBibleNames().size() == 8);
        assertEquals(parallel.getBibleNames().get(0), defaultBibleService.getActiveBible().getName());
    }

    @Test
    public void testGetParallelPassageNotExists() {
        String book = "1. Mose";
        int chapter = 100;
        int verse = 100;

        Passage passage = new Passage(book, chapter, verse);
        Parallel parallel = defaultBibleService.getParallel(passage, defaultBibleService.getActiveBible().getName());

        assertTrue(parallel != null);
        assertTrue(parallel.getVerses().size() == 0);
        assertTrue(parallel.getBibleNames().size() == 0);
    }
}
