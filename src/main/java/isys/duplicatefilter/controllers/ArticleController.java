package isys.duplicatefilter.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import isys.duplicatefilter.dto.Article;
import isys.duplicatefilter.dto.ArticleList;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ArticleController {

    @RequestMapping(path = "/duplicates")
    public
    @ResponseBody
    Collection<Article> deliverFakeData() throws IOException, URISyntaxException {

        byte[] file = Files.readAllBytes(Paths.get(ResourceUtils.getFile("fakeData/example.json").toURI()));
        final ObjectMapper mapper = new ObjectMapper();
        ArticleList articlesList = mapper.readValue(file, ArticleList.class);
        Map<String, Article> articleMap = articlesList.stream().collect(Collectors.toMap(Article::getId, a -> a));
        articleMap.entrySet().forEach((Map.Entry<String, Article> entry) -> {
            Article article = entry.getValue();
            if (articleMap.containsKey(article.getId())) {
                article = articleMap.get(article.getId());
            }
            int outOfTen = (int) (Math.random() * (10 - 1)) + 1;
            if (outOfTen <= 1) {
                String duplicateKey = String.valueOf(articleMap.keySet().toArray()[(int) (Math.random() * (articleMap.size() - 1)) + 1]);
                Article duplicate = articleMap.get(duplicateKey);
                if (!duplicate.getId().equals(article.getId())) {
                    duplicate = articleMap.get(duplicate.getId());

                    for (String id : article.getDuplicateIds()) {
                        articleMap.get(id).getDuplicateIds().add(duplicate.getId());
                        duplicate.getDuplicateIds().add(id);
                    }
                    article.getDuplicateIds().add(duplicate.getId());
                    for (String id : duplicate.getDuplicateIds()) {
                        articleMap.get(id).getDuplicateIds().add(article.getId());
                        article.getDuplicateIds().add(id);
                    }
                    duplicate.getDuplicateIds().add(article.getId());
                }
            }
        });

        return articleMap.values();
    }

}
