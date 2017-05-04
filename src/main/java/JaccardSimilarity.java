import java.util.HashSet;
import java.util.Set;

public class JaccardSimilarity {

    public static float of(Set<String> setA, Set<String> setB) {
        Set<String> intersection = new HashSet<String>(setA);
        intersection.retainAll(setB);

        return (float) intersection.size() / (float) (setA.size() + setB.size() - intersection.size());
    }
}
