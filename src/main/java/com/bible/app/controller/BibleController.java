package com.bible.app.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.bible.app.service.BiblesService;
import com.bible.app.text.Verse;

@Controller
public class BibleController {

	private final BibleService defaultBibleService;
	private final BiblesService defaultBiblesService;
	private static final Logger LOGGER = LoggerFactory.getLogger(BibleController.class);

	public BibleController(BibleService defaultBibleService, BiblesService defauBiblesService) {
		this.defaultBiblesService = defauBiblesService;
		this.defaultBibleService = defaultBibleService;
	}

	@GetMapping({ "/", "/home" })
	public String home(Model model) {
		model.addAttribute("bible", defaultBibleService.getActiveBible());
		model.addAttribute("bibles", defaultBiblesService.getBiblesAsList());
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "home";
	}

	@PostMapping({ "/", "/home" })
	public String home(Model model, @RequestParam String bibleName) {
		defaultBibleService.setActive(bibleName);
		model.addAttribute("bible", defaultBibleService.getActiveBible());
		model.addAttribute("bibles", defaultBiblesService.getBiblesAsList());
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("bible", defaultBibleService.getActiveBible());
		model.addAttribute("bibles", defaultBiblesService.getBiblesAsList());
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "about";
	}

	@PostMapping("/about")
	public String about(Model model, @RequestParam String bibleName) {
		defaultBibleService.setActive(bibleName);
		model.addAttribute("bible", defaultBibleService.getActiveBible());
		model.addAttribute("bibles", defaultBiblesService.getBiblesAsList());
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "about";
	}

	@GetMapping("/readBy")
	public String readBy(Model model, @RequestParam String book, @RequestParam int chapter, @RequestParam int verse) {
		model.addAttribute("bible", defaultBibleService.getActiveBible());
		model.addAttribute("bibles", defaultBiblesService.getBiblesAsList());
		model.addAttribute("books", defaultBibleService.getBooksAsList());
		model.addAttribute("chapters", defaultBibleService.getChaptersAsList());

		Passage passage = new Passage();
		ArrayList<Verse> verses = new ArrayList<>();
		if (book != null && chapter > 0) {
			passage.setBook(book);
			passage.setChapter(chapter);
			passage.setVerse(verse);
			if (defaultBibleService.passageExists(passage)) {
				verses = defaultBibleService.getVerses(passage);
			} else {
				passage = new Passage();
			}
		}
		model.addAttribute("passage", passage);
		model.addAttribute("verses", verses);
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "read";
	}

	@GetMapping("/readStrBy")
	public String readStrBy(Model model, @RequestParam String book, @RequestParam int chapter,
			@RequestParam int verse) {
		model.addAttribute("books", defaultBibleService.getBooksAsListFromLuther1912Strong());
		model.addAttribute("chapters", defaultBibleService.getChaptersAsListFromLuther1912Strong());

		Passage passage = new Passage();
		ArrayList<Verse> verses = new ArrayList<>();
		Object concordance = new LinkedHashMap<String, Item>();
		if (book != null && chapter > 0) {
			passage.setBook(book);
			passage.setChapter(chapter);
			passage.setVerse(verse);
			if (defaultBibleService.passageExists(passage)) {
				verses = defaultBibleService.getVersesFromLuther1912Strong(passage);
				concordance = defaultBibleService.getConcordance();
			} else {
				passage = new Passage();
			}
		}
		model.addAttribute("passage", passage);
		model.addAttribute("verses", verses);
		model.addAttribute("concordance", concordance);
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "strong";
	}

	@GetMapping("/read")
	public String read(Model model) {
		model.addAttribute("bible", defaultBibleService.getActiveBible());
		model.addAttribute("bibles", defaultBiblesService.getBiblesAsList());
		model.addAttribute("books", defaultBibleService.getBooksAsList());
		model.addAttribute("chapters", defaultBibleService.getChaptersAsList());
		model.addAttribute("passage", new Passage());
		model.addAttribute("verses", new ArrayList<Verse>());
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "read";
	}

	@PostMapping("/read")
	public String read(@ModelAttribute("passage") Passage passage, @RequestParam String bibleName, Model model) {
		defaultBibleService.setActive(bibleName);
		model.addAttribute("bible", defaultBibleService.getActiveBible());
		model.addAttribute("bibles", defaultBiblesService.getBiblesAsList());
		model.addAttribute("books", defaultBibleService.getBooksAsList());
		model.addAttribute("chapters", defaultBibleService.getChaptersAsList());
		if (passage.getBook() != null && defaultBibleService.passageExists(passage)) {
			model.addAttribute("passage", passage);
			model.addAttribute("verses", defaultBibleService.getVerses(passage));
			LOGGER.info(Helper.getRemoteAddrAndRequestURL() + " with " + passage);
		} else {
			model.addAttribute("passage", new Passage());
			model.addAttribute("verses", new ArrayList<Verse>());
			LOGGER.info(Helper.getRemoteAddrAndRequestURL() + " with no valid passage");
		}
		return "read";
	}

	@GetMapping("/search")
	public String search(Model model) {
		model.addAttribute("bible", defaultBibleService.getActiveBible());
		model.addAttribute("bibles", defaultBiblesService.getBiblesAsList());
		model.addAttribute("books", defaultBibleService.getBooksAsList());
		model.addAttribute("search", new Search());
		model.addAttribute("searchResult", new SearchResult());
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "search";
	}

	@PostMapping("/search")
	public String search(@ModelAttribute("search") Search search, @RequestParam String bibleName, Model model) {
		defaultBibleService.setActive(bibleName);
		model.addAttribute("bible", defaultBibleService.getActiveBible());
		model.addAttribute("bibles", defaultBiblesService.getBiblesAsList());
		model.addAttribute("books", defaultBibleService.getBooksAsList());
		if (search.getSearch() != null && search.getSection() != null
				&& (search.getSection().matches("Alle|AT|NT")
						|| defaultBibleService.getActiveBible().getBookMap().containsKey(search.getSection()))) {
			model.addAttribute("search", search);
			model.addAttribute("searchResult", defaultBibleService.search(search));
			LOGGER.info(Helper.getRemoteAddrAndRequestURL() + " with " + search);
		} else {
			model.addAttribute("search", new Search());
			model.addAttribute("searchResult", new SearchResult());
			LOGGER.info(Helper.getRemoteAddrAndRequestURL() + " with no valid search");
		}
		return "search";
	}

	@GetMapping("/count")
	public String count(Model model) {
		model.addAttribute("bible", defaultBibleService.getActiveBible());
		model.addAttribute("bibles", defaultBiblesService.getBiblesAsList());
		model.addAttribute("books", defaultBibleService.getBooksAsList());
		model.addAttribute("chapters", defaultBibleService.getChaptersAsList());
		model.addAttribute("verses", defaultBibleService.getVersesAsListOfLists());
		model.addAttribute("section", new Section());
		model.addAttribute("words", new ArrayList<Word>());
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "count";
	}

	@PostMapping("/count")
	public String count(@ModelAttribute("section") Section section, @RequestParam String bibleName, Model model) {
		defaultBibleService.setActive(bibleName);
		model.addAttribute("bible", defaultBibleService.getActiveBible());
		model.addAttribute("bibles", defaultBiblesService.getBiblesAsList());
		model.addAttribute("books", defaultBibleService.getBooksAsList());
		model.addAttribute("chapters", defaultBibleService.getChaptersAsList());
		model.addAttribute("verses", defaultBibleService.getVersesAsListOfLists());
		if (section.getBookFrom() != null && section.getBookTo() != null
				&& defaultBibleService.sectionIsValid(section)) {
			model.addAttribute("section", section);
			model.addAttribute("words", defaultBibleService.countWords(section));
			LOGGER.info(Helper.getRemoteAddrAndRequestURL() + " with " + section);
		} else {
			model.addAttribute("section", new Section());
			model.addAttribute("words", new ArrayList<Word>());
			LOGGER.info(Helper.getRemoteAddrAndRequestURL() + " with no valid section");
		}
		return "count";
	}

	@GetMapping("/strong")
	public String strong(Model model) {
		model.addAttribute("books", defaultBibleService.getBooksAsListFromLuther1912Strong());
		model.addAttribute("chapters", defaultBibleService.getChaptersAsListFromLuther1912Strong());
		model.addAttribute("passage", new Passage());
		model.addAttribute("verses", new ArrayList<Verse>());
		model.addAttribute("concordance", new LinkedHashMap<String, Item>());
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "strong";
	}

	@PostMapping("/strong")
	public String strong(@ModelAttribute("passage") Passage passage, @RequestParam String bibleName, Model model) {
		model.addAttribute("books", defaultBibleService.getBooksAsListFromLuther1912Strong());
		model.addAttribute("chapters", defaultBibleService.getChaptersAsListFromLuther1912Strong());
		if (passage.getBook() != null && defaultBibleService.passageExists(passage)) {
			model.addAttribute("passage", passage);
			model.addAttribute("verses", defaultBibleService.getVersesFromLuther1912Strong(passage));
			model.addAttribute("concordance", defaultBibleService.getConcordance());
		} else {
			model.addAttribute("passage", new Passage());
			model.addAttribute("verses", new ArrayList<Verse>());
			model.addAttribute("concordance", new LinkedHashMap<String, Item>());
		}
		LOGGER.info(Helper.getRemoteAddrAndRequestURL());
		return "strong";
	}
}
