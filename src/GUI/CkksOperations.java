package GUI;

import CKKS.*;
import keys.PublicKey;
import keys.SecretKey;
import modules.Complex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

public class CkksOperations {
    private static int vectorLength;
    private static int totVectors;
    private static double[][] vectorsVals;
    private static int[] levels;
    private static Parameters params;
    private static MathContext mc;
    private static int nextResult;
    private static KeyGeneratorCKKS keyGenerator;
    private static SecretKey secretKey;
    private static PublicKey publicKey;
    private static PublicKey relinearizationKey;
    // These will not be removed under reset
    private static ArrayList<Vector> vectors;
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
    private static JButton deleteKeysButton;
    private static JButton deleteResultsButton;
    private static JButton resetParametersButton;
    private static JButton resetVectorsButton;
    private static JLabel vectorsSign;
    private static JLabel resultsSign;

    //TODO logic and GUI should be kept separate
    private static Encoder encoder;
    private static Encryption encryption;
    private static Decryptor decryptor;
    private static Evaluation evaluation;

    //TODO these can probably be in another class
    private static ArrayList<JLabel>  vectorSymbols;
    private static ArrayList<JButton> vectorUpButtons;
    private static ArrayList<JButton> vectorDownButtons;

    public CkksOperations(double[][] vectorsValues, int vectorsSize, int numVectors, Parameters parameters, MathContext mathContext){
        vectorLength = vectorsSize;
        totVectors = numVectors;
        vectorsVals = vectorsValues;
        params = parameters;
        mc = mathContext;

        vectors = new ArrayList<>(numVectors);
        // 0 = vector 1 = encoded 2 = encrypted
        levels = new int[numVectors];

        //TODO these can probably be in another class
        vectorSymbols = new ArrayList<>(numVectors);
        vectorUpButtons = new ArrayList<>(numVectors);
        vectorDownButtons = new ArrayList<>(numVectors);

        Vector tmpVector;
        for (int i = 0; i < numVectors; i++) {
            tmpVector = new Vector();
            tmpVector.setVector(doubleVectorValsToComplexArrayList(vectorsValues[i]));
            vectors.add(tmpVector);
        }

        encoder = new Encoder(params, mc);
        evaluation = new Evaluation(params);

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

        nextResult = 0;
        currentResult = new JLabel("=R0");
        currentResult.setBounds(330,40,30,25);
        panel.add(currentResult);

        addButton = createAdditionButton();
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

        //TODO maybe add in the future?
        /*
        deleteKeysButton = createDeleteKeysButton();
        deleteKeysButton.setBounds(5,100,150,25);
        panel.add(deleteKeysButton);
         */

        deleteResultsButton = createDeleteResultButton();
        deleteResultsButton.setBounds(150,100,150,25);
        panel.add(deleteResultsButton);

        resetParametersButton = createResetParametersButton();
        resetParametersButton.setBounds(295,100,150,25);
        panel.add(resetParametersButton);

        resetVectorsButton = createResetVectorsButton();
        resetVectorsButton.setBounds(440,100,150,25);
        panel.add(resetVectorsButton);

        vectorsSign = new JLabel("Vectors");
        vectorsSign.setBounds(80,130,150,25);
        vectorsSign.setFont(new Font(null,1,15));
        panel.add(vectorsSign);

        resultsSign = new JLabel("Results");
        resultsSign.setBounds(500,130,150,25);
        resultsSign.setFont(new Font(null,1,15));
        panel.add(resultsSign);

        int yPos = 160;
        for (int i = 0; i < vectors.size(); i++) {
            addNewVectorLabelButtons(i, yPos);
            yPos += 30;
        }

        frame.setVisible(true);
    }


    // TODO this can probably be an own class
    private void addNewResultLabelButtons(int resultIndex, int yPos){
        JLabel vectorLabel = new JLabel("Vector " + resultIndex);
        vectorLabel.setBounds(10,yPos,100,25);
        panel.add(vectorLabel);

        JLabel vectorSymbolLabel = new JLabel("E" + resultIndex);
        vectorSymbolLabel.setBounds(85,yPos,100,25);
        vectorSymbols.add(vectorSymbolLabel);
        panel.add(vectorSymbolLabel);

        JButton showVectorValuesButton = createShowVectorValuesButton(resultIndex);
        showVectorValuesButton.setBounds(130,yPos,60,25);
        panel.add(showVectorValuesButton);

        JButton addVectorButton = createAddVectorButton(resultIndex);
        addVectorButton.setBounds(190,yPos,60,25);
        panel.add(addVectorButton);

        JButton vectorDownButton = createVectorLevelUpButton(resultIndex);
        vectorDownButton.setBounds(250,yPos,80,25);
        vectorUpButtons.add(vectorDownButton);
        panel.add(vectorDownButton);

        JButton vectorUpButton = createVectorLevelDownButton(resultIndex);
        vectorUpButton.setBounds(330,yPos,80,25);
        vectorDownButtons.add(vectorUpButton);
        panel.add(vectorUpButton);
    }

    // TODO this can probably be an own class
    private void addNewVectorLabelButtons(int vectorIndex, int yPos){
        JLabel vectorLabel = new JLabel("Vector " + vectorIndex);
        vectorLabel.setBounds(10,yPos,100,25);
        panel.add(vectorLabel);

        JLabel vectorSymbolLabel = new JLabel("V" + vectorIndex);
        vectorSymbolLabel.setBounds(85,yPos,100,25);
        vectorSymbols.add(vectorSymbolLabel);
        panel.add(vectorSymbolLabel);

        JButton showVectorValuesButton = createShowVectorValuesButton(vectorIndex);
        showVectorValuesButton.setBounds(130,yPos,60,25);
        panel.add(showVectorValuesButton);

        JButton addVectorButton = createAddVectorButton(vectorIndex);
        addVectorButton.setBounds(190,yPos,60,25);
        panel.add(addVectorButton);

        JButton vectorUpButton = createVectorLevelUpButton(vectorIndex);
        vectorUpButton.setBounds(250,yPos,80,25);
        vectorUpButtons.add(vectorUpButton);
        panel.add(vectorUpButton);

        JButton vectorDownButton = createVectorLevelDownButton(vectorIndex);
        vectorDownButton.setBounds(330,yPos,80,25);
        vectorDownButton.setVisible(false);
        vectorDownButtons.add(vectorDownButton);
        panel.add(vectorDownButton);
    }

    //TODO vector down should have a bette name!
    private JButton createVectorLevelUpButton(int vectorIndex) {
        return new JButton(new AbstractAction("Encode") {
            int vectorIdx = vectorIndex;
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO generate error message if user tries to encrypt without keys
                Vector tmpVector;
                if (levels[vectorIndex] == 0){
                    tmpVector = vectors.get(vectorIdx);
                    if (tmpVector.getEncoded() == null){
                        tmpVector.setEncoded(encoder.encode(tmpVector.getVector(), params.getScalingFactor()));
                    }
                    levels[vectorIdx] = 1;
                    infoMsgLabel.setText("Encoded");
                    vectorUpButtons.get(vectorIndex).setText("Encrypt");
                    vectorDownButtons.get(vectorIdx).setVisible(true);
                    vectorDownButtons.get(vectorIdx).setText("Decode");
                }
                else {
                    if (encryption == null){
                        infoMsgLabel.setText("Generate keys before encryption!");
                    }
                    else {
                        tmpVector = vectors.get(vectorIdx);
                        if (tmpVector.getEncrypted() == null) {
                            tmpVector.setEncrypted(encryption.encrypt(tmpVector.getEncoded()));
                        }
                        levels[vectorIdx] = 2;
                        vectorUpButtons.get(vectorIdx).setVisible(false);
                        vectorDownButtons.get(vectorIdx).setText("Decrypt");
                        infoMsgLabel.setText("Encrypted");
                    }
                }
                System.out.println(vectorIdx);
            }
        });
    }

    //TODO vector up should have a bette name!
    private JButton createVectorLevelDownButton(int vectorIndex) {
        return new JButton(new AbstractAction("Decode") {
            int vectorIdx = vectorIndex;


            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO generate error message if user tries to encrypt without keys
                System.out.println(levels[vectorIndex]);
                if (levels[vectorIndex] == 2){
                    infoMsgLabel.setText("Decrypted");
                    levels[vectorIndex] = 1;
                    vectorDownButtons.get(vectorIdx).setText("Decode");
                    vectorUpButtons.get(vectorIdx).setText("Encrypt");
                    vectorUpButtons.get(vectorIdx).setVisible(true);
                }
                else {
                    infoMsgLabel.setText("Decoded");
                    levels[vectorIndex] = 0;
                    vectorDownButtons.get(vectorIdx).setVisible(false);
                    vectorUpButtons.get(vectorIdx).setText("Encode");
                }
                System.out.println(vectorIdx);
            }
        });
    }

    private JButton createAddVectorButton(int vectorIndex) {
        return new JButton(new AbstractAction("Add") {
            int vectorIdx = vectorIndex;
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO add error message if
                // mismatch between vectors
                // not enough operators
                infoMsgLabel.setText("Vector added to operations");
                System.out.println(vectorIdx);
            }
        });
    }

    private JButton createShowVectorValuesButton(int vectorIndex) {
        return new JButton(new AbstractAction("Show") {
            int vectorIdx = vectorIndex;
            @Override
            public void actionPerformed(ActionEvent e) {
                infoMsgLabel.setText("Vector printed in terminal");
                vectors.get(vectorIdx).printVector(levels[vectorIndex]);
                System.out.println(vectorIdx);
            }
        });
    }

    private JButton createDeleteKeysButton() {
        return new JButton(new AbstractAction("Delete keys") {
            @Override
            public void actionPerformed(ActionEvent e) {
                secretKey = null;
                publicKey = null;
                relinearizationKey = null;
                infoMsgLabel.setText("Keys are deleted");
            }
        });
    }

    private JButton createResetVectorsButton() {
        return new JButton(new AbstractAction("Reset vectors") {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                VectorSizeNumGUI vectorSizeNumGUI = new VectorSizeNumGUI();
            }
        });
    }

    private JButton createResetParametersButton() {
        return new JButton(new AbstractAction("Reset parameters") {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                ParametersInput parametersInput = new ParametersInput(vectorLength, totVectors, vectorsVals);
            }
        });
    }

    private JButton createDeleteResultButton() {
        return new JButton(new AbstractAction("Delete Results") {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoMsgLabel.setText("Results are deleted");
            }
        });
    }

    private JButton createGenKeyButton() {
        return new JButton(new AbstractAction("Generate keys") {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoMsgLabel.setText("Keys generated");

                keyGenerator = new KeyGeneratorCKKS(params);
                secretKey = keyGenerator.getSecretKey();
                publicKey = keyGenerator.getPublicKey();
                relinearizationKey = keyGenerator.getRelinearizationKey();

                encryption = new Encryption(params, publicKey, secretKey);
                decryptor = new Decryptor(params, secretKey);

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

    private JButton createAdditionButton(){
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