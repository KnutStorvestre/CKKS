package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VectorsInputGUI implements ActionListener {

    private static int numVectors;
    private static int vectorsLength;

    public VectorsInputGUI(int numberVectors, int vectorsSize){
        numVectors = numberVectors;
        vectorsLength = vectorsSize;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
