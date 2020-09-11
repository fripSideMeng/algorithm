import mhz.algo.rbtree.RbTree;
import mhz.algo.rbtree.RbTreeNode;
import org.junit.Test;

import static org.junit.Assert.*;

public class RbTreeTest {
    protected RbTree createTree() {
        return new RbTree();
    }
    @Test
    public void check_size_when_empty() {
        RbTree emptyTree = createTree();
        assertEquals(emptyTree.size(), 0);
    }
    @Test
    public void check_insert_three_valid() {
        RbTree tree = createTree();
        tree.insert(1);
        tree.insert(2);
        tree.insert(3);
        RbTreeNode root = tree.getOverallRoot();
        assertEquals(2, root.data);
        assertFalse(root.red);
        assertTrue(root.left.red);
        assertTrue(root.right.red);
    }
}
