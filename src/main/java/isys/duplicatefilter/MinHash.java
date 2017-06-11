package isys.duplicatefilter;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BinaryOperator;

public class MinHash {

    private final int numberOfHashFunctions;

    private HashGenerator[] hashFunctions;

    public MinHash(int numberOfHashFunctions) {
        this.numberOfHashFunctions = numberOfHashFunctions;
        // Generate all hash functions
        hashFunctions = new HashGenerator[numberOfHashFunctions];

        int universeSize = Integer.MAX_VALUE;
        Random r = new Random();
        for (int i = 0; i < numberOfHashFunctions; i++) {
            int a = r.nextInt(universeSize);
            int b = r.nextInt(universeSize);
            int c = r.nextInt(universeSize);
            hashFunctions[i] = new HashGenerator(a, b, c, universeSize);
        }
    }

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

    public List<Integer> hash(List<String> shingles) {

        List<Integer> hashValues = new ArrayList<>(numberOfHashFunctions);

        for (int i = 0; i < numberOfHashFunctions; i++) {
            hashValues.add(i, Integer.MAX_VALUE);
        }

        if (shingles == null || shingles.size() == 0) {
            return hashValues;
        }

        for (String shingle : shingles) {
            int hashCode = shingle.hashCode();
            for (int hashIndex = 0; hashIndex < numberOfHashFunctions; hashIndex++) {
                int hashValue = hashFunctions[hashIndex].calculateHash(hashCode);
                hashValues.set(hashIndex, Math.min(hashValues.get(hashIndex), hashValue));
            }
        }
        return hashValues;
    }

    private class HashGenerator {
        private int a;
        private int b;
        private int c;
        private int universeSize;

        HashGenerator(int a, int b, int c, int universeSize) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.universeSize = universeSize;
        }

        int calculateHash(int x) {
            x = x & universeSize;
            int hashValue = (a * (x >> 4) + b * x + c) & universeSize;
            return Math.abs(hashValue);
        }
    }

}
