package com.bible.app;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.bible.app.controller.BibleRestController;
import com.bible.app.creator.Bible;
import com.bible.app.creator.BibleCreator;
import com.bible.app.creator.bible.Luther1912Strong;
import com.bible.app.model.Passage;
import com.bible.app.model.Search;
import com.bible.app.model.Section;
import com.bible.app.model.Strong;
import com.bible.app.model.Word;
import com.bible.app.service.BibleService;
import com.bible.app.service.BiblesService;
import com.bible.app.text.Book;
import com.bible.app.text.Verse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@WebMvcTest(BibleRestController.class)
public class BibleRestControllerTest {

    private final String API_PATH = "/api/v1/";

    @MockBean
    private BibleService activeBibleService;

    @MockBean
    private BiblesService defaultBibleService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHello() throws Exception {
        this.mockMvc.perform(get(API_PATH + "hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, welcome to the Bible Application REST API"));
    }

    @Test
    public void testBibles() throws Exception {
        ArrayList<String> bibles = new ArrayList<String>();
        String bible = "Bible";
        bibles.add(bible);

        when(defaultBibleService.getBiblesAsList()).thenReturn(bibles);

        this.mockMvc.perform(get(API_PATH + "bibles"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(bible)));
    }

    @Test
    public void testBibleExists() throws Exception {
        Bible luther1912 = BibleCreator.getBible("Luther 1912");
        Map<String, Bible> bibleMap = new HashMap<String, Bible>();
        bibleMap.put(luther1912.getName(), luther1912);

        when(defaultBibleService.getBibleMap()).thenReturn(bibleMap);

        this.mockMvc.perform(post(API_PATH + "bible", luther1912.getName()).param("bible", luther1912.getName()))
                .andExpect(status().isOk())
                .andExpect(content().string("Bible " + luther1912.getName() + " activated."));
    }

    @Test
    public void testBibleNotExists() throws Exception {
        Bible luther1912 = BibleCreator.getBible("Luther 1912");
        Map<String, Bible> bibleMap = new HashMap<String, Bible>();

        when(defaultBibleService.getBibleMap()).thenReturn(bibleMap);

        this.mockMvc.perform(post(API_PATH + "bible", luther1912.getName()).param("bible", luther1912.getName()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Could not activate " + luther1912.getName() + "."));
    }

    @Test
    public void testBooks() throws Exception {
        Bible luther1912 = BibleCreator.getBible("Luther 1912");

        when(activeBibleService.getActiveBible()).thenReturn(luther1912);

        MvcResult result = this.mockMvc.perform(get(API_PATH + "books"))
                .andExpect(status().isOk()).andReturn();

        assertTrue(result != null);

        String json = result.getResponse().getContentAsString();
        TypeToken<Set<String>> typeToken = new TypeToken<>() {
        };
        Set<String> booksResult = new Gson().fromJson(json, typeToken.getType());
        assertTrue(booksResult.size() == 66);
    }

    @Test
    public void testReadPassageExists() throws Exception {
        Bible luther1912 = BibleCreator.getBible("Luther 1912");
        Book book = luther1912.getBookMap().get("3. Mose");
        Passage passage = new Passage(book.getName(), 2);

        ArrayList<Verse> verses = new ArrayList<Verse>(
                book.getChapter().get(passage.getChapter()).getVerses().values());

        when(activeBibleService.passageExists(any(Passage.class))).thenReturn(true);
        when(activeBibleService.getVerses(any(Passage.class))).thenReturn(verses);

        MvcResult result = this.mockMvc.perform(post(API_PATH + "read")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(passage)))
                .andExpect(status().isOk()).andReturn();
        assertTrue(result != null);

        String json = result.getResponse().getContentAsString();
        TypeToken<ArrayList<Verse>> typeToken = new TypeToken<>() {
        };
        ArrayList<Verse> versesResult = new Gson().fromJson(json, typeToken.getType());
        assertTrue(versesResult.size() == 16);
    }

    @Test
    public void testReadPassageNotExists() throws Exception {
        Bible luther1912 = BibleCreator.getBible("Luther 1912");
        Book book = luther1912.getBookMap().get("3. Mose");
        Passage passage = new Passage(book.getName(), 2);

        when(activeBibleService.passageExists(any(Passage.class))).thenReturn(false);

        this.mockMvc.perform(post(API_PATH + "read")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(passage)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCountSectionIsValid() throws Exception {
        Bible luther1912 = BibleCreator.getBible("Luther 1912");
        Book book = luther1912.getBookMap().get("3. Mose");
        Section section = new Section();
        section.setBookFrom(book.getName());
        section.setChapterFrom(1);
        section.setVerseFrom(1);
        section.setBookTo(book.getName());
        section.setChapterTo(book.getChapter().size());
        section.setVerseTo(book.getChapter().get(book.getChapter().size()).getVerses().size());

        List<Word> words = new ArrayList<Word>();
        words.add(new Word("my Word", 21, false));

        when(activeBibleService.sectionIsValid(any(Section.class))).thenReturn(true);
        when(activeBibleService.countWords(any(Section.class))).thenReturn(words);

        MvcResult result = this.mockMvc.perform(post(API_PATH + "count")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(section)))
                .andExpect(status().isOk()).andReturn();
        assertTrue(result != null);

        String json = result.getResponse().getContentAsString();
        TypeToken<List<Word>> typeToken = new TypeToken<>() {
        };
        List<Word> wordsResult = new Gson().fromJson(json,
                typeToken.getType());
        assertTrue(wordsResult.size() == 1);
    }

    @Test
    public void testCountSectionIsNotValid() throws Exception {
        Bible luther1912 = BibleCreator.getBible("Luther 1912");
        Book book = luther1912.getBookMap().get("3. Mose");
        Section section = new Section();
        section.setBookFrom(book.getName());
        section.setBookTo(book.getName());

        List<Word> words = new ArrayList<Word>();
        words.add(new Word("my Word", 21, false));

        when(activeBibleService.sectionIsValid(any(Section.class))).thenReturn(false);

        this.mockMvc.perform(post(API_PATH + "count")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(section)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSearchSectionNotNull() throws Exception {
        Bible luther1912 = BibleCreator.getBible("Luther 1912");
        Search search = new Search();
        search.setSearch("Kinder");
        search.setSection("1. Mose");

        when(activeBibleService.getActiveBible()).thenReturn(luther1912);

        MvcResult result = this.mockMvc.perform(post(API_PATH + "search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(search)))
                .andExpect(status().isOk()).andReturn();
        assertTrue(result != null);

        // Unfortunately, content type of response is null

        // String json = result.getResponse().getContentAsString();
        // TypeToken<SearchResult> typeToken = new TypeToken<>() {
        // };
        // SearchResult searchResult = new Gson().fromJson(json, typeToken.getType());
        // assertTrue(searchResult != null);
    }

    @Test
    public void testSearchSectionNull() throws Exception {
        Search search = new Search();
        search.setSearch("Kinder");
        search.setSection(null);

        this.mockMvc.perform(post(API_PATH + "search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(search)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testStrongPassageExists() throws Exception {
        Bible luther1912Strong = BibleCreator.getBible("Luther 1912 Strong");
        Book book = luther1912Strong.getBookMap().get("3. Mose");
        Passage passage = new Passage(book.getName(), 2);

        ArrayList<Verse> verses = new ArrayList<Verse>(
                book.getChapter().get(passage.getChapter()).getVerses().values());

        when(activeBibleService.passageExists(any(Passage.class))).thenReturn(true);
        when(activeBibleService.getVersesFromLuther1912Strong(any(Passage.class))).thenReturn(verses);
        when(activeBibleService.getConcordance()).thenReturn(((Luther1912Strong) luther1912Strong).getConcordance());

        MvcResult result = this.mockMvc.perform(post(API_PATH + "strong")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(passage)))
                .andExpect(status().isOk()).andReturn();
        assertTrue(result != null);

        String json = result.getResponse().getContentAsString();
        TypeToken<Strong> typeToken = new TypeToken<>() {
        };
        Strong strongResult = new Gson().fromJson(json, typeToken.getType());
        assertTrue(strongResult.getVerses().size() == 16);
    }

    @Test
    public void testStrongPassageNotExists() throws Exception {
        Bible luther1912Strong = BibleCreator.getBible("Luther 1912 Strong");
        Book book = luther1912Strong.getBookMap().get("3. Mose");
        Passage passage = new Passage(book.getName(), 2);

        when(activeBibleService.passageExists(any(Passage.class))).thenReturn(false);

        this.mockMvc.perform(post(API_PATH + "strong")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(passage)))
                .andExpect(status().isBadRequest());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
