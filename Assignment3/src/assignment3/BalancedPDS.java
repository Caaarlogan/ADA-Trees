package assignment3;

import java.awt.Color;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;

/**
 *
 * @author Jeremiah
 */
public class BalancedPDS<E> extends BinarySearchTree {
    protected static final Color RED = Color.red;
    protected static final Color BLACK = Color.black;
    
    protected RBNode nil;
    
    public static class BlackHeightException extends RuntimeException
    {
    }
    
    protected class RBNode extends BinarySearchTree.BinaryTreeNode
    {
        protected Color color;

        public RBNode(E element) {
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
        rootNode = nil;
    }
    
    public BalancedPDS(Collection<? extends E> c)
    {
        super(c);
    }

    public BalancedPDS(Comparator<? super E> comparator)
    {
        super(comparator);
    }

    public BalancedPDS(SortedSet<E> s)
    {
        super(s);
    }
    
    protected void leftRotate(RBNode x)
    {
        RBNode y = (RBNode) x.rightChild;
        
        x.rightChild = y.leftChild;
        
        x.rightChild = y.leftChild;
        if (y.leftChild != nil)
            y.leftChild.parent = x;

        y.parent = x.parent;
        
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
    
    public void insert(E element)
    {
        RBNode newNode = new RBNode(element);
        
        super.add(newNode);
        insertFixup(newNode);
    }
    
    protected void insertFixup(RBNode newNode)
    {
        RBNode rotateNode = null;
        
        while (((RBNode) newNode.parent).color == RED) {
            if (newNode.parent == newNode.parent.parent.leftChild) {
                rotateNode = (RBNode) newNode.parent.parent.rightChild;
            
            if (rotateNode.color == RED) {
                ((RBNode) newNode.parent).color = BLACK;
                rotateNode.color = BLACK;
                ((RBNode) newNode.parent.parent).color = RED;
                newNode = (RBNode) newNode.parent.parent;
            }
            
            else {
                if (newNode == newNode.parent.rightChild) {
                    newNode = (RBNode) newNode.parent;
                    leftRotate(newNode);
                }
                
                ((RBNode) newNode.parent).color = BLACK;
                ((RBNode) newNode.parent.parent).color = RED;
                rightRotate((RBNode) newNode.parent.parent);
            }
            }
            
            else {
                rotateNode = (RBNode) newNode.parent.parent.leftChild;
                
                if (rotateNode.color == RED) {
                    ((RBNode) newNode.parent).color = BLACK;
                    rotateNode.color = BLACK;
                    ((RBNode) newNode.parent.parent).color = RED;
                    newNode = (RBNode) newNode.parent.parent;
                }
                
                else {
                    if (newNode == newNode.parent.leftChild) {
                        newNode = (RBNode) newNode.parent;
                        rightRotate(newNode);
                    }
                    
                    ((RBNode) newNode.parent).color = BLACK;
                    ((RBNode) newNode.parent.parent).color = RED;
                    leftRotate((RBNode) newNode.parent.parent);
                }
            }
        }
        
        ((RBNode) rootNode).color = BLACK;
    }
    
    public int blackHeight(RBNode node)
    {
        if (node == null)
            return 0;
        
        int left = blackHeight((RBNode) node.leftChild);
        int right = blackHeight((RBNode) node.rightChild);
        
        if (left == right)
            if (node.color == BLACK)
                return left + 1;
            else
                return left;
        else
            throw new BlackHeightException();
    }
}