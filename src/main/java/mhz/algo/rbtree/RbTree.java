package mhz.algo.rbtree;

import java.util.NoSuchElementException;

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

    public boolean containsHelper(RbTreeNode root, int data) {
        if (root == null) {
            return false;
        }
        if (root.data == data) {
            return true;
        } else if (root.data > data) {
            return containsHelper(root.left, data);
        } else {
            return containsHelper(root.right, data);
        }
    }

    public void insert(int data) {
        if (this.contains(data)) {
            throw new IllegalArgumentException("Node is duplicate or empty");
        }
        overallRoot = insertHelper(overallRoot, data);
        size++;
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

    private void repaintInsertion(RbTreeNode node) {
        if (!node.red || node.parent == null || node.parent.parent == null) {
            return;
        }
        if (node.parent.red) {
            RbTreeNode uncle = node.parent.equals(node.parent.parent.left)
                                ? node.parent.parent.right : node.parent.parent.left;
            if (uncle != null && uncle.red) {
                node.parent.red = false;
                uncle.red = false;// Paint grandfather to red
                // Root reached, repaint to black
                node.parent.parent.red = node.parent.parent.parent != null;
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
                    if (tmp.left != null) {
                        tmp.left.parent = tmp;
                    }
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
                    if (tmp.right != null) {
                        tmp.right.parent = tmp;
                    }
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
                    if (tmp.left != null) {
                        tmp.left.parent = tmp;
                    }
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
                    if (tmp.right != null) {
                        tmp.right.parent = tmp;
                    }
                    node.parent.left = tmp;
                    tmp.parent = node.parent; // Rotation finished
                    node.parent.red = false;
                    node.parent.left.red = true; // Repaint finished for the original grandparent
                } else if (node.parent.equals(node.parent.parent.right)
                            && node.equals(node.parent.left)) { // Right-left case
                    RbTreeNode tmp = node.parent;
                    node.parent.parent.right = node;
                    tmp.left = node.right;
                    if (tmp.left != null) {
                        tmp.left.parent = tmp;
                    }
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
                    if (tmp.right != null) {
                        tmp.right.parent = tmp;
                    }
                    node.left = tmp;
                    tmp.parent = node; // Rotation finished
                    node.left.red = true; // Repaint finished for the original grandparent
                }
            }
        }
    }

    public void delete(int data) {
        if (!this.contains(data)) {
            throw new NoSuchElementException("No such data found");
        }
        overallRoot = deleteHelper(overallRoot, data);
        size--;
        if (overallRoot != null) {
            overallRoot.red = false;
        }
    }

    private RbTreeNode deleteHelper(RbTreeNode node, int data) {
        RbTreeNode deleted;
        RbTreeNode deletedChild = null;
        while (true) {
            if (node.data == data) {
                deleted = node;
                if (node.left == node.right) { // Leaf node reached
                    if (node.parent == null) { // Root is the deleted node
                        return null;
                    }
                    if (node.equals(node.parent.left)) {
                        node.parent.left = null;
                    } else {
                        node.parent.right = null;
                    }
                } else if (node.left == null) { // Has right child only
                    if (node.parent == null) { // Root is the deleted node
                        return node.right;
                    }
                    if (node.equals(node.parent.left)) {
                        node.parent.left = node.right;
                    } else {
                        node.parent.right = node.right;
                    }
                    node.right.parent = node.parent;
                    deletedChild = node.right;
                } else if (node.right == null) { // Has left child only
                    if (node.parent == null) { // Root is the deleted node
                        return node.left;
                    }
                    if (node.equals(node.parent.left)) {
                        node.parent.left = node.left;
                    } else {
                        node.parent.right = node.left;
                    }
                    node.left.parent = node.parent;
                    deletedChild = node.left;
                } else { // Both children not empty
                    RbTreeNode curr = node.right;
                    // Traverse to the leftmost node of right child
                    while (curr.left != null) {
                        curr = curr.left;
                    }
                    node.data = curr.data;
                    deleted = curr;
                    deletedChild = curr;
                    if (node.right.equals(curr)) { // Prevent self-loop condition
                        node.right = curr.right;
                        if (node.right != null) {
                            node.right.parent = node;
                        }
                    } else {
                        curr.parent.left = curr.right;
                        if (curr.parent.left != null) {
                            curr.parent.left = curr.parent;
                        }
                    }
                }
                break;
            } else if (node.data > data) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        RbTreeNode repaintStart;
        boolean oneRed = node.red || (deletedChild != null && deletedChild.red);
        if (oneRed) {
            repaintStart = deletedChild == null ? deleted.parent : deletedChild;
            if (deletedChild != null) {
                repaintStart.red = false; // Mark as black
            }
        } else { // Both black
            repaintStart = repaintDeletion(deleted);
        }
        while (repaintStart.parent != null) {
            repaintStart = repaintStart.parent;
        }
        return repaintStart;
    }

    private RbTreeNode repaintDeletion (RbTreeNode deleted) {
        while (deleted.parent != null && !deleted.red) {
            RbTreeNode sibling = getDeletedSibling(deleted);
            if (!sibling.red) {
                boolean notAllBlack = (sibling.left != null && sibling.left.red)
                                        || (sibling.right != null && sibling.right.red);
                if (!notAllBlack) { // Sibling and its children all black
                    sibling.red = true;
                    if (sibling.parent.red) {
                        sibling.parent.red = false;
                        return sibling;
                    }
                } else { // At least one child of sibling is red
                    RbTreeNode oldParent = sibling.parent;
                    boolean oldParentIsRed = oldParent.red; // Prevent changes in black height
                    boolean oldParentFromLeft = false;
                    if (oldParent.parent != null) {
                        oldParentFromLeft = oldParent.equals(oldParent.parent.left);
                    }
                    if (sibling.equals(sibling.parent.left)) {
                        if (sibling.left != null && sibling.left.red) { // Left-left case
                            sibling.left.red = false;
                            sibling.parent = oldParent.parent;
                            if (sibling.parent != null) {
                                if (oldParentFromLeft) {
                                    sibling.parent.left = sibling;
                                } else {
                                    sibling.parent.right = sibling;
                                }
                            }
                            oldParent.parent = sibling;
                            oldParent.left = sibling.right;
                            sibling.right = oldParent;
                            sibling.right.red = false;
                            if (sibling.right.left != null) {
                                sibling.right.left.parent = oldParent;
                            }
                            sibling.red = oldParentIsRed;
                            return sibling;
                        } else { // Left-right case
                            oldParent.left = sibling.right;
                            sibling.right.parent = oldParent;
                            sibling.right = sibling.right.left;
                            if (sibling.right != null) {
                                sibling.right.parent = sibling;
                            }
                            oldParent.left.left = sibling;
                            sibling.parent = oldParent.left; // First rotation finished
                            RbTreeNode newParent = oldParent.left;
                            newParent.parent = oldParent.parent;
                            if (oldParent.parent != null) {
                                if (oldParentFromLeft) {
                                    newParent.parent.left = newParent;
                                } else {
                                    newParent.parent.right = newParent;
                                }
                            }
                            oldParent.parent = newParent;
                            oldParent.left = newParent.right;
                            oldParent.red = false;
                            newParent.right = oldParent;
                            if (newParent.right.left != null) {
                                newParent.right.left.parent = newParent.right;
                            }
                            newParent.red = oldParentIsRed;
                            return newParent;
                        }
                    } else {
                        if (sibling.right != null && sibling.right.red) { // Right-right case
                            sibling.right.red = false;
                            sibling.parent = oldParent.parent;
                            if (sibling.parent != null) {
                                if (oldParentFromLeft) {
                                    sibling.parent.left = sibling;
                                } else {
                                    sibling.parent.right = sibling;
                                }
                            }
                            oldParent.parent = sibling;
                            oldParent.right = sibling.left;
                            sibling.left = oldParent;
                            sibling.left.red = false;
                            if (sibling.left.right != null) {
                                sibling.left.right.parent = oldParent;
                            }
                            sibling.red = oldParentIsRed;
                            return sibling;
                        } else { // Right-left case
                            oldParent.right = sibling.left;
                            sibling.left.parent = oldParent;
                            sibling.left = sibling.left.right;
                            if (sibling.left != null) {
                                sibling.left.parent = sibling;
                            }
                            oldParent.right.right = sibling;
                            sibling.parent = oldParent.right; // First rotation finished
                            RbTreeNode newParent = oldParent.right;
                            newParent.parent = oldParent.parent;
                            if (newParent.parent != null) {
                                if (oldParentFromLeft) {
                                    newParent.parent.left = newParent;
                                } else {
                                    newParent.parent.right = newParent;
                                }
                            }
                            oldParent.parent = newParent;
                            oldParent.right = newParent.left;
                            oldParent.red = false;
                            newParent.left = oldParent;
                            if (newParent.left.right != null) {
                                newParent.left.right.parent = newParent.left;
                            }
                            newParent.red = oldParentIsRed;
                            return newParent;
                        }
                    }
                }
            } else { // Sibling is red
                sibling.red = false;
                RbTreeNode oldParent = sibling.parent;
                if (sibling.equals(oldParent.left)) { // Left case
                    sibling.parent = oldParent.parent;
                    if (sibling.parent != null) {
                        if (oldParent.equals(sibling.parent.left)) {
                            sibling.parent.left = sibling;
                        } else {
                            sibling.parent.right = sibling;
                        }
                    }
                    oldParent.parent = sibling;
                    oldParent.left = sibling.right;
                    if (oldParent.left != null) {
                        oldParent.left.red = true;
                        oldParent.left.parent = oldParent;
                    }
                } else { // Right case
                    sibling.parent = oldParent.parent;
                    if (sibling.parent != null) {
                        if (oldParent.equals(sibling.parent.left)) {
                            sibling.parent.left = sibling;
                        } else {
                            sibling.parent.right = sibling;
                        }
                    }
                    oldParent.parent = sibling;
                    oldParent.right = sibling.left;
                    if (oldParent.right != null) {
                        oldParent.right.red = true;
                        oldParent.right.parent = oldParent;
                    }
                }
                return sibling;
            }
            deleted = deleted.parent;
        }
        return deleted;
    }

    private RbTreeNode getDeletedSibling(RbTreeNode deleted) {
        if (deleted.parent.left == null) {
            return deleted.parent.right;
        }
        return deleted.equals(deleted.parent.left)
                ? deleted.parent.right : deleted.parent.left;
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
