package isys.duplicatefilter.services;


import isys.duplicatefilter.DataClient;
import isys.duplicatefilter.dto.Article;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UpdateServiceTest {

    @Mock
    private DataClient dataClient;

    @Mock
    private RawArticleService rawArticleService;

    @Mock
    private FilteredArticleService filteredArticleService;

    private UpdateService service;

    @Before
    public void setUp() throws Exception {
        service = new UpdateServiceForTesting(dataClient, rawArticleService, filteredArticleService);

    }

    @Test
    public void shouldFetchAndStore() throws Exception {
        List<Article> articles = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> createTestArticle())
                .collect(Collectors.toList());

        when(dataClient.getNumberOfPages()).thenReturn(3);
        when(dataClient.getPage(Matchers.anyInt())).thenReturn(articles);

        service.updateArticles();

        verify(rawArticleService, times(30)).save(isA(Article.class));
    }

    private Article createTestArticle() {
        return new Article()
                .setContent(UUID.randomUUID().toString())
                .setTitle(UUID.randomUUID().toString());
    }

    private class UpdateServiceForTesting extends UpdateService {
        public UpdateServiceForTesting(DataClient dataClient, RawArticleService rawArticleService, FilteredArticleService filteredArticleService) {
            super(dataClient, rawArticleService, filteredArticleService);
        }

        @Override
        protected void filter() {
            //do nothing
        }
    }

}