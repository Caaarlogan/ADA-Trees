package assignment3;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 * @author Carlo Carbonilla
 */

public class BalancedPDSTest
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Red Black Tree GUI builder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // create the binary search tree
        BalancedPDS<String> tree = new BalancedPDS();
        
        BalancedPDSGUI<String> gui = new BalancedPDSGUI(tree);
        
        frame.getContentPane().add(gui);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        int screenHeight = dimension.height;
        int screenWidth = dimension.width;
        frame.pack();             //resize frame apropriately for its content
        //positions frame in center of screen
        frame.setLocation(new Point((screenWidth / 2) - (frame.getWidth() / 2),
                (screenHeight / 2) - (frame.getHeight() / 2)));
        frame.setVisible(true);
    }
}
