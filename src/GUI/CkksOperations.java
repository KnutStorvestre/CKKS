package GUI;

import CKKS.KeyGeneratorCKKS;
import CKKS.Parameters;
import keys.PublicKey;
import keys.SecretKey;
import modules.Complex;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

public class CkksOperations {
    private static int vectorLength;
    private static int totVectors;
    private static Parameters params;
    private static MathContext mc;
    private static KeyGeneratorCKKS keyGenerator;
    private static SecretKey secretKey;
    private static PublicKey publicKey;
    private static PublicKey relinearizationKey;
    // These will not be removed under reset
    private static ArrayList<Vector> originalVectors;
    private static JFrame frame;
    private static JPanel panel;
    private static JLabel infoLabel;
    private static JLabel infoMsgLabel;
    private static JLabel instructions ;
    private static JTextField currentOperations;
    private static JLabel currentResult;
    private static JButton addButton;
    private static JButton minusButton;
    private static JButton multiplicationButton;
    private static JButton divisionButton;
    private static JButton evaluationButton;
    private static JButton genKeyButton;
    private static JButton showSecretKeyButton;
    private static JButton showPublicKeyButton;
    private static JButton showRelinearizationKeyButton;


    //private static ArrayList<>
    //TODO use stack here :)

    public CkksOperations(double[][] vectorsValues, int vectorsSize, int numVectors, Parameters parameters, MathContext mathContext){
        vectorLength = vectorsSize;
        totVectors = numVectors;
        params = parameters;
        mc = mathContext;

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

        panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        infoLabel = new JLabel("info:");
        infoLabel.setBounds(10,20,200,25);
        panel.add(infoLabel);

        infoMsgLabel = new JLabel();
        infoMsgLabel.setBounds(45,20,500,25);
        panel.add(infoMsgLabel);

        instructions = new JLabel("Current operations:");
        instructions.setBounds(10,40,200,25);
        panel.add(instructions);

        currentOperations = new JTextField();
        currentOperations.setEditable(false);
        currentOperations.setBounds(130,40,200,25);
        panel.add(currentOperations);

        int nextResult = 0;
        currentResult = new JLabel("=R0");
        currentResult.setBounds(330,40,30,25);
        panel.add(currentResult);

        addButton = createAddButton();
        addButton.setBounds(360,40,30,25);
        panel.add(addButton);

        minusButton = createMinusButton();
        minusButton.setBounds(390,40,30,25);
        panel.add(minusButton);

        multiplicationButton = createMultiplicationButton();
        multiplicationButton.setBounds(420,40,30,25);
        panel.add(multiplicationButton);

        divisionButton = createDivisionButton();
        divisionButton.setBounds(450,40,30,25);
        panel.add(divisionButton);

        evaluationButton = createEvaluationButton();
        evaluationButton.setBounds(500,40,100,25);
        panel.add(evaluationButton);

        genKeyButton = createGenKeyButton();
        genKeyButton.setBounds(5,70,150,25);
        panel.add(genKeyButton);

        frame.setVisible(true);
    }

    private JButton createGenKeyButton() {
        return new JButton(new AbstractAction("Generate keys") {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoMsgLabel.setText("Keys created");

                keyGenerator = new KeyGeneratorCKKS(params);
                secretKey = keyGenerator.getSecretKey();
                publicKey = keyGenerator.getPublicKey();
                relinearizationKey = keyGenerator.getRelinearizationKey();

                showSecretKeyButton = createShowSecretKeyButton();
                showSecretKeyButton.setBounds(5,70,180,25);
                panel.add(showSecretKeyButton);

                showPublicKeyButton = createShowPublicKeyButton();
                showPublicKeyButton.setBounds(190,70,180,25);
                panel.add(showPublicKeyButton);

                showRelinearizationKeyButton = createShowRelinearizationKeyButton();
                showRelinearizationKeyButton.setBounds(375,70,180,25);
                panel.add(showRelinearizationKeyButton);

                panel.remove(genKeyButton);
                panel.repaint();
            }
        });
    }

    private void createVector(JPanel panel, int vectorIndex){

    }

    private JButton createShowSecretKeyButton() {
        return new JButton(new AbstractAction("Show secret key") {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoMsgLabel.setText("Secret key printed in terminal");
                System.out.println(secretKey);
            }
        });
    }

    private JButton createShowPublicKeyButton() {
        return new JButton(new AbstractAction("Show public key") {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoMsgLabel.setText("Public key printed in terminal");
                System.out.println(publicKey);
            }
        });
    }

    private JButton createShowRelinearizationKeyButton() {
        return new JButton(new AbstractAction("Show relinearization key") {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoMsgLabel.setText("Relinearization key printed in terminal");
                System.out.println(relinearizationKey);
            }
        });
    }

    //TODO maybe create class for all buttons
    private JButton createEvaluationButton() {
        return new JButton(new AbstractAction("Evaluate") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pressed");
            }
        });
    }
    private JButton createDivisionButton() {
        return new JButton(new AbstractAction("/") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pressed");
            }
        });
    }

    private JButton createMultiplicationButton() {
        return new JButton(new AbstractAction("x") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pressed");
            }
        });
    }

    private JButton createMinusButton() {
        return new JButton(new AbstractAction("-") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pressed");
            }
        });
    }

    private JButton createAddButton(){
        return new JButton(new AbstractAction("+") {
            @Override
            public void actionPerformed(ActionEvent e ) {
                System.out.println("Pressed");
            }
        });
    }

    private ArrayList<Complex> doubleVectorValsToComplexArrayList(double[] vector){
        ArrayList<Complex> vectorComplex = new ArrayList<>(vector.length);
        for (int i = 0; i < vector.length-1; i+=2) {
            vectorComplex.add(new Complex(BigDecimal.valueOf(vector[i]), BigDecimal.valueOf(vector[i+1])));
        }
        return vectorComplex;
    }

}
