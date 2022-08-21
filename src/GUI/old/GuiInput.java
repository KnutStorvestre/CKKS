package GUI.old;

import javax.swing.*;
import java.awt.*;

public class GuiInput {

    // TODO
    // getLengthOfVectors


    /*
           setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setLocationRelativeTo(null);  // better to use..
        setLocationByPlatform(true);
        setTitle("Bla Blubb");
        setResizable(false);
        //setLayout(null); // better to use layouts with padding & borders

        // set a flow layout with large hgap and vgap.
        panel = new JPanel(new FlowLayout(SwingConstants.LEADING, 10, 10));
        // panel.setBounds(5, 5, 290, 290); // better to pack()
        add(panel);

        //textField = new JTextField(); // suggest a size in columns
        textField = new JTextField(8);
        //textField.setBounds(5, 5, 280, 50); // to get height, set large font
        textField.setFont(textField.getFont().deriveFont(50f));
        panel.add(textField);

        pack(); // make the GUI the minimum size needed to display the content
        setVisible(true);
     */
    public int selectNumberVectors(){
        JFrame frame = new JFrame();
        frame.setTitle("CKKS CKKS.Encryption");

        JTextField txtField = new JTextField(20);
        frame.add(txtField);
        JButton btn = new JButton("Click to close!");
        frame.setContentPane(btn);
        btn.addActionListener(e -> {
            frame.dispose();
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(300, 300));
        frame.pack();
        frame.setVisible(true);


        /*
        String totVectorsStr = "";
        int totVectorsInt = -1;

        boolean valid = false;
        while (!valid){
            totVectorsStr = JOptionPane.showInputDialog(null, "Enter number of vectors: 1-9");
            try {
                totVectorsInt = Integer.parseInt(totVectorsStr);
                if (totVectorsInt > 0 && totVectorsInt < 10)
                    valid = true;
                else
                    JOptionPane.showMessageDialog(null, "The number you entered is either too large or too small",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(null, "Please enter a valid number between 1 and 9",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            //if (totVectorsStr.equals("2"))
            //    valid = true;
        }
        System.out.println("hhi");
        return totVectorsInt;
         */
        return -1;
    }
}
