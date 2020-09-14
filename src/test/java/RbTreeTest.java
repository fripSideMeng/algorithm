import mhz.algo.rbtree.RbTree;
import mhz.algo.rbtree.RbTreeNode;
import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import static org.junit.Assert.*;

public class RbTreeTest {
    protected RbTree createTree() {
        return new RbTree();
    }
    protected void checkInvariantRb(@org.jetbrains.annotations.NotNull RbTree tree) {
        RbTreeNode root = tree.getOverallRoot();
        if (!noSelfLoop(root)) {
            fail("Self loop detected\n");
        }
        if (checkBlackHeight(root) == -1) {
            fail("Black nodes invariant broken\n");
        }
        if (!nonConsecutiveRed(root)) {
            fail("Consecutive red nodes detected\n");
        }
    }
    protected int checkBlackHeight(RbTreeNode node) {
        if (node == null) {
            return 0;
        }
        int leftBlackHeight = checkBlackHeight(node.left);
        int rightBlackHeight = checkBlackHeight(node.right);
        if (rightBlackHeight == -1
                || leftBlackHeight != rightBlackHeight) {
            return -1;
        } else {
            return leftBlackHeight + (node.red ? 0 : 1);
        }
    }
    protected boolean nonConsecutiveRed(RbTreeNode node) {
        if (node == null) {
            return true;
        } else {
            if (node.red) {
                if ((node.left != null && node.left.red)
                    || (node.right != null && node.right.red)) { // Consecutive red detected
                    System.out.println(node.right.data);
                    System.out.println(node.data);
                    System.out.println(node.red);
                    return false;
                }
            }
            return nonConsecutiveRed(node.left) && nonConsecutiveRed(node.right);
        }
    }
    protected boolean noSelfLoop(RbTreeNode node) {
        if (node == null) {
            return true;
        } else {
            if (node.equals(node.left) || node.equals(node.right)) {
                return false;
            } else {
                return noSelfLoop(node.left) && noSelfLoop(node.right);
            }
        }
    }
    @Test
    public void check_size_when_empty() {
        RbTree emptyTree = createTree();
        assertEquals(emptyTree.size(), 0);
        checkInvariantRb(emptyTree);
    }
    @Test
    public void check_size_valid() {
        RbTree tree = createTree();
        tree.insert(1);
        tree.insert(12);
        assertEquals(2, tree.size());
    }
    @Test
    public void check_root_black() {
        RbTree tree = createTree();
        tree.insert(1);
        RbTreeNode root = tree.getOverallRoot();
        assertFalse(root.red);
    }
    @Test
    public void check_contains_valid() {
        RbTree tree = createTree();
        tree.insert(12);
        tree.insert(13);
        assertTrue(tree.contains(12) && tree.contains(13));
    }
    @Test
    public void check_batch_insert_valid() {
        RbTree tree = createTree();
        for (int i = 0; i < 8; i++) {
            tree.insert(i);
        }
        RbTreeNode root = tree.getOverallRoot();
        assertEquals(3, root.data);
        checkInvariantRb(tree);
    }
    @Test
    public void check_insert_duplicate() {
        RbTree tree = createTree();
        tree.insert(12);
        Exception e = assertThrows(IllegalArgumentException.class, ()->tree.insert(12));
        String expectedMessage = "Node is duplicate or empty";
        String actualMessage = e.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void check_delete_root() {
        RbTree tree = createTree();
        tree.insert(10);
        tree.insert(15);
        tree.insert(5);
        tree.insert(12);
        tree.delete(10);
        checkInvariantRb(tree);
        System.out.println(tree);
    }
    @Test
    public void check_delete_internal_after_batchInsert () {
        RbTree tree = createTree();
        for (int i = 0; i < 7; i++) {
            tree.insert(i);
        }
        tree.delete(4);
        System.out.println(tree);
        checkInvariantRb(tree);
    }
    @Test
    public void check_delete_red_leaf_after_batchInsert() {
        RbTree tree = createTree();
        for (int i = 1; i < 5; i++) {
            tree.insert(i);
        }
        tree.delete(4);
        System.out.println(tree);
        checkInvariantRb(tree);
    }
    @Test
    public void check_delete_black_sibling_black_leaf_after_batchInsert() {
        RbTree tree = createTree();
        for (int i = 1; i < 89; i++) {
            tree.insert(i);
        }
        tree.delete(2);
        checkInvariantRb(tree);
    }
    @Test
    public void check_delete_root_after_batchInsert1() {
        RbTree tree = createTree();
        for (int i = 1; i < 15; i++) {
            tree.insert(i);
        }
        tree.delete(tree.getOverallRoot().data);
        checkInvariantRb(tree);
    }
    @Test
    public void check_delete_root_after_batchInsert2() {
        RbTree tree = createTree();
        for (int i = 1; i < 91; i++) {
            tree.insert(i);
        }
        tree.delete(tree.getOverallRoot().data);
        checkInvariantRb(tree);
    }
}
