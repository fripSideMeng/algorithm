package mhz.algo.rbtree;

public class RbTreeNode {
    public int data;
    public boolean red;
    public RbTreeNode left;
    public RbTreeNode right;
    public RbTreeNode parent;

    // Create a single new node with red mark
    public RbTreeNode(int data) {
        this(data, true, null, null, null);
    }

    // Create a new node with given positions with red mark
    public RbTreeNode(int data, boolean red, RbTreeNode left, RbTreeNode right, RbTreeNode parent) {
        this.data = data;
        this.red = red;
        this.left = left;
        this.right = right;
        this.parent = parent;
    }
}
