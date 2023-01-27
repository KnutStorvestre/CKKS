package GUI;

import CKKSOperations.Parameters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.math.MathContext;

public class ParametersInput implements ActionListener {

    private static int sizeVector;
    private static int totVectors;
    private static double[][] vectorValues;
    private static BigInteger polynomialDegree;
    private static JFrame frame;
    private static JLabel instructions;
    private static JLabel polynomialDegreeLabel;
    private static JLabel polynomialDegreeLabelVal;
    private static JLabel bitPrimeSizeLabel;
    private static JTextField bitPrimeSizeTF;
    private static JLabel millerRabinIterationsLabel;
    private static JTextField millerRabinIterationsTF;
    private static JLabel descriptionOfBase;
    private static JLabel moduloBigLabel;
    private static JTextField moduloBigTF;
    private static JLabel moduloSmallLabel;
    private static JTextField moduloSmallTF;
    private static JLabel scalingFactorLabel;
    private static JTextField scalingFactorTF;
    private static JButton done;
    private static JLabel success;

    public ParametersInput(int vectorSize, int numVectors, double[][] vectorsVals){
        sizeVector = vectorSize;
        totVectors = numVectors;
        vectorValues = vectorsVals;
        polynomialDegree = new BigInteger(String.valueOf(2*vectorSize));

        frame = new JFrame();
        frame.setSize(450, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        instructions = new JLabel("Parameters");
        instructions.setFont(new Font(null,1,15));
        instructions.setBounds(100,10,120,25);
        panel.add(instructions);

        polynomialDegreeLabel = new JLabel("Polynomial degree:");
        polynomialDegreeLabel.setBounds(10,40,150,25);
        panel.add(polynomialDegreeLabel);

        polynomialDegreeLabelVal = new JLabel(String.valueOf(2*vectorSize));
        polynomialDegreeLabelVal.setBounds(165,40,150,25);
        panel.add(polynomialDegreeLabelVal);

        bitPrimeSizeLabel = new JLabel("Bit size of primes:");
        bitPrimeSizeLabel.setBounds(10,60,150,25);
        panel.add(bitPrimeSizeLabel);

        // TODO set limit on characters in text field
        bitPrimeSizeTF = new JTextField("500");
        bitPrimeSizeTF.setBounds(160,60,100,25);
        panel.add(bitPrimeSizeTF);

        millerRabinIterationsLabel = new JLabel("Miller Rabin iterations:");
        millerRabinIterationsLabel.setBounds(10,80,150,25);
        panel.add(millerRabinIterationsLabel);

        millerRabinIterationsTF = new JTextField("30");
        millerRabinIterationsTF.setBounds(160,80,100,25);
        panel.add(millerRabinIterationsTF);

        // TODO make it more clear that value in text field will be the exponent to base 2!
        descriptionOfBase = new JLabel("All values below has base 2. Set the exponent.");
        descriptionOfBase.setBounds(10,100,350,25);
        panel.add(descriptionOfBase);

        moduloBigLabel = new JLabel("Big modulo: 2^");
        moduloBigLabel.setBounds(10,120,150,25);
        panel.add(moduloBigLabel);

        moduloBigTF = new JTextField("2000");
        moduloBigTF.setBounds(160,120,100,25);
        panel.add(moduloBigTF);

        moduloSmallLabel = new JLabel("Small modulo: 2^");
        moduloSmallLabel.setBounds(10,140,150,25);
        panel.add(moduloSmallLabel);

        moduloSmallTF = new JTextField("1200");
        moduloSmallTF.setBounds(160,140,100,25);
        panel.add(moduloSmallTF);

        scalingFactorLabel = new JLabel("Scaling factor: 2^");
        scalingFactorLabel.setBounds(10,160,150,25);
        panel.add(scalingFactorLabel);

        scalingFactorTF = new JTextField("40");
        scalingFactorTF.setBounds(160,160,100,25);
        panel.add(scalingFactorTF);

        done = new JButton("Done");
        done.setBounds(10,200,80,25);
        done.addActionListener(this);
        panel.add(done);

        success = new JLabel();
        success.setBounds(100,200,500,25);
        success.setForeground(Color.RED);
        panel.add(success);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean allValid = true;
        if (!isNumeric(bitPrimeSizeTF.getText())){
            bitPrimeSizeTF.setText("ERROR");
            allValid = false;
        }
        if (!isNumeric(millerRabinIterationsTF.getText())){
            millerRabinIterationsTF.setText("ERROR");
            allValid = false;
        }
        if (!isNumeric(moduloBigTF.getText())){
            moduloBigTF.setText("ERROR");
            allValid = false;
        }
        if (!isNumeric(moduloSmallTF.getText())){
            moduloSmallTF.setText("ERROR");
            allValid = false;
        }
        if (!isNumeric(scalingFactorTF.getText())){
            scalingFactorTF.setText("ERROR");
            allValid = false;
        }
        if (!allValid){
            success.setText("ERROR: Invalid value(s)!");
        }
        else {
            if (!(Integer.parseInt(scalingFactorTF.getText()) < Integer.parseInt(moduloSmallTF.getText())  && (Integer.parseInt(moduloSmallTF.getText()) < Integer.parseInt(moduloBigTF.getText())))){
                success.setText("scaling factor < small modulo < big modulo");
            }
            else {
                int bigModExp = Integer.parseInt(moduloBigTF.getText());
                int smallModExp = Integer.parseInt(moduloSmallTF.getText());
                int scalingFactorExp = Integer.parseInt(scalingFactorTF.getText());
                int millerRabinIterations = Integer.parseInt(millerRabinIterationsTF.getText());
                int primeBitSize = Integer.parseInt(bitPrimeSizeTF.getText());
                MathContext mc = new MathContext(100);
                Parameters parameters = new Parameters(polynomialDegree, BigInteger.TWO.pow(bigModExp),
                        BigInteger.TWO.pow(smallModExp), BigInteger.TWO.pow(scalingFactorExp), primeBitSize, millerRabinIterations, mc);
                frame.dispose();
                CkksOperations ckksOperations = new CkksOperations(vectorValues, sizeVector, totVectors, parameters, mc);
            }
        }
    }

    public static boolean isNumeric(String str){
        try {
            return Integer.parseInt(str) > 1;
        } catch(NumberFormatException e){
            return false;
        }
    }

}
