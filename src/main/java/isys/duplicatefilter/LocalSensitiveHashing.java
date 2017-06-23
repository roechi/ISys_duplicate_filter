package isys.duplicatefilter;

import com.google.common.collect.Lists;
import org.assertj.core.util.Sets;

import java.util.*;
import java.util.stream.Collectors;

public class LocalSensitiveHashing {

    private List<String> ids;

    private List<List<Integer[]>> minHashBands = new ArrayList<>();

    private Map<IntegerArrayKey, Set<String>> buckets = new HashMap<>();

    public LocalSensitiveHashing(int numberOfBands, int bucketSize, LinkedHashMap<String, List<Integer>> matrix) {
        ids = new ArrayList<>(matrix.keySet());
        for (int i = 0; i < numberOfBands; i++) {
            List<Integer[]> bands = new ArrayList<>();
            for (List<Integer> hashes : matrix.values()) {
                Integer[] band = new Integer[bucketSize];
                int bandIndex = 0;
                for (int index = i * bucketSize; index < i * bucketSize + bucketSize && index < hashes.size(); index++, bandIndex++) {
                    Integer hash = hashes.get(index);
                    band[bandIndex] = hash;
                }
                bands.add(band);
            }
            minHashBands.add(bands);
        }
    }


    public List<ArrayList<String>> compareBands() {
        Set<String> keysToCompare;
        minHashBands.forEach(hashBand -> {
            for (int i = 0; i < hashBand.size(); i++) {
                IntegerArrayKey bandNumber = new IntegerArrayKey(hashBand.get(i));
                Set<String> idsInBucket = buckets.get(bandNumber);
                if (idsInBucket == null) {
                    buckets.put(bandNumber, Sets.newLinkedHashSet(ids.get(i)));
                } else {
                    idsInBucket.add(ids.get(i));
                }
            }
        });
        return buckets.values().stream()
                .filter(set -> set.size() > 1)
                .map(Lists::newArrayList)
                .map(list -> {
                    list.sort(Comparator.naturalOrder());
                    return list;
                })
                .collect(Collectors.toList());
    }

    class IntegerArrayKey {
        private Integer[] key;

        public IntegerArrayKey(Integer[] key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IntegerArrayKey that = (IntegerArrayKey) o;
            return Arrays.equals(key, that.key);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(key);
        }
    }
}
