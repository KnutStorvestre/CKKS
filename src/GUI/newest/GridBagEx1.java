package GUI.newest;

import javax.swing.*;
import java.awt.*;
import java.applet.Applet;

public class GridBagEx1 extends Applet {

    private void makebutton(String name, GridBagLayout gridbag, GridBagConstraints c) {
        Button button = new Button(name);
        gridbag.setConstraints(button, c);
        add(button);
    }

    private void makeTextFieldNotEditable(String name, GridBagLayout gridbag, GridBagConstraints c){
        TextField textField = new TextField(name);
        textField.setEditable(false);
        gridbag.setConstraints(textField, c);
        add(textField);
    }
    private void makeTextField(String name, GridBagLayout gridbag, GridBagConstraints c){
        TextField textField = new TextField(name);
        gridbag.setConstraints(textField, c);
        add(textField);
    }

    public void init() {

        int totVectors = 4;
        int vectorSize = 4;

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        setFont(new Font("SansSerif", Font.PLAIN, 14));
        setLayout(gridbag);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 10.0;

        for (int i = 0; i < totVectors; i++) {
            if (totVectors == i + 1){
                c.gridwidth = GridBagConstraints.REMAINDER;
            }
            makeTextFieldNotEditable("Vector " + i, gridbag, c);
        }

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;


        for (int i = 0; i < 2*totVectors; i++) {
            System.out.println("i");
            /*
            if (2*totVectors == i + 1){
                c.gridwidth = GridBagConstraints.REMAINDER;
            }
             */
            //if (i%2 == 0){
            makeTextFieldNotEditable("Real" + i, gridbag, c);
            //}
            /*
            else {
                makeTextFieldNotEditable("Imaginary" + i, gridbag, c);
            }
             */
        }

        setSize(500, 100);
    }


    public static void main(String args[]) {
        JFrame f = new JFrame("GridBag Layout Example");
        GridBagEx1 ex1 = new GridBagEx1();

        ex1.init();

        f.add("Center", ex1);
        f.pack();
        f.setSize(f.getPreferredSize());
        f.show();

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}