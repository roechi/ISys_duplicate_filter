import java.util.List;
import java.util.function.BinaryOperator;

public class MinHash {

    public static int of(List<String> tokens) {
        Integer min = tokens.stream()
                .map(String::hashCode)
                .reduce(BinaryOperator.minBy(Integer::compareTo))
                .orElse(0);
        return min;
    }

}
