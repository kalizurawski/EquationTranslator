
import com.equationtranslator.EquationInterface;

import static org.junit.Assert.*;
import org.junit.Test;

public class EquationInterfaceTest {
    private final EquationInterface ei = new EquationInterface();

    @Test
    public void oneLineEquation() {
        // 1+1=2
        int[][] testMatrix = new int[12][12];
        testMatrix[0][0] = 49; // 1
        testMatrix[1][0] = 43; // +
        testMatrix[2][0] = 49; // 1
        testMatrix[3][0] = 61; // =
        testMatrix[4][0] = 50; // 2

        String result = ei.convertMatrix(testMatrix);
        assertEquals("$$1+1=2$$", result);
    }

    @Test
    public void equationWithExponent() {
        // x^2=4
        int[][] testMatrix = new int[12][12];
        testMatrix[0][1] = 120; // x
        testMatrix[1][0] = 50;  // ^2
        testMatrix[2][1] = 61;  // =
        testMatrix[3][1] = 52;  // 4

        String result = ei.convertMatrix(testMatrix);
        assertEquals("$$x^2=4$$", result);
    }
}
