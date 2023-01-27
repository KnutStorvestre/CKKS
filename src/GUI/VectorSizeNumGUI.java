package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.ReentrantLock;

public class VectorSizeNumGUI implements ActionListener{

    private static JLabel numVectorsLable;
    private static JTextField numVectorsTF;
    private static JLabel vectorSizeLabel;
    private static TextField vectorSizeTF;
    private static JButton DoneButton;
    private static JLabel success;
    private static JFrame frame;
    private static int vectorSize;
    private static int numVectors;


    public VectorSizeNumGUI() {
        JPanel panel = new JPanel();
        frame = new JFrame();
        frame.setSize(520, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        numVectorsLable = new JLabel("Number of vectors");
        numVectorsLable.setBounds(10,20,120,25);
        panel.add(numVectorsLable);

        numVectorsTF = new JTextField();
        numVectorsTF.setBounds(130, 20, 165, 25);
        panel.add(numVectorsTF);

        vectorSizeLabel = new JLabel("Size of vectors");
        vectorSizeLabel.setBounds(10,50,104,25);
        panel.add(vectorSizeLabel);

        vectorSizeTF = new TextField();
        vectorSizeTF.setBounds(130, 50, 165, 25);
        panel.add(vectorSizeTF);

        DoneButton = new JButton("Done");
        DoneButton.setBounds(10,80,80,25);
        DoneButton.addActionListener(this);
        panel.add(DoneButton);

        success = new JLabel("");
        success.setBounds(10,110,500,25);
        panel.add(success);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String numVectorsStr = numVectorsTF.getText();
        String vectorSizeStr = vectorSizeTF.getText();

        if (isInteger(numVectorsStr)){
            int numVectorsTmp = Integer.parseInt(numVectorsStr);
            if (numVectorsTmp > 10 || numVectorsTmp < 1) {
                success.setText("Number of Vectors must be less than 10 and more than 0");
            }
            else {
                if (isInteger(vectorSizeStr)){
                    int vectorSizeTmp = Integer.parseInt(vectorSizeStr);
                    if (vectorSizeTmp > 32 || vectorSizeTmp < 1) {
                        success.setText("Size of Vectors must be < 33 and positive");
                    } else if (!(((Math.log(vectorSizeTmp) / Math.log(2)) % 1) == 0)) {
                        success.setText("Size is not the power of two");
                    }
                    else {
                        success.setText("Success");
                        vectorSize = vectorSizeTmp;
                        numVectors = numVectorsTmp;
                        FillInVectors fillInVectors = new FillInVectors(numVectors, vectorSize);
                        frame.dispose();
                    }
                }
                else {
                    success.setText("Vector size must be an integer");
                }
            }
        }
        else {
            success.setText("Number of vectors must be an integer");
        }
     }

     public static boolean isInteger(String str){
        try {
            Integer.parseInt(str);
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }
     }

}
