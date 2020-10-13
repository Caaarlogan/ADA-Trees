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

    protected void colorBlack(BinaryTreeNode node)
    {
        node.color = BLACK;
    }

    protected void rbtAdd(List<Boolean> path)
    {
        boolean finished = false;

        while (!finished)
            if (path.size() > 1)
            {
                BinaryTreeNode greatGrandParent = null;
                BinaryTreeNode grandParent = null;
                BinaryTreeNode parent = null;
                BinaryTreeNode uncle = null;
                BinaryTreeNode current = rootNode;

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
                
                if (parent != null && parent.color.equals(RED))
                {
                    if (parent == grandParent.leftChild)
                        uncle = grandParent.rightChild;
                    else
                        uncle = grandParent.leftChild;
                    
                    //Case One
                    if (uncle != null && uncle.color.equals(RED))
                    {
                        parent.color = BLACK;
                        uncle.color = BLACK;
                        grandParent.color = RED;

                        //repeat process with grandparent
                        path = path.subList(0, path.size() - 2);
                    }

                    //Cases Two & Three
                    //if parent is left child of grandParent
                    else if (parent == grandParent.leftChild)
                    {
                        //Case Two
                        if (current == parent.rightChild)
                        {
                            leftRotate(parent, grandParent);
                            int size = path.size();
                            boolean parentPath = path.get(path.size()-2);
                            boolean currentPath = path.get(path.size()-1);
                            path.add(size-1, parentPath);
                            path.add(size-2, currentPath);
                            path.remove(path.size()-1);
                            path.remove(path.size()-1);
                            current.color = BLACK;
                            grandParent.color = RED;
                            rightRotate(grandParent, greatGrandParent);
                            path.remove(path.size()-2);
                        }
                        //Case Three
                        else
                        {
                            parent.color = BLACK;
                            grandParent.color = RED;
                            rightRotate(grandParent, greatGrandParent);
                            path.remove(path.size()-2);
                        }
                    }
                    //if parent is right child of grandParent
                    else
                        //Case Two
                        if (current == parent.leftChild)
                        {
                            rightRotate(parent, grandParent);
                            int size = path.size();
                            boolean parentPath = path.get(path.size()-2);
                            boolean currentPath = path.get(path.size()-1);
                            path.add(size-1, parentPath);
                            path.add(size-2, currentPath);
                            path.remove(path.size()-1);
                            path.remove(path.size()-1);
                            current.color = BLACK;
                            grandParent.color = RED;
                            leftRotate(grandParent, greatGrandParent);
                            path.remove(path.size()-2);
                        }
                        //Case Three
                        else
                        {
                            parent.color = BLACK;
                            grandParent.color = RED;
                            leftRotate(grandParent, greatGrandParent);
                            path.remove(path.size()-2);
                        }
                }
                else
                    finished = true;

                rootNode.color = BLACK;
            }
            else
                finished = true;
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