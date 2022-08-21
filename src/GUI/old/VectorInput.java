package GUI.old;/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


/*
 * A 1.4 application that uses SpringLayout to lay out a grid.
 * Other files required: SpringUtilities.java
 */

import javax.swing.*;

public class VectorInput {

    private int sizeVec;
    private int numVec;

    public VectorInput(int sizeVector, int numVector){
        sizeVec = sizeVector;
        numVec = numVector;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void createAndShowGUI() {
        //Create the panel and populate it.
        JPanel panel = new JPanel(new SpringLayout());
        for (int i = 0; i < 2*(sizeVec*numVec); i++) {
            JTextField textField = new JTextField(Integer.toString(0));

            if (i < 2*numVec) {
                if (i % 2 == 0) {
                    textField.setText("Real");
                    textField.setEditable(false);
                } else {
                    textField.setText("Imaginary");
                    textField.setEditable(false);
                }
            }

            panel.add(textField);
        }


        SpringUtilities su0 = new SpringUtilities();

        //Lay out the panel.
        su0.makeGrid(panel,
                sizeVec, 2*numVec, //rows, cols
                5, 5, //initialX, initialY
                5, 5);//xPad, yPad

        SpringUtilities su1 = new SpringUtilities();

        JPanel panelVectorNum = new JPanel(new SpringLayout());
        for (int i = 0; i < numVec; i++) {
            JTextField textField = new JTextField();
            textField.setText("Vector " + i);

        }

        /*
        su1.makeGrid(panelVectorNum,
                1, numVec, //rows, cols
                10, 10, //initialX, initialY
                5, 5);//xPad, yPad
         */


        //Create and set up the window.
        JFrame frame = new JFrame("CKKS CKKS.Encryption");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        panel.setOpaque(true); //content panes must be opaque
        frame.setContentPane(panel);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }



}