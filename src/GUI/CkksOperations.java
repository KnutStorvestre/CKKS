package GUI;

import CKKS.KeyGeneratorCKKS;
import CKKS.Parameters;
import keys.PublicKey;
import keys.SecretKey;
import modules.Complex;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;

public class CkksOperations {
    private static JFrame frame;

    private static BigInteger moduloBig;
    private static BigInteger moduloSmall;
    private static BigInteger scalingFactor;
    private static Parameters parameter;
    private static KeyGeneratorCKKS keyGenerator;
    private static SecretKey secretKey;
    private static PublicKey publicKey;
    private static PublicKey relinearizationKey;
    // These will not be removed under reset
    private static ArrayList<Vector> originalVectors;

    //private static ArrayList<>
    //TODO use stack here :)

    public CkksOperations(double[][] vectorsValues, int vectorsSize, int numVectors, Parameters parameters){
        BigInteger polynomialDegree = new BigInteger(String.valueOf(vectorsSize*2));
        int bitPrimeSize = 500;
        int millerRabinIterations = 30;

        BigInteger moduloBig = BigInteger.TWO.pow(2000);
        BigInteger moduloSmall = BigInteger.TWO.pow(1200);
        BigInteger scalingFactor = BigInteger.TWO.pow(40);

        MathContext mc = new MathContext(100);

        originalVectors = new ArrayList<>(numVectors);

        Vector tmpVector;
        for (int i = 0; i < numVectors; i++) {
            tmpVector = new Vector();
            tmpVector.setVector(doubleVectorValsToComplexArrayList(vectorsValues[i]));
            originalVectors.add(tmpVector);
        }

        frame = new JFrame();
        frame.setSize(1000, 520);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        JLabel instructions = new JLabel("Current operations:");
        instructions.setBounds(10,20,200,25);
        panel.add(instructions);

        JTextField currentOperations = new JTextField();
        currentOperations.setEditable(false);
        currentOperations.setBounds(130,20,500,25);
        panel.add(currentOperations);

        frame.setVisible(true);
    }

    private void createParametersKeys(BigInteger polynomialDeg, BigInteger moduloSmall, BigInteger moduloBig,
                                      BigInteger scalingFactor, int bitPrimeSize, int millerRabinIterations, MathContext mc){
        parameter = new Parameters(polynomialDeg, moduloBig, moduloSmall, scalingFactor, bitPrimeSize, millerRabinIterations, mc);
        keyGenerator = new KeyGeneratorCKKS(parameter);
        secretKey = keyGenerator.getSecretKey();
        publicKey = keyGenerator.getPublicKey();
        relinearizationKey = keyGenerator.getRelinearizationKey();
    }

    private ArrayList<Complex> doubleVectorValsToComplexArrayList(double[] vector){
        ArrayList<Complex> vectorComplex = new ArrayList<>(vector.length);
        for (int i = 0; i < vector.length-1; i+=2) {
            vectorComplex.add(new Complex(BigDecimal.valueOf(vector[i]), BigDecimal.valueOf(vector[i+1])));
        }
        return vectorComplex;
    }

    private void createVector(JPanel panel, int vectorIndex){
        //TODO button

    }

}
