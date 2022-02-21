// 
// Decompiled by Procyon v0.5.36
// 

package com.chainz.core.async.adrewards;

import java.security.SecureRandom;
import java.util.Random;

class Strutils {
    private static Random RANDOM = new SecureRandom();
    private static char[] CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static String getRandomString() {
        return generateNumbers(5, Strutils.CHARS.length);
    }

    private static String generateNumbers(int length, int maxIndex) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            sb.append(Strutils.CHARS[Strutils.RANDOM.nextInt(maxIndex)]);
        }
        return sb.toString();
    }
}
