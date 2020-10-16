package assignment3;

import assignment3.BinarySearchTree.BinaryTreeNode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * Almost Complete GUI - just need to finish the code when pressing the buttons
 * and updating the number of nodes in the tree.. WIll only build once
 * BinaryTreeNode subclasses are made
 *
 * @author sehall
 */
public class BinarySearchTreeGUI<E> extends JPanel implements ActionListener
{
    private final JButton addButton, removeButton;
    private DrawPanel drawPanel;
    private BinarySearchTree tree;
    private BinaryTreeNode root;
    private JTextField postFixField;
    public static int PANEL_H = 500;
    public static int PANEL_W = 700;
    private final int BOX_SIZE = 40;

    public BinarySearchTreeGUI(BinarySearchTree tree)
    {
        super(new BorderLayout());
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e)
        {
        }
        this.tree = tree;
        root = tree.getRoot();
        super.setPreferredSize(new Dimension(PANEL_W, PANEL_H + 30));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(PANEL_W, 30));
        drawPanel = new DrawPanel();

        addButton = new JButton("Add");
        removeButton = new JButton("Remove");

        addButton.addActionListener((ActionListener) this);
        removeButton.addActionListener((ActionListener) this);

        postFixField = new JTextField(40);

        buttonPanel.add(postFixField);
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        super.add(drawPanel, BorderLayout.CENTER);
        super.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        Object source = event.getSource();

        if (source == addButton && !postFixField.getText().equals(""))
        {   //finish this button event to handle the evaluation and output to infix of the tree 
            String newString = postFixField.getText();
            boolean added = tree.add(newString);
            
            if (added)
                root = tree.getRoot();
            
            postFixField.setText("");
            
        } else if (source == removeButton && !postFixField.getText().equals(""))
        {
            String newString = postFixField.getText();
            boolean removed = tree.remove(newString);
            
            if (removed)
                root = tree.getRoot();
            
            postFixField.setText("");
        }

        drawPanel.repaint();
    }

    private class DrawPanel extends JPanel
    {

        public DrawPanel()
        {
            super();
            super.setBackground(Color.WHITE);
            super.setPreferredSize(new Dimension(PANEL_W, PANEL_H));
        }

        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            if (root != null)
            {
                drawTree(g, getWidth());
            }
        }

        public void drawTree(Graphics g, int width)
        {
            drawNode(g, root, BOX_SIZE, 0, 0, new HashMap<>());
        }

        private int drawNode(Graphics g, BinaryTreeNode current,
                int x, int level, int nodeCount, Map<BinaryTreeNode, Point> map)
        {

            if (current.leftChild.element != null)
            {
                nodeCount = drawNode(g, current.leftChild, x, level + 1, nodeCount, map);
            }

            int currentX = x + nodeCount * BOX_SIZE;
            int currentY = level * 2 * BOX_SIZE + BOX_SIZE;
            nodeCount++;
            map.put(current, new Point(currentX, currentY));

            if (current.rightChild.element != null)
            {
                nodeCount = drawNode(g, current.rightChild, x, level + 1, nodeCount, map);
            }

            g.setColor(Color.red);
            if (current.leftChild.element != null)
            {
                Point leftPoint = map.get(current.leftChild);
                g.drawLine(currentX, currentY, leftPoint.x, leftPoint.y - BOX_SIZE / 2);
            }
            if (current.rightChild.element != null)
            {
                Point rightPoint = map.get(current.rightChild);
                g.drawLine(currentX, currentY, rightPoint.x, rightPoint.y - BOX_SIZE / 2);

            }
            g.setColor(Color.WHITE);

            Point currentPoint = map.get(current);
            g.fillRect(currentPoint.x - BOX_SIZE / 2, currentPoint.y - BOX_SIZE / 2, BOX_SIZE, BOX_SIZE);
            g.setColor(Color.BLACK);
            g.drawRect(currentPoint.x - BOX_SIZE / 2, currentPoint.y - BOX_SIZE / 2, BOX_SIZE, BOX_SIZE);
            Font f = new Font("courier new", Font.BOLD, 16);
            g.setFont(f);
            g.drawString(current.toString(), currentPoint.x - current.toString().length() * 4, currentPoint.y);

            return nodeCount;

        }
    }
}
