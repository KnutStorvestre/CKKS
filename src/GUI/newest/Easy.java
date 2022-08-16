package GUI.newest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Easy implements ActionListener {

    private static JLabel numVectorsLable;
    private static JTextField numVectorsTF;
    private static JLabel vectorSizeLabel;
    private static TextField vectorSizeTF;
    private static JButton DoneButton;
    private static JLabel success;
    private static JFrame frame;

    public static void main(String[] args) {
        JPanel panel = new JPanel();
        frame = new JFrame();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(panel);

        panel.setLayout(null);

        numVectorsLable = new JLabel("Number of Vectors");
        numVectorsLable.setBounds(10,20,80,25);
        panel.add(numVectorsLable);

        numVectorsTF = new JTextField();
        numVectorsTF.setBounds(100, 20, 165, 25);
        panel.add(numVectorsTF);

        vectorSizeLabel = new JLabel("Size of vectors");
        vectorSizeLabel.setBounds(10,50,80,25);
        panel.add(vectorSizeLabel);

        vectorSizeTF = new TextField();
        vectorSizeTF.setBounds(100, 50, 165, 25);
        panel.add(vectorSizeTF);

        DoneButton = new JButton("Done");
        DoneButton.setBounds(10,80,80,25);
        DoneButton.addActionListener(new Easy());
        panel.add(DoneButton);

        success = new JLabel("");
        success.setBounds(10,110,300,25);
        panel.add(success);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String numVextorsStr = numVectorsTF.getText();
        String vectorSizeStr = vectorSizeTF.getText();
        System.out.println(numVextorsStr + ", " + vectorSizeStr);

        if (isInteger(numVextorsStr)){
            int numVectors = Integer.parseInt(numVextorsStr);
            if (numVectors > 10 || numVectors < 1) {
                success.setText("Number of Vectors must be less than 10 and more than 0");
            }
            else {
                if (isInteger(vectorSizeStr)){
                    int vectorSize = Integer.parseInt(vectorSizeStr);
                    if (vectorSize > 32 || vectorSize < 2) {
                        success.setText("Number of Vectors must be < 33 and positive");
                    } else if (!(((Math.log(vectorSize) / Math.log(2)) % 1) == 0)) {
                        success.setText("Number is not the power of two");
                    }
                    else {
                        success.setText("WOW");
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

     public static boolean isPowerOfTwo(int num){
        double log2Val = Math.log(num) / Math.log(2);
        System.out.println(log2Val);
        return false;
     }

     public static boolean isInteger(String str){
        try {
            int a = Integer.parseInt(str);
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }
     }
}
