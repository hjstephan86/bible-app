package com.bible.app.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bible.app.concordance.Item;
import com.bible.app.helper.Helper;
import com.bible.app.model.Passage;
import com.bible.app.model.Search;
import com.bible.app.model.SearchResult;
import com.bible.app.model.Section;
import com.bible.app.model.Word;
import com.bible.app.service.BibleService;
import com.bible.app.text.Verse;

@Controller
public class BibleController {

	@Autowired
	private BibleService bibleService;

	private static final Logger LOGGER = LoggerFactory.getLogger(BibleController.class);

	@GetMapping({ "/", "/home" })
	public String home(Model model) {
		model.addAttribute("bible", bibleService.getActive());
		model.addAttribute("bibles", bibleService.getBiblesAsList());
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "home";
	}

	@PostMapping({ "/", "/home" })
	public String home(Model model, @RequestParam String bibleName) {
		bibleService.setActive(bibleName);
		model.addAttribute("bible", bibleService.getActive());
		model.addAttribute("bibles", bibleService.getBiblesAsList());
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("bible", bibleService.getActive());
		model.addAttribute("bibles", bibleService.getBiblesAsList());
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "about";
	}

	@PostMapping("/about")
	public String about(Model model, @RequestParam String bibleName) {
		bibleService.setActive(bibleName);
		model.addAttribute("bible", bibleService.getActive());
		model.addAttribute("bibles", bibleService.getBiblesAsList());
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "about";
	}

	@GetMapping("/read")
	public String read(Model model) {
		model.addAttribute("bible", bibleService.getActive());
		model.addAttribute("bibles", bibleService.getBiblesAsList());
		model.addAttribute("books", bibleService.getBooksAsList());
		model.addAttribute("chapters", bibleService.getChaptersAsList());
		model.addAttribute("passage", new Passage());
		model.addAttribute("verses", new ArrayList<Verse>());
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "read";
	}

	@PostMapping("/read")
	public String read(@ModelAttribute("passage") Passage passage, @RequestParam String bibleName, Model model) {
		bibleService.setActive(bibleName);
		model.addAttribute("bible", bibleService.getActive());
		model.addAttribute("bibles", bibleService.getBiblesAsList());
		model.addAttribute("books", bibleService.getBooksAsList());
		model.addAttribute("chapters", bibleService.getChaptersAsList());
		if (passage.getBook() != null && bibleService.passageExists(passage)) {
			model.addAttribute("passage", passage);
			model.addAttribute("verses", bibleService.getVerses(passage));
		} else {
			model.addAttribute("passage", new Passage());
			model.addAttribute("verses", new ArrayList<Verse>());
		}
		LOGGER.info(Helper.getRemoteAddrAndRequestURL() + " with " + passage);
		return "read";
	}

	@GetMapping("/search")
	public String search(Model model) {
		model.addAttribute("bible", bibleService.getActive());
		model.addAttribute("bibles", bibleService.getBiblesAsList());
		model.addAttribute("books", bibleService.getBooksAsList());
		model.addAttribute("search", new Search());
		model.addAttribute("searchResult", new SearchResult());
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "search";
	}

	@PostMapping("/search")
	public String search(@ModelAttribute("search") Search search, @RequestParam String bibleName, Model model) {
		bibleService.setActive(bibleName);
		model.addAttribute("bible", bibleService.getActive());
		model.addAttribute("bibles", bibleService.getBiblesAsList());
		model.addAttribute("books", bibleService.getBooksAsList());
		if (search.getSearch() != null && search.getSection() != null
				&& (search.getSection().matches("Alle|AT|NT")
						|| bibleService.getActive().getBookMap().containsKey(search.getSection()))) {
			model.addAttribute("search", search);
			model.addAttribute("searchResult", bibleService.search(search));
		} else {
			model.addAttribute("search", new Search());
			model.addAttribute("searchResult", new SearchResult());
		}
		LOGGER.info(Helper.getRemoteAddrAndRequestURL() + " with " + search);
		return "search";
	}

	@GetMapping("/count")
	public String count(Model model) {
		model.addAttribute("bible", bibleService.getActive());
		model.addAttribute("bibles", bibleService.getBiblesAsList());
		model.addAttribute("books", bibleService.getBooksAsList());
		model.addAttribute("chapters", bibleService.getChaptersAsList());
		model.addAttribute("verses", bibleService.getVersesAsListOfLists());
		model.addAttribute("section", new Section());
		model.addAttribute("words", new ArrayList<Word>());
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "count";
	}

	@PostMapping("/count")
	public String count(@ModelAttribute("section") Section section, @RequestParam String bibleName, Model model) {
		bibleService.setActive(bibleName);
		model.addAttribute("bible", bibleService.getActive());
		model.addAttribute("bibles", bibleService.getBiblesAsList());
		model.addAttribute("books", bibleService.getBooksAsList());
		model.addAttribute("chapters", bibleService.getChaptersAsList());
		model.addAttribute("verses", bibleService.getVersesAsListOfLists());
		if (section.getBookFrom() != null && section.getBookTo() != null && bibleService.sectionIsValid(section)) {
			model.addAttribute("section", section);
			model.addAttribute("words", bibleService.countWords(section));
		} else {
			model.addAttribute("section", new Section());
			model.addAttribute("words", new ArrayList<Word>());
		}
		LOGGER.info(Helper.getRemoteAddrAndRequestURL() + " with " + section);
		return "count";
	}

	@GetMapping("/strong")
	public String strong(Model model) {
		model.addAttribute("books", bibleService.getBooksAsListFromLuther1912Strong());
		model.addAttribute("chapters", bibleService.getChaptersAsListFromLuther1912Strong());
		model.addAttribute("passage", new Passage());
		model.addAttribute("verses", new ArrayList<Verse>());
		model.addAttribute("concordance", new LinkedHashMap<String, Item>());
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "strong";
	}

	@PostMapping("/strong")
	public String strong(@ModelAttribute("passage") Passage passage, @RequestParam String bibleName, Model model) {
		model.addAttribute("books", bibleService.getBooksAsListFromLuther1912Strong());
		model.addAttribute("chapters", bibleService.getChaptersAsListFromLuther1912Strong());
		if (passage.getBook() != null && bibleService.passageExists(passage)) {
			model.addAttribute("passage", passage);
			model.addAttribute("verses", bibleService.getVersesFromLuther1912Strong(passage));
			model.addAttribute("concordance", bibleService.getConcordance());
		} else {
			model.addAttribute("passage", new Passage());
			model.addAttribute("verses", new ArrayList<Verse>());
			model.addAttribute("concordance", new LinkedHashMap<String, Item>());
		}
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "strong";
	}
}
