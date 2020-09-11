package mhz.algo.rbtree;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class RbTree {
    private RbTreeNode overallRoot;
    private Set<Integer> inserted;

    public int size() {
        return inserted.size();
    }

    public RbTreeNode getOverallRoot() {
        return this.overallRoot;
    }

    public RbTree () {
        this(null);
    }

    public RbTree (RbTreeNode root) {
        this.inserted = new HashSet<>();
        this.overallRoot = root;
    }

    public boolean contains(int data) {
        return inserted.contains(data);
    }

    public void insert(int data) {
        if (this.contains(data)) {
            throw new IllegalArgumentException("Node is duplicate or empty");
        }
        overallRoot = insertHelper(overallRoot, data);
        inserted.add(data);
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
                node.parent.parent.red = true; // Paint grandfather to red
                if (node.parent.parent.parent == null) { // Root reached, repaint to black
                    node.parent.parent.red = false;
                }
                repaintInsertion(node.parent.parent); // Recursively repaint grandfather
            } else { // Uncle is black
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

    public void delete(int data) {
        if (!this.contains(data)) {
            throw new NoSuchElementException("No such data found");
        }
        overallRoot = deleteHelper(overallRoot, data);
        inserted.remove(data);
    }

    private RbTreeNode deleteHelper(RbTreeNode node, int data) {
        if (node == null) {
            return null;
        } else {
            RbTreeNode deleted;
            while (true) {
                if (node.data == data) {
                    if (node.left == node.right) {
                        if (node.equals(node.parent.left)) {
                            node.parent.left = null;
                        } else {
                            node.parent.right = null;
                        }
                    }
                    break;
                } else if (node.data > data) {
                    node = node.left;
                } else {
                    node = node.right;
                }
            }
        }
        return node;
    }

    @Override
    public String toString() {
        return toString(overallRoot);
    }

    private String toString(RbTreeNode node) {
        if (node == null) {
            return "";
        } else {
            return toString(node.left) + " " + node.data + (node.red ? ": red" : ": black") + " " + toString(node.right);
        }
    }
}
