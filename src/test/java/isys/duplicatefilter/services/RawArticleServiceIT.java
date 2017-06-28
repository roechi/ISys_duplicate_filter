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
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DuplicateFilterApp.class, WebConfiguration.class},
    properties = "scheduling.enabled=false"
)
public class RawArticleServiceIT {

    @Autowired
    private RawArticleService articleService;

    @Test
    public void shouldFetchArticle() {
        String title = saveTestArticle();

        List<Article> fetched = articleService.findAll();

        assertThat(fetched).isNotNull();
        assertThat(fetched.stream().anyMatch(art -> art.getTitle().equals(title))).isTrue();
    }

    @Test
    public void shouldFetchPage() throws Exception {
        IntStream.rangeClosed(1, 101).forEach(num -> saveTestArticle());
        int numberOfPages = articleService.getNumberOfPages();
        assertThat(numberOfPages).isEqualTo(2);

        List<Article> page = articleService.getPage(0);
        assertThat(page).hasSize(100);
    }

    private String saveTestArticle() {
        Article article = new Article();
        article.setContent(UUID.randomUUID().toString());
        String title = UUID.randomUUID().toString();
        article.setTitle(title);
        articleService.save(article);
        return title;
    }

}
