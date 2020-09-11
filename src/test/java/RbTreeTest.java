import mhz.algo.rbtree.RbTree;
import mhz.algo.rbtree.RbTreeNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class RbTreeTest {
    protected RbTree createTree() {
        return new RbTree();
    }
    protected void checkInvariantRb(RbTree tree) {
        RbTreeNode root = tree.getOverallRoot();
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
                    return false;
                }
            }
            return nonConsecutiveRed(node.left) && nonConsecutiveRed(node.right);
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
}
