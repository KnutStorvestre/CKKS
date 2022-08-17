package GUI;

import javax.swing.*;
import java.awt.*;
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

        int yVal = 20;
        for (int i = 0; i < numberVectors; i++) {
            createTextFieldLabelButton(panel,yVal, i);
            yVal += 30;
        }

        frame.setVisible(true);
    }

    private void createTextFieldLabelButton(JPanel panel, int y, int vectorIndex){
        // the text field should by default be [0,0,0,0]
        // user can change to [2+2.5i,2,2,2i]
        JLabel numVectorsLable = new JLabel("vector " + vectorIndex);
        numVectorsLable.setBounds(10,y,120,25);
        panel.add(numVectorsLable);

        JTextField numVectorsTF = new JTextField();
        numVectorsTF.setBounds(110, y, 165, 25);
        panel.add(numVectorsTF);
        textFields.add(numVectorsTF);

        JButton testButton = createNewButton(vectorIndex);
        testButton.setBounds(285,y,80,25);
        panel.add(testButton);
    }

    private JButton createNewButton(int vectorIndex){
        return new JButton(new AbstractAction("Done " + vectorIndex) {
            @Override
            public void actionPerformed(ActionEvent e ) {
                System.out.println("hu");
                System.out.println(textFields.get(vectorIndex).getText());
            }
        });
    }
    
}
