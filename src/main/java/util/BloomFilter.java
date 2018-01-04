package util;

import java.util.BitSet;
import java.util.Random;


/**
 * default bit size is 1<<31 - 1
 * default num of hashes is 6
 *
 * Reference: https://gist.github.com/nanchenzi/3951177
 */
public class BloomFilter {
    private int bitSize = 1<<31 - 1;
    private int[] hashSeeds = {24, 35, 56, 85, 200, 10};
    private BitSet bitSet = new BitSet(bitSize);

    private SimpleHash[] hashes = new SimpleHash[hashSeeds.length];

    public BloomFilter() {

        for(int i=0; i<this.hashes.length; i++) {
            this.hashes[i] = new SimpleHash(this.bitSize, this.hashSeeds[i]);
        }

        System.out.println("Default   " + this.bitSet.size());
    }

    /**
     * custom bitSize and num of hashes
     * @param bitSize size of filter
     * @param hashNum the number of hash function
     */
    public BloomFilter(int bitSize, int hashNum) {
        this.bitSize = bitSize;
        this.bitSet = new BitSet(bitSize);
        System.gc();
        this.hashSeeds = new int[hashNum];
        this.hashes = new SimpleHash[hashNum];
        for(int i=0; i<hashNum; i++) {
            Random random = new Random();
            this.hashSeeds[i] = random.nextInt();
            this.hashes[i] = new SimpleHash(this.bitSize, this.hashSeeds[i]);
        }

        System.out.println("Custom   " + this.bitSet.size());
    }

    /**
     *  a simple hash function for BloomFilter
     */
    public static class SimpleHash {

        private int cap;
        private int seed;

        SimpleHash(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }

        int hash(String value) {
            int result = 1 << 24 -1;
            int len = value.length();
            for (int i=0; i<len; i++) {
                result += (result ^ value.charAt(i)) * this.seed;
            }
            return (cap - 1) & result;
        }
    }


    public void add(String url) {
        for(SimpleHash hashFun: this.hashes) {
            this.bitSet.set(hashFun.hash(url), true);
        }
    }

    public boolean contains(String url) {
        if (url == null){
            return false;
        }

        boolean result = true;
        for(SimpleHash hashFun: this.hashes) {
            result = result && bitSet.get(hashFun.hash(url));
        }

        return  result;
    }
}
