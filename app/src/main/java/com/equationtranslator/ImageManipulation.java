package com.equationtranslator;

import static android.graphics.Bitmap.createScaledBitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import androidx.core.content.res.TypedArrayUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collections;

public class ImageManipulation {
    // image info
    private static final int IMAGE_HEIGHT = 28;
    private static final int IMAGE_WIDTH = 28;

    public static ByteBuffer preProcess(Bitmap bmp) {
        Bitmap grayBmp = grayscale(createScaledBitmap(bmp, IMAGE_WIDTH, IMAGE_HEIGHT, false));
        return convertBitmapToByteBuffer(grayBmp);
    }

    // code modified from: https://stackoverflow.com/questions/62371665/i-need-to-normalize-a-bitmap-and-store-it-in-a-tensorimage-is-there-any-way-to
    private static ByteBuffer convertBitmapToByteBuffer(Bitmap bmp) {
        ByteBuffer imgData = ByteBuffer.allocateDirect(Float.BYTES*IMAGE_HEIGHT*IMAGE_WIDTH);
        imgData.order(ByteOrder.nativeOrder());
        int [] pixels = new int[IMAGE_WIDTH*IMAGE_HEIGHT];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        float [] arr = normalize(pixels);
        int i = 0;
        // Convert the image to floating point.
        for (float pixel : arr) {
            imgData.putFloat(pixel);
        }

        return imgData;
    }

    // adapted from: http://www.java2s.com/example/java-utility-method/array-normalize/normalize-int-values-0b80e.html
    private static float[] normalize(int[] pixels) {
        float[] normalized = new float[pixels.length];

        // get min and max
        int min = Arrays.stream(pixels).min().getAsInt();
        int max = Arrays.stream(pixels).max().getAsInt();
        int spread = max - min;

        if (spread == 0) {
            Arrays.fill(normalized, 1f);
            return normalized;
        }

        // normalize each value
        for (int i = 0; i < pixels.length; i++) {
            normalized[i] = (pixels[i] - min) / (float) spread;
        }
        return normalized;
    }

    private static Bitmap grayscale(Bitmap src)
    {
        int height = src.getHeight();
        int width = src.getWidth();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        ColorMatrix matrixGrayscale = new ColorMatrix();
        matrixGrayscale.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrixGrayscale);
        paint.setColorFilter(filter);

        canvas.drawBitmap(src, 0, 0, paint);
        return bitmap;
    }
}
