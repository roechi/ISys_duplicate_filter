package isys.duplicatefilter;

import java.util.*;

public class LocalSensitiveHashing {

    private List<String> keys;

    private List<List<Integer[]>> minHashBands = new ArrayList<>();

    public LocalSensitiveHashing(int numberOfBands, LinkedHashMap<String, List<Integer>> matrix) {
        keys = new ArrayList<>(matrix.keySet());
        for (int i = 0; i < numberOfBands; i++) {
            List<Integer[]> bands = new ArrayList<>();
            for (List<Integer> hashes : matrix.values()) {
                Integer[] band = new Integer[3];
                int bandIndex = 0;
                for (int index = i * 3; index < i * 3 + 3 && index < hashes.size(); index++, bandIndex++) {
                    Integer hash = hashes.get(index);
                    band[bandIndex] = hash;
                }
                bands.add(band);
            }
            minHashBands.add(bands);
        }
    }


    public Set<String> compareBands() {
        Set<String> keysToCompare = new HashSet<>();
        minHashBands.forEach(hashBand -> {
            for (int i = 0; i < hashBand.size(); i++) {
                for (int j = i + 1; j < hashBand.size(); j++) {
                    Integer[] iHashes = hashBand.get(i);
                    Integer[] jHashes = hashBand.get(j);
                    boolean isEqual = Arrays.equals(iHashes, jHashes);
                    if (isEqual) {
                        keysToCompare.add(keys.get(i));
                        keysToCompare.add(keys.get(j));
                    }
                }
            }
        });
        return keysToCompare;
    }
}
