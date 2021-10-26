package com.equationtranslator;

import java.util.Random;

public class EquationInterface {

    private static final Random rand = new Random();
    private static final int MAX_ROWS = 12;
    private static final int MAX_COLS = 12;
    private boolean[][] checked = new boolean[MAX_COLS][MAX_ROWS];

    public String processMatrix(int[][]matrix) {
        return buildEquation();
    }

    public String convertMatrix(int[][] matrix) {

        initChecked();

        StringBuilder finalEq = new StringBuilder("$$");
        for(int c = 0; c < MAX_COLS; c++) {
            for(int r = 0; r < MAX_ROWS; r++) {
                if (!checked[c][r]) {
                    if (matrix[c][r] != 0) { // found value
                        finalEq.append((char) matrix[c][r]);

                        int exponent = hasExponent(matrix, c, r);
                        if (exponent != 0) { // if value has an exponent
                            finalEq.append('^');
                            finalEq.append((char) exponent);
                            checked[c+1][r-1] = true;
                        }
                    }
                    checked[c][r] = true;
                }
            }
        }
        finalEq.append("$$");
        return finalEq.toString();
    }

    private int hasExponent(int[][]matrix, int col, int row) {
        // check not first row or last col
        if (row == 0 || col == MAX_COLS - 1)
            return 0;

        // check there is a space to the right
        if (matrix[col+1][row] != 0)
            return 0;

        // check if there is a value above
        if (matrix[col+1][row-1] != 0) {
            return matrix[col+1][row-1];
        } else {
            return 0;
        }
    }

    private String buildEquation() {
      switch(rand.nextInt(10)) {
            case 0:
                return "$$x = \\frac{-b \\pm \\sqrt{b^2-4ac}}{2a}$$";
            case 1:
                return "$$1 + 1 = 2$$";
            case 2:
                return "$$x \\pm 5 = 7 + y$$";
            case 3:
                return "$$\\frac{(x+1)^2}{(x+2)^2}=0$$";
            case 4:
                return "$$\\sqrt{-1} = i$$";
            case 5:
                return "$$a^2+b^2=c^2$$";
            case 6:
                return "$$A=lw$$";
            case 7:
                return "$$\\frac{cos(x)}{sin(x)}=tan(x)$$";
            case 8:
                return "$$y = mx + b$$";
            case 9:
                return "$$|-1| = 1$$";
            default:
                return "Error";
        }
    }

    private void initChecked() {
        for(int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLS; j++) {
                checked[i][j] = false;
            }
        }
    }
}
