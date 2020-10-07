package assignment3;

import assignment3.BalancedPDS.RBNode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

/**
 * Almost Complete GUI - just need to finish the code when pressing the buttons
 * and updating the number of nodes in the tree.. WIll only build once
 * BinarySearchTree.BinaryTreeNode subclasses are made
 *
 * @author sehall
 */
public class BalancedPDS_GUI<E> extends JPanel
{
    private DrawPanel drawPanel;
    private BalancedPDS<E> tree;
    private BalancedPDS.RBNode root;
    private int numberNodes = 0;
    public static int PANEL_H = 500;
    public static int PANEL_W = 700;
    private JLabel nodeCounterLabel;
    private final int BOX_SIZE = 40;

    public BalancedPDS_GUI(BalancedPDS<E> tree)
    {
        super(new BorderLayout());
        
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e)
        {
        }
        
        this.tree = tree;
        root = ((RBNode) tree.getRoot());
        numberNodes = tree.size();
        super.setPreferredSize(new Dimension(PANEL_W, PANEL_H + 30));
        drawPanel = new DrawPanel();

        super.add(drawPanel, BorderLayout.CENTER);

        nodeCounterLabel = new JLabel("Number of Nodes: " + numberNodes);
        super.add(nodeCounterLabel, BorderLayout.NORTH);
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

            if (tree.getRoot() != null)
            {
                drawTree(g, getWidth());
            }
        }

        public void drawTree(Graphics g, int width)
        {
            drawNode(g, root, BOX_SIZE, 0, 0, new HashMap<>());
        }

        private int drawNode(Graphics g, BinarySearchTree.BinaryTreeNode current,
                int x, int level, int nodeCount, Map<BinarySearchTree.BinaryTreeNode, Point> map)
        {

            if (current.leftChild != null)
            {
                nodeCount = drawNode(g, current.leftChild, x, level + 1, nodeCount, map);
            }

            int currentX = x + nodeCount * BOX_SIZE;
            int currentY = level * 2 * BOX_SIZE + BOX_SIZE;
            nodeCount++;
            map.put(current, new Point(currentX, currentY));

            if (current.rightChild != null)
            {
                nodeCount = drawNode(g, current.rightChild, x, level + 1, nodeCount, map);
            }

            g.setColor(Color.red);
            if (current.leftChild != null)
            {
                Point leftPoint = map.get(current.leftChild);
                g.drawLine(currentX, currentY, leftPoint.x, leftPoint.y - BOX_SIZE / 2);
            }
            if (current.rightChild != null)
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