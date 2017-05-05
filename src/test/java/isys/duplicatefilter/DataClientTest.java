package isys.duplicatefilter;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DataClientTest {

    @Rule
    public WireMockRule server = new WireMockRule(0);

    private DataClient client;

    @Before
    public void setUp() throws Exception {
        client = new DataClient(new RestTemplate(), "http://localhost:" + server.port());
    }

    @Test
    public void shouldFetchNumberOfPages() throws Exception {
        stubFor(get(urlEqualTo("/articles_pages"))
                .willReturn(okJson(ExampleData.OK_JSON))
        );

        int numberOfPages = client.getNumberOfPages();

        assertThat(numberOfPages).isEqualTo(142);
    }
}