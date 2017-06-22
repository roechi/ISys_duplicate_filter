package isys.duplicatefilter;

import com.jayway.jsonpath.JsonPath;
import isys.duplicatefilter.dto.Article;
import isys.duplicatefilter.dto.ArticleList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataClient {

    private final RestTemplate restTemplate;
    private final String serverUrl;

    public int getNumberOfPages() {

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(serverUrl + "/articles_pages", String.class);

        int numberOfPages = JsonPath.read(responseEntity.getBody(), "$.pages");

        return numberOfPages;
    }

    public List<Article> getPage(int pageNumber) {
        ArticleList articles = restTemplate.getForObject(serverUrl + "/articles?page=" + pageNumber, ArticleList.class);
        return articles;
    }
}
