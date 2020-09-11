package mhz.algo.rbtree;

public class RbTree {
    private RbTreeNode overallRoot;
    private int size;

    public int size() {
        return this.size;
    }

    public RbTreeNode getOverallRoot() {
        return this.overallRoot;
    }

    public RbTree () {
        this(null);
    }

    public RbTree (RbTreeNode root) {
        this.overallRoot = root;
    }

    public boolean contains(int data) {
        return containsHelper(overallRoot, data);
    }

    private boolean containsHelper(RbTreeNode root, int data) {
        if (root != null) {
            if (root.data == data) {
                return true;
            } else if (root.data > data) {
                return containsHelper(root.left, data);
            } else {
                return containsHelper(root.right, data);
            }
        }
        return false;
    }

    public void insert(int data) {
        if (this.contains(data)) {
            throw new IllegalArgumentException("Node is duplicate or empty");
        }
        overallRoot = insertHelper(overallRoot, data);
        size++;
    }

    private RbTreeNode insertHelper(RbTreeNode node, int data) {
        RbTreeNode newNode;
        if (node == null) {
            return new RbTreeNode(data);
        } else {
            while (true) {
                if (node.data > data) {
                    if (node.left == null) {
                        newNode = new RbTreeNode(data);
                        node.left = newNode;
                        newNode.parent = node;
                        break;
                    } else {
                        node = node.left;
                    }
                } else {
                    if (node.right == null) {
                        newNode = new RbTreeNode(data);
                        node.right = newNode;
                        newNode.parent = node;
                        break;
                    } else {
                        node = node.right;
                    }
                }
            }
            while (newNode.parent != null) {
                newNode = repaintInsertion(newNode).parent;
            }
            return newNode;
        }
    }

    private RbTreeNode repaintInsertion(RbTreeNode node) {
        if (!node.red || node.parent == null || node.parent.parent == null) {
            return node;
        }
        if (node.parent.red) {
            RbTreeNode uncle = node.parent.equals(node.parent.parent.left)
                                ? node.parent.parent.right : node.parent.parent.left;
            if (uncle != null && uncle.red) {
                node.parent.red = false;
                uncle.red = false;
                node.parent.parent = repaintInsertion(node.parent.parent);// Recursively repaint grandfather
            } else { // Uncle is black or empty
                if (node.parent.equals(node.parent.parent.left)
                        && node.equals(node.parent.left)) { // Left-left case
                    RbTreeNode tmp = node.parent.parent;
                    node.parent.parent = node.parent.parent.parent;
                    if (node.parent.parent != null) {
                        if (tmp.equals(node.parent.parent.left)) {
                            node.parent.parent.left = node.parent;
                        } else if (tmp.equals(node.parent.parent.right)) {
                            node.parent.parent.right = node.parent;
                        }
                    }
                    tmp.left = node.parent.right;
                    node.parent.right = tmp;
                    tmp.parent = node.parent; // Rotation finished
                    node.parent.red = false;
                    node.parent.right.red = true; // Repaint finished for the original grandparent
                } else if (node.parent.equals(node.parent.parent.left)
                       && node.equals((node.parent.right))) { // Left-right case
                    RbTreeNode tmp = node.parent;
                    node.parent.parent.left = node;
                    node.parent = node.parent.parent;
                    tmp.right = node.left;
                    node.left = tmp;
                    tmp.parent = node; // Finished left rotation of parent node
                    tmp = node.parent;
                    node.parent = node.parent.parent;
                    if (node.parent != null) {
                        if (tmp.equals(node.parent.left)) {
                            node.parent.left = node;
                        } else if (tmp.equals(node.parent.right)) {
                            node.parent.right = node;
                        }
                    }
                    tmp.left = node.right;
                    node.right = tmp;
                    tmp.parent = node; // Rotation finished
                    node.right.red = true; // Repaint finished for the original grandparent
                } else if (node.parent.equals(node.parent.parent.right)
                        && node.equals(node.parent.right)) { // Right-right case
                    RbTreeNode tmp = node.parent.parent;
                    node.parent.parent = node.parent.parent.parent;
                    if (node.parent.parent != null) {
                        if (tmp.equals(node.parent.parent.left)) {
                            node.parent.parent.left = node.parent;
                        } else if (tmp.equals(node.parent.parent.right)) {
                            node.parent.parent.right = node.parent;
                        }
                    }
                    tmp.right = node.parent.left;
                    node.parent.left = tmp;
                    tmp.parent = node.parent; // Rotation finished
                    node.parent.red = false;
                    node.parent.left.red = true; // Repaint finished for the original grandparent
                } else if (node.parent.equals(node.parent.parent.right)
                            && node.equals(node.parent.left)) { // Right-left case
                    RbTreeNode tmp = node.parent;
                    node.parent.parent.right = node;
                    tmp.left = node.right;
                    node.right = tmp;
                    tmp.parent = node; // Finished rotation for parent node
                    tmp = node.parent;
                    node.parent = node.parent.parent;
                    if (node.parent != null) {
                        if (tmp.equals(node.parent.left)) {
                            node.parent.left = node.parent;
                        } else if (tmp.equals(node.parent.parent.right)) {
                            node.parent.right = node.parent;
                        }
                    }
                    tmp.right = node.left;
                    node.left = tmp;
                    tmp.parent = node; // Rotation finished
                    node.left.red = true; // Repaint finished for the original grandparent
                }
            }
        }
        return node;
    }
}
