package isys.duplicatefilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import isys.duplicatefilter.dto.ArticleList;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class MinHashTest {

    @Test
    public void shouldGetMinHash() throws Exception {
        String text = "hic est scribet minimus hashus textus";
        List<String> shingles = TextProcessingUtils.getKShingles(text, 3);

        int minHash = MinHash.of(shingles);

        assertThat(minHash).isEqualTo(279080452);
    }

    @Test
    public void shouldWorkOnSingleItem() throws Exception {
        assertThat(MinHash.of(singletonList("foobar"))).isNotEqualTo(0);
    }

    @Test
    public void shouldReturnZeroForEmptyList() throws Exception {
        assertThat(MinHash.of(new LinkedList<>())).isEqualTo(0);
    }

    @Test
    public void shouldGetMinHashWithSeed() throws Exception {
        String text = "hic est scribet minimus hashus textus";
        List<String> shingles = TextProcessingUtils.getKShingles(text, 3);

        int minHash = MinHash.of(shingles, 184731932);

        assertThat(minHash).isEqualTo(463513368);
    }

    @Test
    public void shouldWorkOnSingleItemWithSeed() throws Exception {
        assertThat(MinHash.of(singletonList("foobar"), 47189321)).isNotEqualTo(0);
    }

    @Test
    public void shouldReturnZeroForEmptyListWithSeed() throws Exception {
        assertThat(MinHash.of(new LinkedList<>(), 31472891)).isEqualTo(0);
    }

    @Test
    public void testRunner() throws IOException, URISyntaxException {
        byte[] file = Files.readAllBytes(Paths.get(MinHash.class.getClassLoader().getResource(
                "fakeData/example.json").toURI()));
        final ObjectMapper mapper = new ObjectMapper();
        ArticleList articlesList = mapper.readValue(file, ArticleList.class);
        Random r = new Random(199);
        Map<String, List<String>> shinglesOfArticles = new HashMap<>();
        Map<String, List<Integer>> minHashOfArticles = new HashMap<>();

        articlesList.forEach(article -> {
            String content = article.getContent();
            if (content == null) {
                content = article.getTitle();
            }
            shinglesOfArticles.put(article.getId(), TextProcessingUtils.getKShingles(content, 3));
        });


        System.out.println("==================Shingles generated");


        MinHash minHash = new MinHash(199);

        shinglesOfArticles.forEach((key, shingles) -> {
            minHashOfArticles.put(key, minHash.hash(shingles));
        });
        System.out.println("==================hashes generated");


        List<Map.Entry<String, List<Integer>>> minHashes = Lists.newArrayList(minHashOfArticles.entrySet());

        int doublesCount = 0;

        for (int i = 0; i < minHashOfArticles.size(); i++) {
            for (int j = i + 1; j < minHashOfArticles.size(); j++) {
                Map.Entry<String, List<Integer>> iHash = minHashes.get(i);
                Map.Entry<String, List<Integer>> jHash = minHashes.get(j);

                float similiarity = JaccardSimilarity.ofInt(iHash.getValue(), jHash.getValue());
                if (similiarity > 0.5) {
                    System.out.println(iHash.getKey() + " is similar to " + jHash.getKey() + " with a similarity of " + similiarity);
                    doublesCount++;
                }
            }
        }
        System.out.println("==================Jaccard generated");
        System.out.println(doublesCount + " duplicates");


    }

    @Test
    public void testLocalSensityHashing() throws IOException, URISyntaxException {
        byte[] file = Files.readAllBytes(Paths.get(MinHash.class.getClassLoader().getResource(
                "fakeData/example.json").toURI()));
        final ObjectMapper mapper = new ObjectMapper();
        ArticleList articlesList = mapper.readValue(file, ArticleList.class);
        Random r = new Random(199);
        Map<String, List<String>> shinglesOfArticles = new HashMap<>();
        Map<String, List<Integer>> minHashOfArticles = new HashMap<>();

        articlesList.forEach(article -> {
            String content = article.getContent();
            if (content == null) {
                content = article.getTitle();
            }
            shinglesOfArticles.put(article.getId(), TextProcessingUtils.getKShingles(content, 3));
        });


        System.out.println("==================Shingles generated");


        MinHash minHash = new MinHash(199);

        shinglesOfArticles.forEach((key, shingles) -> {
            minHashOfArticles.put(key, minHash.hash(shingles));
        });
        System.out.println("==================hashes generated");

        LocalSensitiveHashing sensitiveHashing = new LocalSensitiveHashing(67, new LinkedHashMap<>(minHashOfArticles));
        Set<String> keys = sensitiveHashing.compareBands();
        System.out.println(keys.size() + " articles to compare after LSH");
    }

}