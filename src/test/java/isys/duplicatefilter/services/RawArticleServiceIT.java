package isys.duplicatefilter.services;

import isys.duplicatefilter.DuplicateFilterApp;
import isys.duplicatefilter.config.WebConfiguration;
import isys.duplicatefilter.dto.Article;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DuplicateFilterApp.class, WebConfiguration.class})
public class RawArticleServiceIT {

    @Autowired
    private RawArticleService articleService;

    @Test
    public void testFetchArticle() {
        Article article = new Article();
        article.setContent("Test");
        String title = "Title";
        article.setTitle(title);
        articleService.save(article);

        List<Article> fetched = articleService.findAll();

        assertThat(fetched).isNotNull();
        assertThat(fetched.stream().anyMatch(art -> art.getTitle().equals(title))).isTrue();
    }

}
