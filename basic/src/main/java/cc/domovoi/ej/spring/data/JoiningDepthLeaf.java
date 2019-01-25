package cc.domovoi.ej.spring.data;

import java.util.Optional;

public class JoiningDepthLeaf implements JoiningDepthTreeLike {

    public JoiningDepthLeaf() {
    }

    @Override
    public Optional<JoiningDepthTreeLike> subTree(String key) {
        return Optional.empty();
    }

    @Override
    public Boolean isLeaf() {
        return true;
    }

    @Override
    public String treeString() {
        return "Leaf";
    }
}
