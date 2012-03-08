package Admin;
/**
  * Shows color set by RGB scrollbars.
  * @version One of my first Java programs (1995?), last update 2002-04-21.
  * @author Fred Swartz
  */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

////////////////////////////////////////////////////////////////// ColorDisplay
class ColorDisplay extends JFrame {

    JSlider redBar; // Slider for adjusting red from 0-255
    JSlider grnBar;
    JSlider bluBar;

    int red   = 128; // initial red color value
    int green = 128;
    int blue  = 128;

    GraphicsPanel palette; // an area to display the color
    JTextField txtField;   // shows the Java source equivalent

    //================================================ constructor
    public ColorDisplay() {
        palette = new GraphicsPanel();

        // Create a textfield for displaying the values
        txtField = new JTextField();
        txtField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtField.setText("Color(" + red + ", " + green + ", " + blue +
                ") or " + "Color(0x" + 
                Integer.toHexString(blue + 256 * (green + 256 * red)) + ")");
        txtField.setEditable(false);

        // Create one object that will listen to all sliders
        ChangeListener al = new ColorListener();

        redBar = new JSlider(JSlider.VERTICAL, 0, 255, red);
        redBar.addChangeListener(al);
        redBar.setBackground(Color.red);

        grnBar = new JSlider(JSlider.VERTICAL, 0, 255, green);
        grnBar.addChangeListener(al);
        grnBar.setBackground(Color.green);

        bluBar = new JSlider(JSlider.VERTICAL, 0, 255, blue);
        bluBar.addChangeListener(al);
        bluBar.setBackground(Color.blue);

        //=== Put the color palette and textfield in a box
        Box paletteText = Box.createVerticalBox();
        paletteText.add(palette);
        paletteText.add(txtField);

        //=== put the 3 sliders in a gridlayout panel
        JPanel colorControls = new JPanel();
        colorControls.setLayout(new GridLayout(1, 3));
        colorControls.add(redBar);
        colorControls.add(grnBar);
        colorControls.add(bluBar);
        
        //=== Now let's build the content pane
        Container content = this.getContentPane();
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
        content.add(paletteText);
        content.add(colorControls);
        
        this.setTitle("ColorDisplay");
        this.pack();
    }//end constructor
    
////////////////////////////////////////////////////////////// GraphicsPanel
        // A component for drawing ,overrides paintComponent()
    class GraphicsPanel extends JPanel {
        //======================================== GraphicsPanel constructor
        public GraphicsPanel() {
            this.setPreferredSize(new Dimension(300, 300));
            this.setBackground(Color.white);
            this.setForeground(Color.black);
        }//end constructor

        //=================================== GraphicsPanel : paintComponent
        public void paintComponent(Graphics g) {
            super.paintComponent(g);           // paint background, border
            g.setColor(new Color(red, green, blue)); // Set the color
            g.fillRect(0, 0, this.getWidth(), this.getHeight()); // Color everything
        }//endmethod paintComponent
    }//endclass GraphicsPanel

    
    ///////////////////////////////////////////////////// class ColorListener
        // This class is created only as a place to put the listener.
        // We could have just as easily put the listener in any other class,
        // or used an anonymous listener.
        // This is defined as an inner class (defined inside of ColorDisplay),
        // so it can reference the field variables of that class (specifically,
        // the sliders, graphics panel, and text field).
    class ColorListener implements ChangeListener {
        public void stateChanged(ChangeEvent ae) {
            red   = redBar.getValue();
            green = grnBar.getValue();
            blue  = bluBar.getValue();
            txtField.setText("Color(" + red + ", " + green + ", " + blue +
                    ") or " + "Color(0x" + Integer.toHexString(blue +
                    256 * (green + 256 * red)) + ")");
            palette.repaint();
        }//endmethod stateChanged
    }//endclass ColorListener
    
    //================================================================= main
    public static void main(String[] args) {
        JFrame window = new ColorDisplay();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }//end main

}//end class ColorDisplay