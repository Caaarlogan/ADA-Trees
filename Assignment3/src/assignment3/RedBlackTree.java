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

    protected void rbtAdd(List<Boolean> path, E o)
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
    
    protected void rbtRemove(List<Boolean> path)
    {
    	//boolean finished = false;
    	BinaryTreeNode parent = null;
    	BinaryTreeNode sibling = null;
    	BinaryTreeNode current = rootNode;
    	
    	boolean siblingChildrenCheck;
    	boolean caseThreeLoop = false;
    	
    	for (boolean direction : path)
        {
            parent = current;
    		
            if (!direction)
                current = parent.leftChild;
            else
                current = parent.rightChild;
        }
    	
    	//Case One
    	//check if removal node and replacement node are different colors
    	if (current.color.equals(RED))
            current.color = BLACK;
    	else // current.color == BLACK
    	{
            while (!caseThreeLoop) {
                if (current == parent.leftChild)
                    sibling = parent.rightChild;
                else // current == parent.rightChild;
                    sibling = parent.leftChild;

                //Case Two
                if (sibling.color.equals(RED))
                {
                    sibling.color = BLACK;
                    parent.color = RED;

                    if (current == parent.leftChild)
                        //Test
                        leftRotate(sibling, parent);
                    else
                        rightRotate(sibling, parent);

                    if (current == parent.leftChild)
                        sibling = parent.rightChild;
                    else
                        sibling = parent.leftChild;
                }

                //Case Three
                //At this stage, sibling's color is currently set as BLACK
                if (sibling.leftChild.color.equals(BLACK) && sibling.rightChild.color.equals(BLACK))
                {
                    sibling.color = RED;
                    current = parent;

                    if (current.color.equals(RED))
                        current.color = BLACK;
                    else
                        caseThreeLoop = true;
                }

                if (!caseThreeLoop)
                {
                    //Check sibling children for Case Four
                    if (current == parent.leftChild)
                    siblingChildrenCheck = sibling.leftChild.color.equals(RED) && sibling.rightChild.color.equals(BLACK);
                    else
                        siblingChildrenCheck = sibling.rightChild.color.equals(RED) && sibling.leftChild.color.equals(BLACK);

                    //Case Four
                    if (siblingChildrenCheck)
                    {
                        if (current == parent.leftChild)
                            sibling.leftChild.color = BLACK;
                        else
                            sibling.rightChild.color = BLACK;

                        sibling.color = RED;

                        if (current == parent.leftChild)
                            rightRotate(sibling.rightChild, sibling);
                        else
                            leftRotate(sibling.leftChild, sibling);

                        if (current == parent.leftChild)
                            sibling = parent.rightChild;
                        else
                            sibling = parent.leftChild;
                    }

                    //Check sibling children for Case Five
                    if (current == parent.leftChild)
                        siblingChildrenCheck = sibling.rightChild.color.equals(RED);
                    else
                        siblingChildrenCheck = sibling.leftChild.color.equals(RED);

                    //Case Five
                    if (siblingChildrenCheck)
                    {
                        sibling.color = parent.color;
                        parent.color = BLACK;

                        if (current == parent.leftChild)
                            sibling.rightChild.color = BLACK;
                        else
                            sibling.leftChild.color = BLACK;

                        if (current == parent.leftChild)
                            leftRotate(sibling, parent);
                        else
                            rightRotate(sibling, parent);
                    }
                }
            }
    	}
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
