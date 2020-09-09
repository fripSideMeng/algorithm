package mhz.algo.rbtree;

class RbTreeNode<T> {
    public final T data;
    public boolean red;
    public RbTreeNode left;
    public RbTreeNode right;
    public RbTreeNode parent;

    // Create a single new node with red mark
    RbTreeNode(T data) {
        this.data = data;
        this.red = true;
        this.parent = null;
        this.left = null;
        this.right = null;
    }

    // Create a new node with given parent with red mark
    RbTreeNode(T data, RbTreeNode parent) {
        this.data = data;
        this.red = true;
        this.parent = parent;
    }

    // Create a new node with given positions with red mark
    RbTreeNode(T data, RbTreeNode left, RbTreeNode right, RbTreeNode parent) {
        this.data = data;
        this.red = true;
        this.left = left;
        this.right = right;
        this.parent = parent;
    }
}
