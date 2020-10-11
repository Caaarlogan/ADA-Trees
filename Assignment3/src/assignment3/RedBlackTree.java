package assignment3;

import java.awt.Color;
import java.util.List;

/**
 * @author Carlo Carbonilla
 */
public class RedBlackTree<E> extends BinarySearchTree<E>
{

    protected static final Color RED = Color.red;
    protected static final Color BLACK = Color.black;

    //Notes down path the add or remove method takes
    //false means left, true means right
    protected void notePath(List<Boolean> path, boolean direction)
    {
        path.add(direction);
    }

    protected void colorRed(BinaryTreeNode node)
    {
        node.color = RED;
    }

    protected void rbtAdd(List<Boolean> path)
    {
        BinaryTreeNode greatGrandParent = null;
        BinaryTreeNode grandParent = null;
        BinaryTreeNode parent = null;
        BinaryTreeNode uncle = null;
        BinaryTreeNode current = rootNode;
        boolean whichUncle = false;

        for (boolean direction : path)
        {
            greatGrandParent = grandParent;
            grandParent = parent;
            parent = current;

            if (!direction)
                current = parent.leftChild;
            else
                current = parent.rightChild;
        }

        //if false, uncle is left child of grandparent
        //if true, uncle is right child of grandparent
        if (path.size() > 1)
            whichUncle = !path.get(path.size() - 2);

        while (parent != null && parent.color.equals(RED))
        {
            //if uncle is right child, parent is left child
            if (whichUncle)
                uncle = grandParent.rightChild;
            else
                uncle = grandParent.leftChild;

            if (uncle != null && uncle.color.equals(RED))
            {
                parent.color = BLACK;
                uncle.color = BLACK;
                grandParent.color = RED;

                //repeat process with grandparent
                path = path.subList(0, path.size() - 2);
            }
            else if (whichUncle)
            {
                if (parent.rightChild == current)
                {
                    current = parent;
                    parent = grandParent;
                    grandParent = greatGrandParent;
                    leftRotate(current, parent);
                }

                parent.color = BLACK;
                grandParent.color = RED;
                rightRotate(grandParent, greatGrandParent);
            }
            else
            {
                if (parent.leftChild == current)
                {
                    current = parent;
                    parent = grandParent;
                    grandParent = greatGrandParent;
                    rightRotate(current, parent);
                }

                parent.color = BLACK;
                grandParent.color = RED;
                leftRotate(grandParent, greatGrandParent);
            }
        }

        rootNode.color = BLACK;
    }
    
    //Feel free to edit parameters of this hook method to your fitting
    //Have a look at Persistent Dynamic Set remove for inspiration?
    protected void rbtRemove(List<Boolean> path)
    {
    }

    private void rightRotate(BinaryTreeNode parent, BinaryTreeNode grandParent)
    {
        BinaryTreeNode current = parent.leftChild;
        parent.leftChild = current.rightChild;

        if (grandParent == null)
            rootNode = current;
        else //parent is left child of grandparent
        if (grandParent.rightChild == parent)
            grandParent.rightChild = current;
        //parent is right child of grandparent
        else
            grandParent.leftChild = current;

        current.rightChild = parent;
    }

    private void leftRotate(BinaryTreeNode parent, BinaryTreeNode grandParent)
    {
        BinaryTreeNode current = parent.rightChild;
        parent.rightChild = current.leftChild;

        if (grandParent == null)
            rootNode = current;
        else //parent is left child of grandparent
        if (grandParent.leftChild == parent)
            grandParent.leftChild = current;
        //parent is right child of grandparent
        else
            grandParent.rightChild = current;

        current.leftChild = parent;
    }
}
