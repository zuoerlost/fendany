package com.fendany.utils.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class RandomUtils {

    private final static ThreadLocal<SecureRandom> currentRandom = new ThreadLocal<SecureRandom>() {
        @Override
        protected SecureRandom initialValue() {
            try {
                SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
                sr.setSeed(System.currentTimeMillis());
                return sr;
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
        }
    };

    private RandomUtils() {}

    public static int nextInt(int max) {
        return currentRandom.get().nextInt(max);
    }

    public static long nextLong() {
        return currentRandom.get().nextLong();
    }

}
