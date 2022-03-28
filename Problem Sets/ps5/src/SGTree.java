import com.sun.source.tree.Tree;

/**
 * ScapeGoat Tree class
 *
 * This class contains some of the basic code for implementing a ScapeGoat tree.
 * This version does not include any of the functionality for choosing which node
 * to scapegoat.  It includes only code for inserting a node, and the code for rebuilding
 * a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     *
     * This class holds the data for a node in a binary tree.
     *
     * Note: we have made things public here to facilitate problem set grading/testing.
     * In general, making everything public like this is a bad idea!
     *
     */
    public static class TreeNode {
        int key;
        int weight;
        public TreeNode left = null;
        public TreeNode right = null;

        TreeNode(int k) {
            key = k;
        }

        @Override
        public String toString() {
            return String.format("{ key: %s, weight: %s}", key, weight);
        }
    }

    // Root of the binary tree
    public TreeNode root = null;
    /**
     * Helper for countNodes
     */
    public int getLength(TreeNode node) {
        if (node == null)
            return 0;

        int leftLength = getLength(node.left);
        int rightLength = getLength(node.right);
        return 1 + leftLength + rightLength;
    }
    /**
     * Counts the number of nodes in the specified subtree
     *
     * @param node  the parent node, not to be counted
     * @param child the specified subtree
     * @return number of nodes
     */
    public int countNodes(TreeNode node, Child child) {
        if (child == Child.RIGHT)
            return getLength(node.right);
        if (child == Child.LEFT)
            return getLength(node.left);
        return 0;
    }

    /**
     * Helper for enumerateNode
     */
    public int inOrderTraverse(TreeNode[] nodeArray, TreeNode node, int pointer) {
        if (node == null)
            return pointer;

        pointer = inOrderTraverse(nodeArray, node.left, pointer);
        nodeArray[pointer++] = node;
        pointer = inOrderTraverse(nodeArray, node.right, pointer);

        return pointer;
    }
    /**
     * Builds an array of nodes in the specified subtree
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    public TreeNode[] enumerateNodes(TreeNode node, Child child) {
        int treeLength = countNodes(node, child);
        TreeNode[] nodeArray = new TreeNode[treeLength];

        if (child == Child.LEFT)
            inOrderTraverse(nodeArray, node.left, 0);
        if (child == Child.RIGHT)
            inOrderTraverse(nodeArray, node.right, 0);

        return nodeArray;
    }

    public int getWeight(TreeNode node) {
        if(node == null)
            return 0;
        return node.weight;
    }
    /**
     * Helper for buildTree
     */
    public TreeNode buildTreeHelper(TreeNode[] nodeList, int low, int high) {
        int mid = low + (high - low)/2;
        if (low > high)
            return null;

        TreeNode node = nodeList[mid];
        node.weight = -1;

        node.left = buildTreeHelper(nodeList, low, mid - 1);
        node.right = buildTreeHelper(nodeList, mid + 1, high);

        node.weight = getWeight(node.left) + getWeight(node.right) + 1;
        return node;
    }
    /**
     * Builds a tree from the list of nodes
     * Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    public TreeNode buildTree(TreeNode[] nodeList) {
        int low = 0;
        int high = nodeList.length - 1;
        TreeNode newTreeNodes = buildTreeHelper(nodeList, low, high);
        return newTreeNodes;
    }

    /**
     * Determines if a node is balanced.  If the node is balanced, this should return true.  Otherwise, it should return false.
     * A node is unbalanced if either of its children has weight greather than 2/3 of its weight.
     *
     * @param node a node to check balance on
     * @return true if the node is balanced, false otherwise
     */
    public boolean checkBalance(TreeNode node) {
        if (node == null)
            return true;

        double totalWeight = getWeight(node);
        double totalWeightModified = (2.0/3.0) * totalWeight;
        if (getWeight(node.left) > totalWeightModified || getWeight(node.right) > totalWeightModified)
            return false;

        return true;
    }


    /**
     * Rebuilds the specified subtree of a node
     *
     * @param node the part of the subtree to rebuild
     * @param child specifies which child is the root of the subtree to rebuild
     */
    public void rebuild(TreeNode node, Child child) {
        // Error checking: cannot rebuild null tree
        if (node == null) return;
        // First, retrieve a list of all the nodes of the subtree rooted at child
        TreeNode[] nodeList = enumerateNodes(node, child);
        // Then, build a new subtree from that list
        TreeNode newChild = buildTree(nodeList);
        // Finally, replace the specified child with the new subtree
        if (child == Child.LEFT) {
            node.left = newChild;
        } else if (child == Child.RIGHT) {
            node.right = newChild;
        }
    }

    /**
     * Create a Tree Leaf node.
     *
     * @param key  The key you want to insert
     */
    public TreeNode createLeaf(int key) {
        TreeNode leaf = new TreeNode(key);
        leaf.weight = 1;
        return leaf;
    }
    /**
     * Insert a key into the tree Helper.
     *
     * @param key  The key you want to insert
     * @param node  The current node you are on
     */
    public void insertHelper(int key, TreeNode node) {
        if (node == null) {
            root = createLeaf(key);
            return;
        }

        if (key <= node.key) {
            if (node.left == null) {
                node.left = createLeaf(key);
                node.weight++;
                return;
            }
            insertHelper(key, node.left);
        }

        if (key > node.key) {
            if (node.right == null) {
                node.right = createLeaf(key);
                node.weight++;
                return;
            }
            insertHelper(key, node.right);
        }
        node.weight++;

        if (!checkBalance(node.left))
            rebuild(node, Child.LEFT);

        if (!checkBalance(node.right))
            rebuild(node, Child.RIGHT);

        return;
    }

    /**
     * Insert a key into the tree
     *
     * @param key the key to insert
     */
    public void insert(int key) {
        insertHelper(key, this.root);
    }

    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 10; i++) {
            tree.insert(i);
        }
        // tree.rebuild(tree.root, Child.RIGHT);
        // System.out.println(tree.root.right);
        // System.out.println(tree.checkBalance(tree.root.right));
    }
}
