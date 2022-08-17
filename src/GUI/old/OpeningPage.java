package GUI.old;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpeningPage {

    public OpeningPage(){

        JFrame window = new JFrame("JFrame with text");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new CardLayout(300,300));

        JPanel centerPanel = new JPanel();

        JLabel header = new JLabel("Welcome to CKKS encryptor");
        // header.setFont(Font.PLAIN, 12);
        header.setSize(50,50);
        centerPanel.add(header);
        JButton b = new JButton("push me");

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //your actions
            }
        });

        centerPanel.add(b);

        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));

        window.add(centerPanel);
        window.pack();
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }

}
