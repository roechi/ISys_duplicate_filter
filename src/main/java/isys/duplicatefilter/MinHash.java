package isys.duplicatefilter;

import java.util.List;
import java.util.function.BinaryOperator;

public class MinHash {

    public static int of(List<String> tokens) {
        return tokens.stream()
                .map(String::hashCode)
                .reduce(BinaryOperator.minBy(Integer::compareTo))
                .orElse(0);
    }

    public static int of(List<String> tokens, int seed) {
        return tokens.stream()
                .map(String::hashCode)
                .map(h -> h ^ seed)
                .reduce(BinaryOperator.minBy(Integer::compareTo))
                .orElse(0);
    }
}
