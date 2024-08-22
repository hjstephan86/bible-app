package com.bible.app;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.bible.app.controller.BibleController;
import com.bible.app.service.BibleService;

@WebMvcTest(BibleController.class)
public class BibleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BibleService bibleService;

    @Test
    public void testHome() throws Exception {
        // @Autowired, @InjectMocks ApplicationContext failed exception

        // NOT POSSIBLE to set fields with @MockBean
        // org.thymeleaf.exceptions.TemplateProcessingException: Exception evaluating
        // SpringEL expression: "bible.name" (template: "home" - line 43, col 25)
        // org.springframework.expression.spel.SpelEvaluationException: EL1007E:
        // Property or field 'name' cannot be found on null

        // Bible activeBible = BibleCreator.getBible("Luther 1912");

        // Map<String, Bible> bibleMap = new LinkedHashMap<String, Bible>();
        // bibleMap.put(activeBible.getName(), activeBible);

        // bibleService.setBibleMap(bibleMap);
        // bibleService.setActive(activeBible.getName());
        MvcResult mvcResult = mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("bible"))
                .andExpect(model().attribute("bible", bibleService.getActive()))
                .andExpect(model().attributeExists("bibles"))
                .andExpect(model().attribute("bibles", bibleService.getBiblesAsList()))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        System.out.println("Response: " + responseBody);
    }
}
