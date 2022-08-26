package GUI;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainGUI {

    public static void main(String[] args) {
        //VectorSizeNumGUI a = new VectorSizeNumGUI();
        // returns two ints

        //FillInVectors b = new FillInVectors(2,2);

        double[][] testVectors = new double[2][4];
        testVectors[0][0] = 1;
        testVectors[0][1] = 1;
        testVectors[1][0] = 1;
        testVectors[1][1] = 1;

        ParametersInput a = new ParametersInput(2,2,testVectors);
        // encoding encryption evaluation


        // TODO add big math to maven

    }

}
