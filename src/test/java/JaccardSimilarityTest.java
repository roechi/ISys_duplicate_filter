import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class JaccardSimilarityTest {

    @Test
    public void shouldBeIdentical() throws Exception {
        HashSet<String> set = new HashSet<String>(Collections.singletonList("foo"));

        float similarity = JaccardSimilarity.of(set, set);

        assertThat(similarity).isEqualTo(1f);
    }

    @Test
    public void shouldBeDisjunct() throws Exception {
        HashSet<String> setA = new HashSet<String>(Collections.singletonList("foo"));
        HashSet<String> setB = new HashSet<String>(Collections.singletonList("bar"));

        float similarity = JaccardSimilarity.of(setA, setB);

        assertThat(similarity).isEqualTo(0f);
    }

    @Test
    public void shouldBeIdenticalWithDifferentOrder() throws Exception {
        HashSet<String> setA = new HashSet<String>(asList("foo", "bar"));
        HashSet<String> setB = new HashSet<String>(asList("bar", "foo"));

        float similarity = JaccardSimilarity.of(setA, setB);

        assertThat(similarity).isEqualTo(1f);
    }

    @Test
    public void shouldBeSixtyPercentIntersection() throws Exception {
        HashSet<String> setA = new HashSet<String>(asList("chair", "desk", "rug", "keyboard", "mouse"));
        HashSet<String> setB = new HashSet<String>(asList("chair", "rug", "keyboard"));

        float similarity = JaccardSimilarity.of(setA, setB);

        assertThat(similarity).isEqualTo(0.6f);
    }
}