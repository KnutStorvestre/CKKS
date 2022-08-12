package GUI.old;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// An AWT program inherits from the top-level container java.awt.Frame
public class AWTCounter extends Frame {
    private Label lblCount;    // Declare a Label component
    private TextField tfCount; // Declare a TextField component
    private Button btnCount;   // Declare a Button component
    private int count = 0;     // Counter's value

    // Constructor to setup GUI components and event handlers
    public AWTCounter () {
        setLayout(new FlowLayout());

        lblCount = new Label("Val");
        add(lblCount);

        tfCount = new TextField(count + "", 10);
        tfCount.setEditable(false);
        add(tfCount);

        btnCount = new Button("Add");
        add(btnCount);
        btnCountAddListener listenerAdd = new btnCountAddListener();
        btnCount.addActionListener(listenerAdd);


        btnCount = new Button("Minus");
        add(btnCount);
        btnCountMinusListener listenerMinus = new btnCountMinusListener();
        btnCount.addActionListener(listenerMinus);


        setTitle("AWT Counter");
        setSize(300, 100);

        setVisible(true);

    }

    // The entry main() method
    public static void main(String[] args) {
        // Invoke the constructor to setup the GUI, by allocating an instance
        AWTCounter app = new AWTCounter();
        // or simply "new AWTCounter();" for an anonymous instance
    }

    // Define an inner class to handle the "Count" button-click
    private class btnCountAddListener implements ActionListener {
        // ActionEvent handler - Called back upon button-click.
        @Override
        public void actionPerformed(ActionEvent evt) {
            ++count; // Increase the counter value
            // Display the counter value on the TextField tfCount
            tfCount.setText(count + ""); // Convert int to String
        }
    }

    private class btnCountMinusListener implements ActionListener {
        // ActionEvent handler - Called back upon button-click.
        @Override
        public void actionPerformed(ActionEvent evt) {
            --count; // Increase the counter value
            // Display the counter value on the TextField tfCount
            tfCount.setText(count + ""); // Convert int to String
        }
    }
}
