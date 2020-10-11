package assignment3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;

/**
 * @author Carlo Carbonilla
 */
public class PersistentDynamicSet<E> extends BinarySearchTree<E>
{

    private List<BinaryTreeNode> versions;
    
    public PersistentDynamicSet()
    {
        super();
        versions = new ArrayList();
    }

    public PersistentDynamicSet(Collection<? extends E> c)
    {
        super(c);
        versions = new ArrayList();
    }

    public PersistentDynamicSet(Comparator<? super E> comparator)
    {
        super(comparator);
        versions = new ArrayList();
    }

    public PersistentDynamicSet(SortedSet<E> s)
    {
        super(s);
        versions = new ArrayList();
    }

    @Override
    //Notes down path the add or remove method takes
    //false means left, true means right
    protected void notePath(List<Boolean> path, boolean direction)
    {
        path.add(direction);
    }

    @Override
    //Creates new version using the last version with a given path and
    //element to add
    protected void versionAdd(List<Boolean> path, E o)
    {
        if (path.isEmpty())
        {
            BinaryTreeNode root = new BinaryTreeNode(o);
            versions.add(root);
        }
        else
        {
            //Get root of last version
            BinaryTreeNode currentLast = versions.get(versions.size() - 1);

            //Create new root and current node
            BinaryTreeNode newRoot = new BinaryTreeNode(currentLast.element);
            BinaryTreeNode currentNew = newRoot;

            //Traverse through path, add new element when child node is null
            for (boolean direction : path)
                if (!direction)
                {
                    currentNew.rightChild = currentLast.rightChild;

                    if (currentLast.leftChild != null)
                        currentNew.leftChild = new BinaryTreeNode(currentLast.leftChild.element);
                    else
                        currentNew.leftChild = new BinaryTreeNode(o);

                    currentLast = currentLast.leftChild;
                    currentNew = currentNew.leftChild;
                }
                else
                {
                    currentNew.leftChild = currentLast.leftChild;

                    if (currentLast.rightChild != null)
                        currentNew.rightChild = new BinaryTreeNode(currentLast.rightChild.element);
                    else
                        currentNew.rightChild = new BinaryTreeNode(o);

                    currentLast = currentLast.rightChild;
                    currentNew = currentNew.rightChild;
                }

            versions.add(newRoot);
        }
    }

    @Override
    protected void versionRemove(List<Boolean> path, List<Boolean> replacePath, Object o)
    {
        E element = (E) o;
        BinaryTreeNode lastRoot = versions.get(versions.size() - 1); //Get root of last version
        BinaryTreeNode newRoot = new BinaryTreeNode(lastRoot.element); //Create new root

        BinaryTreeNode lastParent = lastRoot;
        BinaryTreeNode lastRemove = lastRoot;

        BinaryTreeNode newParent = newRoot;
        BinaryTreeNode newRemove = newRoot;

        if (!path.isEmpty())
        {
            // determine whether to traverse to left or right of root
            if (!path.get(0))
            {
                newParent.rightChild = lastParent.rightChild;
                newParent.leftChild = new BinaryTreeNode(lastParent.leftChild.element);

                newRemove = newParent.leftChild;
                lastRemove = lastParent.leftChild;

            }
            else
            {
                newParent.leftChild = lastParent.leftChild;
                newParent.rightChild = new BinaryTreeNode(lastParent.rightChild.element);

                newRemove = newParent.rightChild;
                lastRemove = lastParent.rightChild;
            }

            //Traverse to remove node
            if (path.size() > 1)
                for (int i = 1; i < path.size(); i++)
                {
                    boolean direction = path.get(i);

                    lastParent = lastRemove;
                    newParent = newRemove;

                    if (!direction)
                    {
                        newParent.rightChild = lastParent.rightChild;
                        newParent.leftChild = new BinaryTreeNode(lastParent.leftChild.element);

                        newRemove = newParent.leftChild;
                        lastRemove = lastParent.leftChild;

                    }
                    else
                    {
                        newParent.leftChild = lastParent.leftChild;
                        newParent.rightChild = new BinaryTreeNode(lastParent.rightChild.element);

                        newRemove = newParent.rightChild;
                        lastRemove = lastParent.rightChild;
                    }
                }
        }

        //if false, remove node is left child of parent node
        //if true, remove node is right child of parent node
        boolean whichChild = false;
        boolean removeRoot = false; //are we removing root?

        if (path.isEmpty())
            removeRoot = true;
        else //if not removing root
            whichChild = path.get(path.size() - 1);

        //if remove node has no children
        if (lastRemove.leftChild == null && lastRemove.rightChild == null)
            if (removeRoot)
                newRoot = null;
            else if (!whichChild)
                newParent.leftChild = null;
            else
                newParent.rightChild = null;
        //if remove node has one children
        else if (lastRemove.leftChild == null && lastRemove.rightChild != null)
            if (removeRoot)
                newRoot = lastRoot.rightChild;
            else if (!whichChild)
                newParent.leftChild = lastRemove.rightChild;
            else
                newParent.rightChild = lastRemove.rightChild;
        else if (lastRemove.leftChild != null && lastRemove.rightChild == null)
            if (removeRoot)
                newRoot = lastRoot.leftChild;
            else if (!whichChild)
                newParent.leftChild = lastRemove.leftChild;
            else
                newParent.rightChild = lastRemove.leftChild;
        //if remove node has two children
        else
        {
            BinaryTreeNode lastReplaceParent = lastRemove;
            BinaryTreeNode lastReplaceRemove = lastReplaceParent.rightChild;

            BinaryTreeNode newReplaceParent = newRemove;
            BinaryTreeNode newReplaceRemove = new BinaryTreeNode(lastReplaceParent.rightChild.element);

            newReplaceParent.leftChild = lastReplaceParent.leftChild;
            newReplaceParent.rightChild = newReplaceRemove;
            newReplaceRemove.rightChild = lastReplaceRemove.rightChild;

            //if right child of remove node has no left child
            if (replacePath.isEmpty())
            {
                newReplaceRemove.leftChild = lastReplaceParent.leftChild;

                if (removeRoot)
                    newRoot = newReplaceRemove;
                else if (!whichChild)
                    newParent.leftChild = newReplaceRemove;
                else
                    newParent.rightChild = newReplaceRemove;
            }
            //iterate to successor node
            else
            {
                for (boolean left : replacePath)
                {
                    lastReplaceParent = lastReplaceRemove;
                    newReplaceParent = newReplaceRemove;

                    newReplaceParent.rightChild = lastReplaceParent.rightChild;
                    newReplaceParent.leftChild = new BinaryTreeNode(lastReplaceParent.leftChild.element);

                    newReplaceRemove = newReplaceParent.leftChild;
                    lastReplaceRemove = lastReplaceParent.leftChild;
                }

                newReplaceParent.leftChild = lastReplaceRemove.rightChild;
                newReplaceRemove.leftChild = newRemove.leftChild;
                newReplaceRemove.rightChild = newRemove.rightChild;

                if (removeRoot)
                    newRoot = newReplaceRemove;
                else if (!whichChild)
                    newParent.leftChild = newReplaceRemove;
                else
                    newParent.rightChild = newReplaceRemove;
            }
        }

        versions.add(newRoot);
    }

    public List<BinaryTreeNode> getVersions()
    {
        return versions;
    }
}
