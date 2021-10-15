package com.equationtranslator;

import android.graphics.Bitmap;

public class ModelInterface {

    static int val = 0;

    public static String processImage(Bitmap img) {
        int[][] matrix = new int[11][11];
        return EquationInterface.processMatrix(matrix);
    }
}
