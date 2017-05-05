import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

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
}