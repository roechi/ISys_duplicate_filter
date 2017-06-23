package isys.duplicatefilter;

import com.jayway.jsonpath.JsonPath;
import isys.duplicatefilter.dto.Article;
import isys.duplicatefilter.dto.ArticleList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataClient {

    private final RestTemplate restTemplate;
    private final String serverUrl;

    public int getNumberOfPages() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(serverUrl + "/articles_pages", String.class);

        return JsonPath.read(responseEntity.getBody(), "$.pages");
    }

    public List<Article> getPage(int pageNumber) {
        try {
            return restTemplate.getForObject(serverUrl + "/articles?page=" + pageNumber, ArticleList.class);
        } catch (RuntimeException e) {
            log.error(MessageFormat.format("Got error while fetching page {0}.", pageNumber), e.getMessage());
            return new ArrayList<>();
        }
    }
}
