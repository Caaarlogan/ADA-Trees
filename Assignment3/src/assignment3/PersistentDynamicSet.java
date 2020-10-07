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
    protected void newAdd(List<Boolean> path, E o)
    {
        if (path.isEmpty())
        {
            BinaryTreeNode root = new BinaryTreeNode(o);
            versions.add(root);
        } else
        {
            //Get root of last version
            BinaryTreeNode currentLast = versions.get(versions.size() - 1);

            //Create new root and current node
            BinaryTreeNode newRoot = new BinaryTreeNode(currentLast.element);
            BinaryTreeNode currentNew = newRoot;

            //Traverse through path, add new element when child node is null
            for (boolean direction : path)
            {
                if (!direction)
                {
                    currentNew.rightChild = currentLast.rightChild;

                    if (currentLast.leftChild != null)
                    {
                        currentNew.leftChild = new BinaryTreeNode(currentLast.leftChild.element);
                    } else
                    {
                        currentNew.leftChild = new BinaryTreeNode(o);
                    }

                    currentLast = currentLast.leftChild;
                    currentNew = currentNew.leftChild;
                } else
                {
                    currentNew.leftChild = currentLast.leftChild;

                    if (currentLast.rightChild != null)
                    {
                        currentNew.rightChild = new BinaryTreeNode(currentLast.rightChild.element);
                    } else
                    {
                        currentNew.rightChild = new BinaryTreeNode(o);
                    }

                    currentLast = currentLast.rightChild;
                    currentNew = currentNew.rightChild;
                }
            }

            versions.add(newRoot);
        }
    }

    @Override
    protected void newRemove(List<Boolean> path, Object o)
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

            } else
            {
                newParent.leftChild = lastParent.leftChild;
                newParent.rightChild = new BinaryTreeNode(lastParent.rightChild.element);

                newRemove = newParent.rightChild;
                lastRemove = lastParent.rightChild;
            }

            //Traverse to remove node
            if (path.size() > 1)
            {
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

                    } else
                    {
                        newParent.leftChild = lastParent.leftChild;
                        newParent.rightChild = new BinaryTreeNode(lastParent.rightChild.element);

                        newRemove = newParent.rightChild;
                        lastRemove = lastParent.rightChild;
                    }
                }
            }
        }

        //if false, remove node is parent node left child
        //if true, remove node is parent node right child
        boolean whichChild = path.get(path.size() - 1);

        if (lastRemove.leftChild == null && lastRemove.rightChild == null)
        {
            if (!whichChild)
                newParent.leftChild = null;
            else
                newParent.rightChild = null;
        } else if (lastRemove.leftChild == null && lastRemove.rightChild != null)
        {
            if (!whichChild)
                newParent.leftChild = lastRemove.rightChild;
            else
                newParent.rightChild = lastRemove.rightChild;
        } else if (lastRemove.leftChild != null && lastRemove.rightChild == null)
        {
            if (!whichChild)
                newParent.leftChild = lastRemove.leftChild;
            else
                newParent.rightChild = lastRemove.leftChild;
        } else
        {

        }

        versions.add(newRoot);
    }

    public List<BinaryTreeNode> getVersions()
    {
        return versions;
    }
}
