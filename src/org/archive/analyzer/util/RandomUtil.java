/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.archive.analyzer.util;

import java.util.Random;

/**
 *
 * @author praso
 */
public class RandomUtil {

    public static int generateRandomInt(int aStart, int aEnd, Random aRandom) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long) aEnd - (long) aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * aRandom.nextDouble());
        int randomNumber = (int) (fraction + aStart);
        return randomNumber;
    }
    
    public static void waitForAWhile(int iterations) {
        for (int i = 0; i < iterations; i++) {
            // do nothing
        }
    }
}
