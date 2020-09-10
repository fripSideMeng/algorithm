package mhz.algo.rbtree;

import java.util.HashSet;
import java.util.Set;

public class RbTree {
    private Set<Integer> inserted;
    private RbTreeNode overallRoot;

    public RbTree() {
        inserted = new HashSet<>();
    }

    public int size() {
        return inserted.size();
    }

    public boolean contains(RbTreeNode node) {
        return inserted.contains(node.data);
    }

    public void insert(RbTreeNode node) {
        if (node == null || this.contains(node)) {
            throw new IllegalArgumentException("Node is duplicate or empty");
        }
        if (inserted.size() == 0) {
            overallRoot = new RbTreeNode(node.data);
            overallRoot.red = false; // Always paint root as black
        } else {
            insertHelper(node); // Do a normal BST insertion
            repaintInsertion(node); // Restore RB tree invariant
        }
        inserted.add(node.data);
    }

    private void insertHelper(RbTreeNode node) {
        RbTreeNode curr = overallRoot;
        while (curr != null) {
            if (curr.data < node.data) {
                if (curr.right == null) {
                    node.parent = curr;
                    curr.right = node;
                    return;
                } else {
                    curr = curr.right;
                }
            } else {
                if (curr.left == null) {
                    node.parent = curr;
                    curr.left = node;
                    return;
                } else {
                    curr = curr.left;
                }
            }
        }
    }

    private void repaintInsertion(RbTreeNode node) {
        if (node == null || node.parent == null || node.parent.parent == null) {
            return;
        }
        if (node.parent.red) {
            RbTreeNode uncle = node.parent.equals(node.parent.parent.left)
                                ? node.parent.parent.right : node.parent.parent.left;
            if (uncle != null) {
                if (uncle.red) {
                    node.parent.red = false;
                    uncle.red = false;
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
                        node.parent.right = tmp; // Rotation finished
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
                        node.left.red = true;
                        node.right.red = true; // Repaint finished for the original grandparent
                    }
                }
            } else {
                node.parent.red = false;
            }
        }
    }
}
