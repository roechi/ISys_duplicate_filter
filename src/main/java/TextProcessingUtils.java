import org.assertj.core.util.Strings;

import java.util.*;

import static java.util.Arrays.asList;

public final class TextProcessingUtils {

    public static List<String> getKShingle(String text, int k) {
        String[] words = (String[]) splitWords(text, " ").toArray();

        int numberOfShingles = words.length - k + 1;
        LinkedList<String> shingles = new LinkedList<String>();

        for (int i = 0; i < numberOfShingles; i++) {
            LinkedList<String> shingleSet = new LinkedList<String>();
            for (int j = 0; j < k; j++) {
                shingleSet.add(words[i + j]);
            }

            String shingle = shingleSet.stream().map(s -> s + " ").reduce(Strings::concat).orElse("");
            shingles.add(shingle.trim());
        }

        return shingles;
    }


    public static Collection<String> splitWords(String text, String delimiterPattern) {
        return asList(text.split(delimiterPattern));
    }
}
