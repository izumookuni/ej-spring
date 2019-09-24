package cc.domovoi.spring.utils.joiningdepthtree;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

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
    public DepthTreeType type() {
        return DepthTreeType.NORMAL;
    }

    @Override
    public Boolean contains(String key) {
        return false;
    }

    @Deprecated
    @Override
    public Set<String> keySet() {
        return Collections.emptySet();
    }

    @Override
    public String treeString() {
        return "Leaf";
    }
}
