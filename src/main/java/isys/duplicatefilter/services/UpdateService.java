package isys.duplicatefilter.services;

import isys.duplicatefilter.*;
import isys.duplicatefilter.dto.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateService {

    private final static double SIMILARITY_THRESHOLD = 0.7;

    private final DataClient client;
    private final ArticleService service;


    public void updateArticles() {
        log.info("Fetching articles...");

        int numberOfPages = client.getNumberOfPages();

        IntStream
                .rangeClosed(1, numberOfPages)
                .map(operand -> {
                    log.info(MessageFormat.format("Fetching page {0}...", operand));
                    return operand;
                })
                .mapToObj(client::getPage)
                .flatMap(List::stream)
                .forEach(service::save);

        log.info("Articles fetched and saved.");
    }

    //@Scheduled(fixedDelay = 1000 * 60 * 60)
    private void filter() {
        log.info("Filtering.");
        List<Article> allArticles = service.findAll();

        HashMap<String, Article> articleMapComplete = new HashMap<>();
        HashMap<String, List<String>> articleMap = new HashMap<>();

        allArticles
                .stream()
                .filter(article -> !article.getContent().isEmpty())
                .forEach(article -> {
                                articleMap
                                    .put(article.getId(), TextProcessingUtils.getKShingles(article.getContent(), 3));
                                articleMapComplete.put(article.getId(), article);
                        }
                );

        log.info("Loaded articles.");
        MinHash minHash = new MinHash(198);

        LinkedHashMap<String, List<Integer>> minHashMap = new LinkedHashMap<>();

        articleMap.forEach((id, shingles) -> minHashMap.put(id, minHash.hash(shingles)));

        log.info("Hashed articles.");

        LocalSensitiveHashing localSensitiveHashing = new LocalSensitiveHashing(66, 3, minHashMap);
        ArrayList<String> ids = localSensitiveHashing.compareBands();

        log.info("LSH done. Size: " + ids.size());

        int duplicates = 0;
        for (int i = 0; i < ids.size(); i++) {
            String id = ids.get(i);
            List<Integer> hashes = minHashMap.get(id);
            for (int j = i + 1; j < ids.size(); j++) {
                String otherId = ids.get(j);
                List<Integer> otherHashes = minHashMap.get(otherId);
                float similarity = JaccardSimilarity.ofInt(hashes, otherHashes);
                if (similarity >= SIMILARITY_THRESHOLD) {
                    duplicates++;
                    if (!articleMapComplete.get(id).getJournal().equals(articleMapComplete.get(otherId).getJournal())) {
                        log.info(MessageFormat.format("Duplicate for articles {0} and {1}.", id, otherId));
                    }
                }
            }
        }
        log.info(MessageFormat.format("Found {0} duplicates.", duplicates));
    }


}
