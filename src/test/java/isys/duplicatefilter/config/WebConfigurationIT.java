package isys.duplicatefilter.config;

import isys.duplicatefilter.DuplicateFilterApp;
import isys.duplicatefilter.controllers.ArticleController;
import isys.duplicatefilter.services.FilteredArticleService;
import isys.duplicatefilter.services.RawArticleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {DuplicateFilterApp.class, WebConfiguration.class},
        properties = "scheduling.enabled=false")
@RunWith(SpringRunner.class)
public class WebConfigurationIT {

    private MockMvc mockMvc;

    @Autowired
    private RawArticleService rawArticleService;


    @Autowired
    private FilteredArticleService filteredArticleService;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new ArticleController(filteredArticleService, rawArticleService)).build();
    }

    @Test
    public void shouldRedirect() throws Exception {
        mockMvc.perform(get("/duplicates").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void shouldEncodeWithUTF8() throws Exception {
        mockMvc.perform(get("/articles/pages").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().encoding("UTF-8"));
    }
}