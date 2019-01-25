package cc.domovoi.ej.spring.test;

import cc.domovoi.ej.spring.data.JoiningDepthTree;
import org.junit.Test;

public class TreeTest {

    @Test
    public void testTree() {

        JoiningDepthTree tree = new JoiningDepthTree();
        tree.put("a").put("b").put("c", new JoiningDepthTree().put("d").put("e", new JoiningDepthTree().put("f").put("g")));
        System.out.println(tree.treeString());

    }
}
