import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class TextProcessingUtilsTest {

    @Test
    public void shouldGetOneShingle() throws Exception {
        String text = "these are some words";

        Collection<String> shingles = TextProcessingUtils.getKShingle(text, 4);

        assertThat(shingles.size()).isEqualTo(1);
        assertThat(shingles.toArray()[0]).isEqualTo(text);
    }

    @Test
    public void shouldGetTwoShingles() throws Exception {
        String text = "these are some words";

        Collection<String> shingles = TextProcessingUtils.getKShingle(text, 3);

        assertThat(shingles.size()).isEqualTo(2);
        assertThat(shingles.toArray()[0]).isEqualTo("these are some");
        assertThat(shingles.toArray()[1]).isEqualTo("are some words");
    }

    @Test
    public void shouldSplitAtSpace() throws Exception {
        String text = "these are some words";

         Collection<String> words = TextProcessingUtils.splitWords(text, " ");

         assertThat(words.size()).isEqualTo(4);
         assertThat(words).isEqualTo(asList("these", "are", "some", "words"));
    }

    @Test
    public void shouldSplitAtDelimiter() throws Exception {
        String text = "these, are, some, words";

        Collection<String> words = TextProcessingUtils.splitWords(text, ", ");

        assertThat(words.size()).isEqualTo(4);
        assertThat(words).isEqualTo(asList("these", "are", "some", "words"));
    }

    @Test
    public void shouldGetCorrect5Shingles() throws Exception {
        String text = "The quick brown fox jumps over the lazy dog";
        List<String> expectedShingles = asList(
                "The quick brown fox jumps",
                "quick brown fox jumps over",
                "brown fox jumps over the",
                "fox jumps over the lazy",
                "jumps over the lazy dog"
        );

        List<String> shingles = TextProcessingUtils.getKShingle(text, 5);

        assertThat(shingles.size()).isEqualTo(5);
        assertThat(shingles).isEqualTo(expectedShingles);
    }
}