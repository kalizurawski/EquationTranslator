package com.equationtranslator;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

//import com.example.equationtranslator.ml.DyBasicModel;
import com.example.equationtranslator.ml.QuantInt16x8ClusteredModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ModelInterface {

    // context
    public Context context;

    // model
    private @NonNull QuantInt16x8ClusteredModel model;

    // image info
    private static final int IMAGE_HEIGHT = 28;
    private static final int IMAGE_WIDTH = 28;

    private String result;
    private String percent;

    public ModelInterface(Context context) {
        this.context = context;
    }

    public String processImage(Bitmap img) {
        // resize, convert to grayscale, normalize
        ByteBuffer fin = ImageManipulation.preProcess(img);

        Thread t = new Thread(() -> {
            try {
                // inits model
                initModel();

                // creates inputs for reference
                TensorBuffer inputFeature = TensorBuffer.createFixedSize(new int[] {1, IMAGE_HEIGHT, IMAGE_WIDTH, 1}, DataType.FLOAT32);
                inputFeature.loadBuffer(fin);

                // runs model
                QuantInt16x8ClusteredModel.Outputs outputs = model.process(inputFeature);
                TensorBuffer outputFeature = outputs.getOutputFeature0AsTensorBuffer();

                System.out.println("Output Feature: " + outputFeature.getDataType().toString());
                System.out.println(Arrays.toString(outputFeature.getFloatArray()));

                // interprets output
                interpretOutput(outputFeature);

                System.out.println("Result from model: " + result);

                // releases model
                model.close();

            } catch (final Exception e) {
                throw new RuntimeException("Error using TFLite model!", e);
            }
        });

        t.start(); // launch thread

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // output string
        return "Prediction: " + result + " with " + percent + "% assurance.";
    }

    private void interpretOutput(TensorBuffer output) {
        int idx = getMaxIndex(output.getFloatArray());
        result = mapToChar(idx);
    }

    private String mapToChar(int val) {
        if (val < 10) {
            return String.valueOf(val); // 0 thru 9
        } else if (val < 36) {
            val += 55;
            return Character.toString((char) val); // A thru Z
        }

        switch(val) {
            case 36: return Character.toString((char) 97);  // a
            case 37: return Character.toString((char) 98);  // b
            case 38: return Character.toString((char) 100); // d
            case 39: return Character.toString((char) 101); // e
            case 40: return Character.toString((char) 102); // f
            case 41: return Character.toString((char) 103); // g
            case 42: return Character.toString((char) 104); // h
            case 43: return Character.toString((char) 110); // n
            case 44: return Character.toString((char) 113); // q
            case 45: return Character.toString((char) 114); // r
            case 46: return Character.toString((char) 116); // t
            case 47: return Character.toString((char) 45);
            case 48: return Character.toString((char) 33);
            case 49: return Character.toString((char) 40);
            case 50: return Character.toString((char) 41);
            case 51: return Character.toString((char) 91);
            case 52: return Character.toString((char) 93);
            case 53: return Character.toString((char) 123);
            case 54: return Character.toString((char) 125);
            case 55: return Character.toString((char) 43);
            case 56: return Character.toString((char) 61);
            case 57: return "$$\\alpha$$";
            case 58: return "$$\\beta$$";
            case 59: return "$$\\delta$$";
            case 60: return "$$\\gamma$$";
            case 61: return "$$\\geq$$";
            case 62: return Character.toString((char) 62);
            case 63: return "$$\\infty$$";
            case 64: return "$$\\int_{}^{}$$";
            case 65: return "$$\\lambda$$";
            case 66: return "$$\\leq$$";
            case 67: return Character.toString((char) 60);
            case 68: return "$$\\mu$$";
            case 69: return "$$\\neq$$";
            case 70: return "$$\\phi$$";
            case 71: return "$$\\pi$$";
            case 72: return "$$\\pm$$";
            case 73: return "$$\\sigma$$";
            case 74: return "$$\\sqrt{}$$";
            case 75: return "$$\\Sigma$$";
            case 76: return "$$\\theta$$";
            default: return "Unexpected result received";
        }
    }

    private int getMaxIndex(float[] arr) {
        float currMax = 0;
        int idx = -1;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > currMax) {
                currMax = arr[i];
                percent = String.format("%.2f", currMax * 100);
                idx = i;
            }
        }

        return idx;
    }

    private void initModel() {
        model = null;
        try {
            model = QuantInt16x8ClusteredModel.newInstance(context);
        } catch (final Exception e) {
            throw new RuntimeException("Error initializing TensorFlow!", e);
        }
    }
//
//    private String convertBmpToCsv(Bitmap img) {
//        String filename;
//        filename = module_convertImg.callAttr("convertImg", img).toString();
//        return filename;
//    }
}
