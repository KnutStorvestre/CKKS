package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class FillInVectors {

    private static JFrame frame;
    private static int vectorSize;
    private static int numVectors;
    private static ArrayList<JTextField> textFields;


    public FillInVectors(int numberVectors, int vectorLength){
        numVectors = numberVectors;
        vectorSize = vectorLength;
        textFields = new ArrayList<>();

        frame = new JFrame();
        frame.setSize(520, 520);
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
        textFields.add(numVectorsTF);

        JButton testButton = createNewButtonForVector(vectorIndex);
        testButton.setBounds(285,y,80,25);
        panel.add(testButton);
    }

    private JButton createNewButtonForVector(int vectorIndex){
        return new JButton(new AbstractAction("Done") {
            @Override
            public void actionPerformed(ActionEvent e ) {
                //System.out.println(textFields.get(vectorIndex).getText());
                System.out.println(isNumeric(textFields.get(vectorIndex).getText()));
            }
        });
    }

    //TODO create list for array processing

    // TODO solution create action listner that launces main!!! :)))

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
