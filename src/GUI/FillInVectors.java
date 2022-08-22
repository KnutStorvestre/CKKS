package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class FillInVectors {

    private static JFrame frame;
    private static int vectorSize;
    private static int numVectors;
    private static ArrayList<JTextField> textFieldsVectors;
    private static ArrayList<JLabel> vectorValuesLabel;
    private static boolean[] validVectors;
    private static JLabel nextButtonErrorMsg;
    private static double[][] vectors;

    public FillInVectors(int numberVectors, int vectorLength){
        numVectors = numberVectors;
        vectorSize = vectorLength;
        textFieldsVectors = new ArrayList<>(numVectors);
        vectorValuesLabel = new ArrayList<>(numVectors);
        validVectors = new boolean[numberVectors];
        vectors = new double[numberVectors][vectorLength];

        frame = new JFrame();
        frame.setSize(1000, 520);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        JLabel instructions = new JLabel("Example of vector: [2+2.5i, 0, 5.34, 2i]");
        instructions.setBounds(10,20,500,25);
        panel.add(instructions);

        int yVal = 50;
        for (int i = 0; i < numberVectors; i++) {
            createTextFieldLabelButton(panel,yVal, i);
            yVal += 30;
        }

        JButton nextButton = createNextButton();
        nextButton.setBounds(10,yVal,120,25);
        panel.add(nextButton);

        nextButtonErrorMsg = new JLabel();
        nextButtonErrorMsg.setBounds(140,yVal,400,25);
        panel.add(nextButtonErrorMsg);

        frame.setVisible(true);
    }

    private void createTextFieldLabelButton(JPanel panel, int y, int vectorIndex){
        // the text field should by default be [0,0,0,0]
        // user can change to [2+2.5i,0,5.34,2i]
        JLabel numVectorsLable = new JLabel("vector " + vectorIndex);
        numVectorsLable.setBounds(10,y,120,25);
        panel.add(numVectorsLable);

        StringBuilder vectorStr = new StringBuilder("[");
        for (int i = 0; i < vectorSize; i++) {
            vectorStr.append("0");
            if (i != vectorSize-1)
                vectorStr.append(",");
        }
        vectorStr.append("]");
        JTextField numVectorsTF = new JTextField(String.valueOf(vectorStr));
        numVectorsTF.setBounds(110, y, 165, 25);
        panel.add(numVectorsTF);
        textFieldsVectors.add(numVectorsTF);

        JButton button = createNewButtonForVector(vectorIndex);
        button.setBounds(285,y,80,25);
        panel.add(button);

        JLabel success = new JLabel();
        success.setBounds(400,y,2000,25);
        panel.add(success);
        vectorValuesLabel.add(success);
    }

    private JButton createNextButton(){
        return new JButton(new AbstractAction("Next") {
            @Override
            public void actionPerformed(ActionEvent e ) {
                for (boolean val : validVectors){
                    if (!val){
                        nextButtonErrorMsg.setText("You have not set all vectors");
                        return;
                    }
                }
                frame.dispose();
            }
        });
    }

    private JButton createNewButtonForVector(int vectorIndex){
        return new JButton(new AbstractAction("Set") {
            @Override
            public void actionPerformed(ActionEvent e ) {
                double[] processedVector = vectorProcessing(textFieldsVectors.get(vectorIndex).getText());
                if (processedVector == null){
                    vectorValuesLabel.get(vectorIndex).setText("ERROR");
                    validVectors[vectorIndex] = false;
                }
                else {
                    String labelFormattedInputVector = "[";

                    for (int i = 0; i < processedVector.length; i++) {
                        if (i%2== 1){
                            if (processedVector[i] >= 0){
                                labelFormattedInputVector += "+";
                            }
                            labelFormattedInputVector += processedVector[i];
                            labelFormattedInputVector += "i,";
                        }
                        else {
                            labelFormattedInputVector += processedVector[i];
                        }
                    }
                    vectors[vectorIndex] = processedVector;
                    validVectors[vectorIndex] = true;
                    labelFormattedInputVector = labelFormattedInputVector.substring(0,labelFormattedInputVector.length()-1) + "]";
                    vectorValuesLabel.get(vectorIndex).setText(labelFormattedInputVector);
                }
            }
        });
    }

    // TODO create list for array processing
    // TODO solution create action listner that launces main!!! :)))
    public static double[] vectorProcessing(String vectorStr){
        vectorStr = vectorStr.replace(" ","");
        vectorStr = vectorStr.replace("[","");
        vectorStr = vectorStr.replace("]","");
        String[] vectorStrSplit = vectorStr.split(",");


        if (vectorStrSplit.length != vectorSize){
            return null;
        }

        // even = real number
        // odd = imaginary number
        double[] complexNumbers = new double[vectorSize*2];

        double[] complexNumSplit;
        for (int i = 0; i < vectorStrSplit.length; i++) {
            complexNumSplit = processRealAndImag(vectorStrSplit[i]);
            //System.out.println(Arrays.toString(complexNumSplit));
            if (complexNumSplit == null)
                return null;
            complexNumbers[2*i] = complexNumSplit[0];
            complexNumbers[(2*i)+1] = complexNumSplit[1];
        }
        return complexNumbers;
    }

    /**
     *
     * @param complexNumStr
     * @return list of 2 elements. First real number then imaginary number
     */
    public static double[] processRealAndImag(String complexNumStr){
        if (complexNumStr.charAt(0) != '-')
            complexNumStr = "+" + complexNumStr;

        String[] operators = complexNumStr.replace("i","").split("[0-9]+");
        String[] operands = complexNumStr.substring(1).split("[+-]");

        double[] result = new double[2];
        String imgNum;
        for (int i = 0; i < operands.length; i++) {
            // if imaginary or real
            //System.out.println(operands[i]);
            if (operands[i].contains("i")){
                imgNum = operands[i].replace("i","");
                if (!isNumeric(imgNum)) {
                    return null;
                }
                if (operators[i].equals("-"))
                    result[1] = -1*Double.parseDouble(imgNum);
                else{
                    result[1] = Double.parseDouble(imgNum);
                }
            }
            else {
                if (!isNumeric(operands[i])){
                    return null;
                }
                if (operators[i].equals("-"))
                    result[0] = -1*Double.parseDouble(operands[i]);
                else{
                    result[0] = Double.parseDouble(operands[i]);
                }
            }
        }
        return result;
    }

    /**
     * Check if string can be translated to real or imaginary number.
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        str = str.replace("i","");
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
