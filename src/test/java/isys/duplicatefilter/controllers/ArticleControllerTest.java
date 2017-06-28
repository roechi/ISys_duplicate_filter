package isys.duplicatefilter.controllers;


import isys.duplicatefilter.services.FilteredArticleService;
import isys.duplicatefilter.services.RawArticleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ArticleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RawArticleService rawArticleService;

    @Mock
    private FilteredArticleService filteredArticleService;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ArticleController(filteredArticleService, rawArticleService))
                .setControllerAdvice(new ErrorAdvice())
                .build();
    }

    @Test
    public void shouldFetchNumberOfRawPages() throws Exception {
        when(rawArticleService.getNumberOfPages()).thenReturn(10);

         mockMvc
                .perform(get("/articles/pages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pages", equalTo(10)));
    }

    @Test
    public void shouldFetchNumberOfFilteredPages() throws Exception {
        when(rawArticleService.getNumberOfPages()).thenReturn(10);

        mockMvc
                .perform(get("/filteredArticles/pages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pages", equalTo(10)));
    }

    @Test
    public void shouldHandleInvalidPageType() throws Exception {
        when(rawArticleService.getNumberOfPages()).thenReturn(10);

        mockMvc
                .perform(get("/fooArticles/pages"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("/articles/pages")))
                .andExpect(jsonPath("$.error", containsString("/filteredArticles/pages")));

        ;
    }

    @Test
    public void shouldFetchRawArticlePage() throws Exception {
        when(rawArticleService.getNumberOfPages()).thenReturn(43);
        when(rawArticleService.getPage(42)).thenReturn(new ArrayList<>());

        mockMvc
                .perform(get("/articles/page/42"))
                .andExpect(status().isOk());

    }

    @Test
    public void shouldFetchFilteredArticlePage() throws Exception {
        when(filteredArticleService.getNumberOfPages()).thenReturn(43);
        when(filteredArticleService.getPage(42)).thenReturn(new ArrayList<>());

        mockMvc
                .perform(get("/filteredArticles/page/42"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotFetchPageForNotExistingPageType() throws Exception {
        when(rawArticleService.getPage(42)).thenReturn(new ArrayList<>());

        mockMvc
                .perform(get("/fooArticles/page/42"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void fetchRawArticlePageShouldNotWorkWithWrongPageNumber() throws Exception {
        when(rawArticleService.getNumberOfPages()).thenReturn(1);

        mockMvc
                .perform(get("/articles/page/42"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Invalid page number 42.")));
        ;
    }

    @Test
    public void fetchRawArticlePageShouldNotWorkNegativePageNumber() throws Exception {
        when(rawArticleService.getNumberOfPages()).thenReturn(1);

        mockMvc
                .perform(get("/articles/page/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Invalid page number -1.")));
        ;
    }

    @Test
    public void fetchFilteredArticlePageShouldNotWorkWithWrongPageNumber() throws Exception {
        when(filteredArticleService.getNumberOfPages()).thenReturn(1);

        mockMvc
                .perform(get("/filteredArticles/page/42"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Invalid page number 42.")));
        ;
    }
}