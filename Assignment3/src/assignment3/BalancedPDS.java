package assignment3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlo Carbonilla
 */
public class BalancedPDS<E> extends RedBlackTree<E>
{

    protected static final Color RED = Color.red;
    protected static final Color BLACK = Color.black;

    private List<BinaryTreeNode> versions;

    public BalancedPDS()
    {
        super();
        versions = new ArrayList();
    }

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

    @Override
    protected void versionAdd(List<Boolean> path, E o)
    {
        if (path.isEmpty())
        {
            BinaryTreeNode root = new BinaryTreeNode(o);
            root.color = BLACK;
            versions.add(root);
        }
        else
        {
            boolean finished = false;

            BinaryTreeNode lastRoot = versions.get(versions.size() - 1);
            BinaryTreeNode root = new BinaryTreeNode(lastRoot.element);
            boolean modified = false;

            while (!finished)
            {

                BinaryTreeNode lastGreatGrandParent = null;
                BinaryTreeNode lastGrandParent = null;
                BinaryTreeNode lastParent = null;
                BinaryTreeNode lastUncle = null;
                BinaryTreeNode lastCurrent = lastRoot;

                BinaryTreeNode greatGrandParent = null;
                BinaryTreeNode grandParent = null;
                BinaryTreeNode parent = null;
                BinaryTreeNode uncle = null;
                BinaryTreeNode current = root;

                lastRoot.color = BLACK;
                root.color = BLACK;

                for (boolean direction : path)
                {
                    lastGreatGrandParent = lastGrandParent;
                    lastGrandParent = lastParent;
                    lastParent = lastCurrent;

                    greatGrandParent = grandParent;
                    grandParent = parent;
                    parent = current;

                    if (modified)
                    {
                        if(!direction)
                            current = current.leftChild;
                        else
                            current = current.rightChild;
                    }
                    else if (!direction)
                    {
                        current.rightChild = lastCurrent.rightChild;

                        if (lastCurrent.leftChild != null)
                        {
                            current.leftChild = new BinaryTreeNode(lastCurrent.leftChild.element);
                            current.leftChild.color = lastCurrent.leftChild.color;
                        }
                        else
                        {
                            current.leftChild = new BinaryTreeNode(o);
                            current.leftChild.color = RED;
                        }

                        lastCurrent = lastCurrent.leftChild;
                        current = current.leftChild;
                    }
                    else
                    {
                        current.leftChild = lastCurrent.leftChild;

                        if (lastCurrent.rightChild != null)
                        {
                            current.rightChild = new BinaryTreeNode(lastCurrent.rightChild.element);
                            current.rightChild.color = lastCurrent.rightChild.color;
                        }
                        else
                        {
                            current.rightChild = new BinaryTreeNode(o);
                            current.rightChild.color = RED;
                        }

                        lastCurrent = lastCurrent.rightChild;
                        current = current.rightChild;
                    }
                }
                
                if(modified)
                    modified = false;
                
                if (parent != null && parent.color.equals(RED))
                {
                    modified = true;

                    //if false, uncle is parent left child
                    //if true, uncle is parent right child
                    boolean whichUncle = false;

                    if (parent == grandParent.leftChild)
                    {
                        lastUncle = lastGrandParent.rightChild;
                        uncle = grandParent.rightChild;
                        whichUncle = true;
                    }
                    else
                    {
                        lastUncle = lastGrandParent.leftChild;
                        uncle = grandParent.leftChild;
                        whichUncle = false;
                    }

                    //Case One
                    if (uncle != null && uncle.color.equals(RED))
                    {
                        parent.color = BLACK;
                        grandParent.color = RED;

                        //Create new uncle when recolouring as it's uses link
                        //from previous version
                        BinaryTreeNode newUncle = new BinaryTreeNode(uncle.element);
                        newUncle.leftChild = uncle.leftChild;
                        newUncle.rightChild = uncle.rightChild;
                        newUncle.color = BLACK;

                        if (!whichUncle)
                            grandParent.leftChild = newUncle;
                        else
                            grandParent.rightChild = newUncle;

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
                            BinaryTreeNode newRoot1 = leftRotate(parent, grandParent);

                            if (newRoot1 != null)
                                root = newRoot1;

                            int size = path.size();
                            boolean parentPath = path.get(path.size() - 2);
                            boolean currentPath = path.get(path.size() - 1);
                            path.add(size - 1, parentPath);
                            path.add(size - 2, currentPath);
                            path.remove(path.size() - 1);
                            path.remove(path.size() - 1);
                            current.color = BLACK;
                            grandParent.color = RED;

                            BinaryTreeNode newRoot2 = rightRotate(grandParent, greatGrandParent);

                            if (newRoot2 != null)
                                root = newRoot2;

                            path.remove(path.size() - 2);
                        }
                        //Case Three
                        else
                        {
                            parent.color = BLACK;
                            grandParent.color = RED;
                            BinaryTreeNode newRoot = rightRotate(grandParent, greatGrandParent);

                            if (newRoot != null)
                                root = newRoot;

                            path.remove(path.size() - 2);
                        }
                    }
                    //if parent is right child of grandParent
                    else //Case Two
                    {
                        if (current == parent.leftChild)
                        {
                            BinaryTreeNode newRoot1 = rightRotate(parent, grandParent);

                            if (newRoot1 != null)
                                root = newRoot1;

                            int size = path.size();
                            boolean parentPath = path.get(path.size() - 2);
                            boolean currentPath = path.get(path.size() - 1);
                            path.add(size - 1, parentPath);
                            path.add(size - 2, currentPath);
                            path.remove(path.size() - 1);
                            path.remove(path.size() - 1);
                            current.color = BLACK;
                            grandParent.color = RED;
                            BinaryTreeNode newRoot2 = leftRotate(grandParent, greatGrandParent);

                            if (newRoot2 != null)
                                root = newRoot2;

                            path.remove(path.size() - 2);
                        }
                        //Case Three
                        else
                        {
                            parent.color = BLACK;
                            grandParent.color = RED;
                            BinaryTreeNode newRoot = leftRotate(grandParent, greatGrandParent);

                            if (newRoot != null)
                                root = newRoot;

                            path.remove(path.size() - 2);
                        }
                    }
                }
                else
                {
                    finished = true;
                    root.color = BLACK;
                    versions.add(root);
                }
            }
        }
    }
    
    @Override
    protected void versionRemove(List<Boolean> path, List<Boolean> replacePath, Object o)
    {
        
    }

    private BinaryTreeNode rightRotate(BinaryTreeNode parent, BinaryTreeNode grandParent)
    {
        boolean changeRoot = false;

        BinaryTreeNode current = parent.leftChild;
        parent.leftChild = current.rightChild;

        if (grandParent == null)
            changeRoot = true;

        else //parent is left child of grandparent
        if (grandParent.rightChild == parent)
            grandParent.rightChild = current;
        //parent is right child of grandparent
        else
            grandParent.leftChild = current;

        current.rightChild = parent;

        if (changeRoot)
            return current;
        else
            return null;
    }

    private BinaryTreeNode leftRotate(BinaryTreeNode parent, BinaryTreeNode grandParent)
    {
        boolean changeRoot = false;

        BinaryTreeNode current = parent.rightChild;
        parent.rightChild = current.leftChild;

        if (grandParent == null)
            changeRoot = true;

        else //parent is left child of grandparent
        if (grandParent.leftChild == parent)
            grandParent.leftChild = current;
        //parent is right child of grandparent
        else
            grandParent.rightChild = current;

        current.leftChild = parent;

        if (changeRoot)
            return current;
        else
            return null;
    }

    public List<BinaryTreeNode> getVersions()
    {
        return versions;
    }
}
