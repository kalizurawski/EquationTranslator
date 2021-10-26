package com.equationtranslator;

import android.graphics.Bitmap;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

public class ModelInterface {

    // python interpreter
    private Python py;
    private PyObject module_convertImg;

    // interfaces
    private EquationInterface ei;

    static int val = 0;

    public ModelInterface() {
        py = Python.getInstance();
        module_convertImg = py.getModule("convertImg");
        ei = new EquationInterface();
    }

    public String processImage(Bitmap img) {
        // convert bitmap to csv for ML model
        String csvFile = convertBmpToCsv(img);

        // TODO: run model on csv

        // interpret as matrix
        int[][] matrix = new int[11][11];

        // output equation
        return ei.processMatrix(matrix);
    }

    private String convertBmpToCsv(Bitmap img) {
        String filename;
        filename = module_convertImg.callAttr("convertImg", img).toString();
        return filename;
    }
}
