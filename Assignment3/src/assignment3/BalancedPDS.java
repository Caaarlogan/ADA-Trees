/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment3;

import java.awt.Color;

/**
 *
 * @author Jeremiah
 */
public class BalancedPDS extends BinarySearchTree {
    protected static final Color RED = Color.red;
    protected static final Color BLACK = Color.black;
    
    protected RBNode nil;
    
    public static class BlackHeightException extends RuntimeException
    {
    }
    
    protected class RBNode extends BinarySearchTree.BinaryTreeNode
    {
        protected Color color;

        public RBNode(Object element) {
            super(element);
            this.color = RED;
        }
        
        public String toString()
        {
            return super.toString() + ", " + (color == RED ? "red" : "black");
        }
    }
    
    protected void setNil(RBNode node)
    {
        node.color = BLACK;
        node.leftChild = null;
        node.rightChild = null;
    }
    
    public BalancedPDS()
    {
        setNil(new RBNode(null));
        RBNode root = nil;
    }
    
    protected void leftRotate(RBNode x)
    {
        RBNode y = (RBNode) x.rightChild;
        
        x.rightChild = y.leftChild;
        
        x.rightChild = y.leftChild;
        if (y.leftChild != nil)
            y.leftChild.parent = x;

        // Make y the root of the subtree for which x was the root.
        y.parent = x.parent;

        // If x is the root of the entire tree, make y the root.
        // Otherwise, make y the correct child of the subtree's
        // parent.
        if (x.parent == nil)
            rootNode = y;
        else 
            if (x == x.parent.leftChild)
            x.parent.leftChild = y;
            else
            x.parent.rightChild = y;

        // Relink x and y.
        y.leftChild = x;
        x.parent = y;
    }
    
    protected void rightRotate(RBNode x)
    {
        RBNode y = (RBNode) x.leftChild;

        x.leftChild = y.rightChild;
        if (x.leftChild != null)
            y.rightChild.parent = x;

        y.parent = x.parent;

        y.rightChild = x;
        x.parent = y;

        if (rootNode == x)
            rootNode = y;
        else
            if (y.parent.leftChild == x)
            y.parent.leftChild = y;
            else
            y.parent.rightChild = y;
    }
}
