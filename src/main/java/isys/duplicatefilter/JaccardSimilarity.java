package isys.duplicatefilter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JaccardSimilarity {

    public static float of(Set<String> setA, Set<String> setB) {
        Set<String> intersection = new HashSet<String>(setA);
        intersection.retainAll(setB);

        float numerator = intersection.size();
        float denominator = (setA.size() + setB.size() - intersection.size());

        return numerator / denominator;
    }

    public static float ofInt(List<Integer> listA, List<Integer> listB) {
        List<Integer> intersection = new ArrayList<>(listA);
        intersection.retainAll(listB);

        float numerator = intersection.size();
        float denominator = (listA.size() + listB.size() - intersection.size());

        return numerator / denominator;
    }
}
