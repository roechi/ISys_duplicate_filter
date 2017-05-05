package isys.duplicatefilter;

import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class DataClient {

    private final RestTemplate restTemplate;
    private final String serverUrl;

    public int getNumberOfPages() {

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(serverUrl + "/articles_pages", String.class);

        int numberOfPages = (int) JsonPath.read(responseEntity.getBody(), "$.pages");

        return numberOfPages;
    }
}
