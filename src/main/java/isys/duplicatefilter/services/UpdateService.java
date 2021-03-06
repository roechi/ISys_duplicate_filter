package isys.duplicatefilter.services;

import isys.duplicatefilter.*;
import isys.duplicatefilter.dto.Article;
import isys.duplicatefilter.dto.FilteredArticle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateService {

    private final static double SIMILARITY_THRESHOLD = 1.0;

    private final DataClient client;
    private final RawArticleService rawArticleService;
    private final FilteredArticleService filteredArticleService;

    private static FilteredArticle toFilteredArticle(Article article) {
        return (FilteredArticle) new FilteredArticle()
                .setAddress(article.getAddress())
                .setCategory(article.getCategory())
                .setContent(article.getContent())
                .setDate(article.getDate())
                .setDistrict(article.getDistrict())
                .setId(article.getId())
                .setJournal(article.getJournal())
                .setMessageTimestamp(article.getMessageTimestamp())
                .setPrecinct(article.getPrecinct())
                .setReferenceReports(article.getReferenceReports())
                .setReliefForces(article.getReliefForces())
                .setTitle(article.getTitle())
                .setUrl(article.getUrl());
    }

    @Scheduled(fixedDelay = 1000 * 60 * 60)
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
                .forEach(rawArticleService::save);

        log.info("Articles fetched and saved.");
        filter();
    }

    protected void filter() {
        log.info("Filtering.");
        List<Article> allArticles = rawArticleService.findAll();

        HashMap<String, FilteredArticle> articleMapComplete = new HashMap<>();
        HashMap<String, List<String>> articleMap = new HashMap<>();

        allArticles
                .stream()
                .filter(article -> !article.getContent().isEmpty())
                .forEach(article -> {
                            articleMap
                                    .put(article.getId(), TextProcessingUtils.getKShingles(article.getContent(), 3));
                            articleMapComplete.put(article.getId(), toFilteredArticle(article));
                        }
                );

        log.info("Loaded and shingled all articles.");
        MinHash minHash = new MinHash(198);

        LinkedHashMap<String, List<Integer>> minHashMap = new LinkedHashMap<>();

        articleMap.forEach((id, shingles) -> minHashMap.put(id, minHash.hash(shingles)));

        log.info("Hashed articles.");

        LocalSensitiveHashing localSensitiveHashing = new LocalSensitiveHashing(66, 3, minHashMap);
        List<ArrayList<String>> buckets = localSensitiveHashing.compareBands();

        int bucketSize = buckets.size();
        Set<String> strings = new HashSet<>();
        buckets.forEach(strings::addAll);
        int articleToCompare = strings.size();
        log.info("LSH done. Buckets: " + bucketSize + " Article to compare: " + articleToCompare);
        int duplicates = 0;
        int compares = 0;
        int completedBuckets = 0;
        double oldPercentage = 0;
        StringBuilder sb = new StringBuilder();
        Set<String> alreadyCompared = new HashSet<>();
        for (ArrayList<String> keySet : buckets) {
            for (int i = 0; i < keySet.size(); i++) {
                String id = keySet.get(i);
                List<Integer> hashes = minHashMap.get(id);
                for (int j = i + 1; j < keySet.size(); j++) {
                    String otherId = keySet.get(j);
                    List<Integer> otherHashes = minHashMap.get(otherId);
                    sb.append(id);
                    sb.append(otherId);
                    if (!alreadyCompared.contains(sb.toString())) {
                        compares++;
                        float similarity = JaccardSimilarity.ofInt(hashes, otherHashes);
                        if (similarity >= SIMILARITY_THRESHOLD) {
                            FilteredArticle articleWithDuplicate = articleMapComplete.get(id);
                            articleWithDuplicate.getDuplicateIds().add(otherId);
                            FilteredArticle otherArticleWithDuplicate = articleMapComplete.get(otherId);
                            otherArticleWithDuplicate.getDuplicateIds().add(id);
                            duplicates++;
                            if (!articleWithDuplicate.getJournal().equals(articleMapComplete.get(otherId).getJournal())) {
                                log.info(MessageFormat.format("Duplicate for articles {0} and {1}.", id, otherId));
                            }

                        }
                        alreadyCompared.add(sb.toString());
                    }
                    sb.setLength(0);
                }
            }
            completedBuckets++;

            double percentage = ((double) completedBuckets) / ((double) bucketSize) * 100;

            if (percentage - oldPercentage > 5) {
                log.info(MessageFormat.format("Completed {0} out of {1} buckets. {2}%", completedBuckets, bucketSize, percentage));
                oldPercentage = percentage;
            }
        }
        log.info(MessageFormat.format("Found {0} duplicates in {1} comparisons.", duplicates, compares));
        log.info("Saving filtered articles...");
        articleMapComplete.values().forEach(filteredArticleService::save);
        log.info("Saved filtered articles.");
    }

}
