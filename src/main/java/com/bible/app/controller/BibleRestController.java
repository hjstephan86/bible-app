package com.bible.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bible.app.helper.Helper;
import com.bible.app.model.Passage;
import com.bible.app.model.Search;
import com.bible.app.model.SearchResult;
import com.bible.app.model.Section;
import com.bible.app.model.Strong;
import com.bible.app.model.Word;
import com.bible.app.service.BibleService;
import com.bible.app.service.BiblesService;
import com.bible.app.text.Verse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "REST Controller for Bible Application")
@RestController
@RequestMapping("/api/v1/")
public class BibleRestController {

    private final BibleService defaultBibleService;
    private final BiblesService defaultBiblesService;
    private static final Logger LOGGER = LoggerFactory.getLogger(BibleRestController.class);

    public BibleRestController(BibleService defaultBibleService, BiblesService defaultsBibleService) {
        this.defaultBibleService = defaultBibleService;
        this.defaultBiblesService = defaultsBibleService;
    }

    @GetMapping("/hello")
    @Operation(summary = "Welcome page")
    public String hello() {
        LOGGER.info(Helper.getRemoteAddrAndRequestURL());
        return "Hello, welcome to the Bible Application REST API";
    }

    @GetMapping("/bibles")
    @Operation(summary = "Get the list of available bible translations")
    public ResponseEntity<ArrayList<String>> bibles() {
        LOGGER.info(Helper.getRemoteAddrAndRequestURL());
        return new ResponseEntity<>(defaultBiblesService.getBiblesAsList(), HttpStatus.OK);
    }

    @PostMapping("/bible")
    @Operation(summary = "Set the active bible translation")
    public ResponseEntity<String> bible(@RequestParam String bible) {
        if (defaultBiblesService.getBibleMap().containsKey(bible)) {
            defaultBibleService.setActive(bible);
            return new ResponseEntity<>("Bible " + bible + " activated.", HttpStatus.OK);
        }
        LOGGER.info(Helper.getRemoteAddrAndRequestURL() + " with " + bible);
        return new ResponseEntity<>("Could not activate " + bible + ".", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/books")
    @Operation(summary = "Get the list of bible books")
    public ResponseEntity<Set<String>> books() {
        LOGGER.info(Helper.getRemoteAddrAndRequestURL());
        return new ResponseEntity<>(defaultBibleService.getActiveBible().getBookMap().keySet(), HttpStatus.OK);
    }

    @PostMapping("/read")
    @Operation(summary = "Read the bible")
    public ResponseEntity<ArrayList<Verse>> read(@RequestBody Passage passage) {
        if (passage.getBook() != null && defaultBibleService.passageExists(passage)) {
            return new ResponseEntity<>(defaultBibleService.getVerses(passage), HttpStatus.OK);
        }
        LOGGER.info(Helper.getRemoteAddrAndRequestURL() + " with " + passage);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/strong")
    @Operation(summary = "Read the bible with strong concordance")
    public ResponseEntity<Strong> strong(@RequestBody Passage passage) {
        if (passage.getBook() != null && defaultBibleService.passageExists(passage)) {
            Strong strong = new Strong();
            strong.setVerses(defaultBibleService.getVersesFromLuther1912Strong(passage));
            strong.setConcordance(defaultBibleService.getConcordance());

            return new ResponseEntity<>(strong, HttpStatus.OK);
        }
        LOGGER.info(Helper.getRemoteAddrAndRequestURL() + " with " + passage);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/search")
    @Operation(summary = "Search the bible, use \"\" for exact and '' for case sensitive search")
    public ResponseEntity<SearchResult> search(@RequestBody Search search) {
        if (search.getSearch() != null && search.getSection() != null
                && (search.getSection().matches("Alle|AT|NT")
                        || defaultBibleService.getActiveBible().getBookMap().containsKey(search.getSection()))) {
            return new ResponseEntity<>(defaultBibleService.search(search), HttpStatus.OK);
        }
        LOGGER.info(Helper.getRemoteAddrAndRequestURL() + " with " + search);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/count")
    @Operation(summary = "Count words in the bible")
    public ResponseEntity<List<Word>> count(@RequestBody Section section) {
        if (section.getBookFrom() != null && section.getBookTo() != null
                && defaultBibleService.sectionIsValid(section)) {
            return new ResponseEntity<>(defaultBibleService.countWords(section), HttpStatus.OK);
        }
        LOGGER.info(Helper.getRemoteAddrAndRequestURL() + " with " + section);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
