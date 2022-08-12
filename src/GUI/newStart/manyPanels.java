package GUI.newStart;

import javax.swing.*;
import java.awt.*;

public class manyPanels {

    public static void main(String[] args) {
        JFrame frame = new JFrame("CKKS Encryption");

        JPanel p0 = new JPanel();
        JPanel p1 = new JPanel();
        p0.setLayout(new FlowLayout());

        //TextField tfCount = new TextField(val + "", 10);

        JLabel lbl = new JLabel("Pleas enter the complex numbers");

        JPanel fieldsPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.setLayout(new FlowLayout());

        JButton button = new JButton();
        button.setText("OK");
        p0.add(button);

        frame.add(fieldsPanel);
        frame.add(buttonPanel);

        frame.setSize(200, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}

/*
 Frame -> Panel -> Imag and real txt field
       -> Panel -> Done button
 */

