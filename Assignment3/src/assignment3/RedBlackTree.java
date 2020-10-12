package assignment3;

import java.awt.Color;
import java.util.ArrayList;
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
    
    //Update path for fixup after rotating a parent node
    protected void pathParentRotate(List<Boolean> path)
    {
        boolean oldDirection = path.get(path.size() - 1);
        path.remove(path.size() - 1);
        path.add(!oldDirection);
    }
    
    //Update path for fixup after rotating a grandparent node
    protected void pathGrandparentRotate(List<Boolean> path)
    {
        path.remove(path.size() - 1);
        path.remove(path.size() - 1);
    }

    protected void colorRed(BinaryTreeNode node)
    {
        node.color = RED;
    }

    protected void rbtAddFixup(List<Boolean> path)
    {
        BinaryTreeNode greatGrandParent = null;
        BinaryTreeNode grandParent = null;
        BinaryTreeNode parent = null;
        BinaryTreeNode uncle = null;
        BinaryTreeNode current = rootNode;
        boolean whichUncle = false;
        
        List<Boolean> newPath = path;
        boolean repeatRbtFixup = true;
        
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
        
        if (current == rootNode || parent.color.equals(BLACK))
            repeatRbtFixup = false;
        
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
                    
                    if (parent.rightChild.color == RED)
                    {
                        pathParentRotate(path);
                        newPath = path; 
                    }
                }

                parent.color = BLACK;
                if (parent != rootNode)
                {
                    grandParent.color = RED;
                    rightRotate(grandParent, greatGrandParent);
                    
                    pathGrandparentRotate(path);
                    newPath = path;
                }
            }
            else if (!whichUncle)
            {
                if (parent.leftChild == current)
                {
                    current = parent;
                    parent = grandParent;
                    grandParent = greatGrandParent;
                    rightRotate(current, parent);
                    
                    if (parent.rightChild.color == RED)
                    {
                        pathParentRotate(path);
                        newPath = path; 
                    }
                }

                parent.color = BLACK;
                if (parent != rootNode)
                {
                    grandParent.color = RED;
                    leftRotate(grandParent, greatGrandParent);
                    
                    pathGrandparentRotate(path);
                    newPath = path;
                }
            }
        }

        rootNode.color = BLACK;
        
        if (repeatRbtFixup)
            rbtAddFixup(newPath);
    }
    
    //Feel free to edit parameters of this hook method to your fitting
    //Have a look at Persistent Dynamic Set remove for inspiration?
    protected void rbtRemoveFixup(List<Boolean> path)
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
