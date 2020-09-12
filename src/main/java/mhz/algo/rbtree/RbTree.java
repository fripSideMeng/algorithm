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
        overallRoot.red = false;
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
            repaintInsertion(newNode);
            while (newNode.parent != null) {
                newNode = newNode.parent;
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
        overallRoot.red = false;
    }

    private RbTreeNode deleteHelper(RbTreeNode node, int data) {
        RbTreeNode deleted;
        RbTreeNode deletedChild = null;
        RbTreeNode sibling = null;
        while (true) {
            if (node.data == data) {
                deleted = node;
                if (node.left == node.right) { // Leaf node reached
                    if (node.parent == null) { // Root is the deleted node
                        return null;
                    }
                    if (node.equals(node.parent.left)) {
                        node.parent.left = null;
                        sibling = node.parent.right;
                    } else {
                        node.parent.right = null;
                        sibling = node.parent.left;
                    }
                } else if (node.left == null) { // Has right child only
                    if (node.parent == null) { // Root is the deleted node
                        return node.right;
                    }
                    if (node.equals(node.parent.left)) {
                        node.parent.left = node.right;
                        sibling = node.parent.right;
                    } else {
                        node.parent.right = node.right;
                        sibling = node.parent.left;
                    }
                    node.right.parent = node.parent;
                    deletedChild = node.right;
                } else if (node.right == null) { // Has left child only
                    if (node.parent == null) { // Root is the deleted node
                        return node.left;
                    }
                    if (node.equals(node.parent.left)) {
                        node.parent.left = node.left;
                        sibling = node.parent.right;
                    } else {
                        node.parent.right = node.left;
                        sibling = node.parent.left;
                    }
                    node.left.parent = node.parent;
                    deletedChild = node.left;
                } else { // Both children not empty
                    boolean fromLeft = false;
                    if (node.parent != null) {
                        fromLeft = node.equals(node.parent.left);
                    }
                    RbTreeNode curr = node.right;
                    // Traverse to the leftmost node of right child
                    while (curr.left != null) {
                        curr = curr.left;
                    }
                    RbTreeNode oldParent = curr.parent;
                    curr.parent = node.parent;
                    curr.left = node.left;
                    curr.left.parent = curr;
                    oldParent.left = curr.right; // Always coming from left
                    // Prevent self loop condition
                    curr.right = node.right.equals(curr) ? null : node.right;
                    if (node.parent != null) {
                        if (fromLeft) {
                            node.parent.left = curr;
                            sibling = node.parent.right;
                        } else {
                            node.parent.right = curr;
                            sibling = node.parent.left;
                        }
                    } else {
                        return curr;
                    }
                    deletedChild = curr;
                }
                break;
            } else if (node.data > data) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        RbTreeNode repaintStart;
        boolean oneRed = (deleted.red && deletedChild == null)
                || (deletedChild != null && (deleted.red || deletedChild.red));
        if (oneRed) {
            repaintStart = deletedChild == null ? deleted.parent : deletedChild;
            if (deletedChild != null) {
                repaintStart.red = false; // Mark as black
            }
            while (repaintStart.parent != null) {
                repaintStart = repaintStart.parent;
            }
        } else { // Both black
            repaintStart = repaintDeletion(sibling);
            while (repaintStart.parent != null) {
                repaintStart = repaintStart.parent;
            }
        }
        return repaintStart;
    }

    private RbTreeNode repaintDeletion (RbTreeNode sibling) {
        if (sibling.red) {
            sibling.red = false;
            RbTreeNode oldParent = sibling.parent;
            if (sibling.equals(oldParent.left)) { // Left case
                if (oldParent.parent == null) {
                    sibling.parent = null;
                    oldParent.left = sibling.right;
                    sibling.right = oldParent;
                    oldParent.parent = sibling;
                } else { // Sibling's grandparent is not root
                    boolean parentFromLeft = oldParent.equals(oldParent.parent.left);
                    sibling.parent = oldParent.parent;
                    oldParent.left = sibling.right;
                    sibling.right = oldParent;
                    oldParent.parent = sibling;
                    if (parentFromLeft) {
                        sibling.parent.left = sibling;
                    } else {
                        sibling.parent.right = sibling;
                    }
                }
            } else { // Right case
                if (oldParent.parent == null) {
                    sibling.parent = null;
                    oldParent.right = sibling.left;
                    sibling.left = oldParent;
                    oldParent.parent = sibling;
                    oldParent.right.red = true;
                } else { // Sibling's grandparent is not root
                    boolean parentFromLeft = oldParent.equals(oldParent.parent.left);
                    sibling.parent = sibling.parent.parent;
                    oldParent.right = sibling.left;
                    sibling.left = oldParent;
                    oldParent.parent = sibling;
                    oldParent.right.red = true;
                    if (parentFromLeft) {
                        sibling.parent.left = sibling;
                    } else {
                        sibling.parent.right = sibling;
                    }
                }
            }
        }
        return sibling;
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
