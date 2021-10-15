package com.equationtranslator;

import java.util.Random;

public class EquationInterface {

    private static Random rand = new Random();

    public static String processMatrix(int[][]matrix) {
        return String.valueOf(rand.nextInt(100));
    }
}
