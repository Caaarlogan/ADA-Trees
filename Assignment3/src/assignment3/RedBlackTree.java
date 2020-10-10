/**
 * Implements the {@link Dictionary} interface as a red-black tree
 * from Chapter 13 of <i>Introduction to Algorithms</i>, Second
 * edition.  Objects inserted into a red-black tree must implement the
 * <code>Comparable</code> interface.
 */
package assignment3;

import java.awt.Color;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;

/**
 * @author Jeremiah Martinez
 */

public class RedBlackTree<E> extends BinarySearchTree
{
    /** Color for a red node. */
    protected static final Color RED = Color.red;

    /** Color for a black node. */
    protected static final Color BLACK = Color.black;
    
    protected BinaryTreeNode nil;
    
    //protected BinaryTreeNode nil;
    
    /**
     * Exception thrown by {@link #blackHeight} if the black-height of
     * a node is ill-defined.
     */
    public static class BlackHeightException extends RuntimeException
    {
    }
    
    protected void setNil(BinaryTreeNode node)
    {
        nil = node;
        node.color = Color.black;
        nil.element = "Nothing";
        nil.parent = nil;
        nil.leftChild = nil;
        nil.rightChild = nil;
    }
    
    public RedBlackTree()
    {
        setNil(new BinaryTreeNode(null));
        rootNode = null;
    }
    
    public RedBlackTree(Collection<? extends E> c)
    {
        super(c);
        setNil(new BinaryTreeNode(null));
        rootNode = null;
    }
    
    public RedBlackTree(Comparator<? super E> comparator)
    {
        super(comparator);
        setNil(new BinaryTreeNode(null));
        rootNode = null;
    }
    
    public RedBlackTree(SortedSet<E> s)
    {
        super(s);
        setNil(new BinaryTreeNode(null));
        rootNode = null;
    }

    /**
     * Performs a left rotation on a node, making the node's right
     * child its parent.
     *
     * @param x The node.
     */
    protected void leftRotate(BinaryTreeNode x)
    {
        BinaryTreeNode y = (BinaryTreeNode) x.rightChild;

        // Swap the in-between subtree from y to x.
        x.rightChild = y.leftChild;
        if (y.leftChild != null)
            y.leftChild.parent = x;

        // Make y the root of the subtree for which x was the root.
        y.parent = x.parent;

        // If x is the root of the entire tree, make y the root.
        // Otherwise, make y the correct child of the subtree's
        // parent.
        if (x.parent == null)
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

    /**
     * Performs a right rotation on a node, making the node's left
     * child its parent.
     *
     * @param x The node.
     */
    protected void rightRotate(BinaryTreeNode x)
    {
        BinaryTreeNode y = (BinaryTreeNode) x.leftChild;

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

    @Override
    protected void addColor(BinaryTreeNode node)
    {
        node.color = Color.red;
    }
    
    /**
     * Inserts data into the tree, creating a new node for this data.
     *
     * @param data Data to be inserted into the tree.
     * @return A reference to the <code>Node</code> object created.
     * The <code>Node</code> class is opaque to methods outside this
     * class.
     */
    public void insert(E element)
    {
        super.add(element);
    }

    /**
     * Restores the red-black conditions of the tree after inserting a
     * node.
     *
     * @param z The node inserted.
     */
    @Override
    protected void insertFixup(BinaryTreeNode newNode)
    {
        BinaryTreeNode rotateNode = null;
        
        //System.out.println(newNode + " & " + rootNode);
        //int outcome = compare(newNode.element, rootNode.element);
        //System.out.println(outcome);
        //System.out.println(newNode.element.getClass());
        
        
        
        if (compare(newNode.element, rootNode.element) != 0)
        {
            while (((BinaryTreeNode) newNode.parent).color == RED) {
                if (newNode.parent == newNode.parent.parent.leftChild) {
                    rotateNode = (BinaryTreeNode) newNode.parent.parent.rightChild;
                    if (rotateNode.color == RED) {
                        ((BinaryTreeNode) newNode.parent).color = BLACK;
                        rotateNode.color = BLACK;
                        ((BinaryTreeNode) newNode.parent.parent).color = RED;
                        newNode = (BinaryTreeNode) newNode.parent.parent;
                    }
                    else {
                        if (newNode ==  newNode.parent.rightChild) {
                        newNode = (BinaryTreeNode) newNode.parent;
                        leftRotate(newNode);
                        }

                        ((BinaryTreeNode) newNode.parent).color = BLACK;
                        ((BinaryTreeNode) newNode.parent.parent).color = RED;
                        rightRotate((BinaryTreeNode) newNode.parent.parent);
                    }
                }
                else {
                    rotateNode = (BinaryTreeNode) newNode.parent.parent.leftChild;
                    if (rotateNode.color == RED) {
                        ((BinaryTreeNode) newNode.parent).color = BLACK;
                        rotateNode.color = BLACK;
                        ((BinaryTreeNode) newNode.parent.parent).color = RED;
                        newNode = (BinaryTreeNode) newNode.parent.parent;
                    }
                    else {
                        if (newNode ==  newNode.parent.leftChild) {
                        newNode = (BinaryTreeNode) newNode.parent;
                        rightRotate(newNode);
                        }

                        ((BinaryTreeNode) newNode.parent).color = BLACK;
                        ((BinaryTreeNode) newNode.parent.parent).color = RED;
                        leftRotate((BinaryTreeNode) newNode.parent.parent);
                    }
                }
            }
        }
            
        rootNode.color = BLACK;
    }

    /**
     * Removes a node from the tree.
     *
     * @param node The node to be removed.
     * @throws DeleteSentinelException if there is an attempt to
     * delete the sentinel <code>nil</code>.
     * @throws ClassCastException if <code>node</code> does not
     * reference a <code>Node</code> object.
     */
    public void delete(Object handle)
    {
        BinaryTreeNode z = (BinaryTreeNode) handle;
        BinaryTreeNode y = z;
        BinaryTreeNode x = null;
        BinaryTreeNode w = z;

        // Do not allow the sentinel to be deleted.
        if (z == null)
            throw new RuntimeException("Cannot delete sentinel");

        if (z.leftChild != null && z.rightChild != null)
        {
            if (w.rightChild != null)
            {
                w = w.rightChild;
                while (w.leftChild != null)
                    w = w.leftChild;
                
                y = w;
            }

            else
            {
                BinaryTreeNode v = w.parent;
                while (v != null && w == v.rightChild) {
                    w = v;
                    v = v.parent;
                }
            
                y = v;
            }
        }

        if (z.leftChild != null)
            x = (BinaryTreeNode) y.leftChild;
        else
            x = (BinaryTreeNode) y.rightChild;

        x.parent = y.parent;

        if (y.parent == null)
            rootNode = x;
        else
            if (y == y.parent.leftChild)
            y.parent.leftChild = x;
            else
            y.parent.rightChild = x;

        if (y != z) {
            y.leftChild = z.leftChild;
            y.leftChild.parent = y;
            y.rightChild = z.rightChild;
            y.rightChild.parent = y;
            y.parent = z.parent;
            if (z == rootNode)
            rootNode = y;
            else
            if (z == z.parent.leftChild)
                z.parent.leftChild = y;
            else
                z.parent.rightChild = y;
        }

        if (y.color == BLACK)
            deleteFixup(x);
    }
    
    /**
     * Restores the red-black properties of the tree after a deletion.
     *
     * @param x Node at which there may be a violation.
     */
    protected void deleteFixup(BinaryTreeNode x)
    {
        while (x != rootNode && x.color == BLACK) {
            if (x.parent.leftChild == x) {
            BinaryTreeNode w = (BinaryTreeNode) x.parent.rightChild;

            if (w.color == RED) {
                w.color = BLACK;
                ((BinaryTreeNode) x.parent).color = RED;
                leftRotate((BinaryTreeNode) x.parent);
                w = (BinaryTreeNode) x.parent.rightChild;
            }

            if (((BinaryTreeNode) w.leftChild).color == BLACK 
                && ((BinaryTreeNode) w.rightChild).color == BLACK) {
                w.color = RED;
                x = (BinaryTreeNode) x.parent;
            }
            else {
                if (((BinaryTreeNode) w.rightChild).color == BLACK) {
                ((BinaryTreeNode) w.leftChild).color = BLACK;
                w.color = RED;
                rightRotate(w);
                w = (BinaryTreeNode) x.parent.rightChild;
                }

                w.color = ((BinaryTreeNode) x.parent).color;
                ((BinaryTreeNode) x.parent).color = BLACK;
                ((BinaryTreeNode) w.rightChild).color = BLACK;
                leftRotate((BinaryTreeNode) x.parent);
                x = (BinaryTreeNode) rootNode;
            }
            }
            else {
            BinaryTreeNode w = (BinaryTreeNode) x.parent.leftChild;

            if (w.color == RED) {
                w.color = BLACK;
                ((BinaryTreeNode) x.parent).color = RED;
                rightRotate((BinaryTreeNode) x.parent);
                w = (BinaryTreeNode) x.parent.leftChild;
            }

            if (((BinaryTreeNode) w.rightChild).color == BLACK 
                && ((BinaryTreeNode) w.leftChild).color == BLACK) {
                w.color = RED;
                x = (BinaryTreeNode) x.parent;
            }
            else {
                if (((BinaryTreeNode) w.leftChild).color == BLACK) {
                ((BinaryTreeNode) w.rightChild).color = BLACK;
                w.color = RED;
                leftRotate(w);
                w = (BinaryTreeNode) x.parent.leftChild;
                }

                w.color = ((BinaryTreeNode) x.parent).color;
                ((BinaryTreeNode) x.parent).color = BLACK;
                ((BinaryTreeNode) w.leftChild).color = BLACK;
                rightRotate((BinaryTreeNode) x.parent);
                x = (BinaryTreeNode) rootNode;
            }       
            }
        }
        x.color = BLACK;
    }

    /**
     * Returns the number of black nodes from a given node down to any
     * leaf.  The value should be the same for all paths.
     *
     * @param z The node.
     * @throws BlackHeightException if the number of black nodes on a
     * path from the left child down to a leaf differs from the number
     * of black nodes on a path from the right child down to a leaf.
     */
    public int blackHeight(BinaryTreeNode z)
    {
        if (z == null)
            return 0;

        int left = blackHeight((BinaryTreeNode) z.leftChild);
        int right = blackHeight((BinaryTreeNode) z.rightChild);
        if (left == right)
            if (z.color == BLACK)
            return left + 1;
            else
            return left;
        else
            throw new BlackHeightException();
    }

    /**
     * Returns the number of black nodes from the root down to any
     * leaf.  The value should be the same for all paths.
     *
     * @param z The node.
     * @throws BlackHeightException if the number of black nodes on a
     * path from the left child down to a leaf differs from the number
     * of black nodes on a path from the right child down to a leaf.
     */
    public int blackHeight()
    {
        return blackHeight((BinaryTreeNode) rootNode);
    }
}
