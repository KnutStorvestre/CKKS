package GUI.newest;

import GUI.VectorInput;

import javax.swing.*;

public class Main{

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.

        int sizeVector = 4;
        int numVectors = 4;

        VectorInput a = new VectorInput(sizeVector, numVectors);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                a.createAndShowGUI();
            }
        });

        System.out.println("hei");
    }

}
