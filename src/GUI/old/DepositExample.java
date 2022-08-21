package GUI.old;

import javax.swing.*;

public class DepositExample {
    JFrame frame;
    JPanel buttonPane, fieldsPanel, descriptionPanel;
    JLabel RealLabel, imaginaryLabel;
    JTextField cashField, checksField;
    JButton ok, cancel;

    DepositExample() {
        frame = new JFrame("CKKS CKKS.Encryption");

        buttonPane = new JPanel();
        fieldsPanel = new JPanel();
        descriptionPanel = new JPanel();

        RealLabel = new JLabel("Real");
        imaginaryLabel = new JLabel("Imaginary");

        cashField = new JTextField("",10);
        checksField = new JTextField("",10);

        ok = new JButton("OK");
        cancel = new JButton("Cancel");

        /*
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        buttonPane.setLayout(new FlowLayout());
         */

        descriptionPanel.add(RealLabel);
        descriptionPanel.add(imaginaryLabel);
        fieldsPanel.add(cashField);
        fieldsPanel.add(checksField);
        buttonPane.add(ok);
        buttonPane.add(cancel);

        /*
        frame.add(fieldsPanel, BorderLayout.PAGE_START);
        frame.add(descriptionPanel, BorderLayout.PAGE_START);
        frame.add(buttonPane, BorderLayout.PAGE_END);
         */

        JPanel panelOfPanels = new JPanel();
        panelOfPanels.setLayout(new BoxLayout(panelOfPanels, BoxLayout.PAGE_AXIS));

        panelOfPanels.add(descriptionPanel);
        panelOfPanels.add(fieldsPanel);
        panelOfPanels.add(buttonPane);
        /*
        frame.add(fieldsPanel);
        frame.add(descriptionPanel);
        frame.add(buttonPane);
         */

        frame.add(panelOfPanels);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    public static void main(String args[]) {
        new DepositExample();
    }
}