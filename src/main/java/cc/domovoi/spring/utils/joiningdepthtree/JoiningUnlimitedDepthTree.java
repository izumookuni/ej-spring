package cc.domovoi.spring.utils.joiningdepthtree;

import java.util.Optional;
import java.util.Set;

public class JoiningUnlimitedDepthTree implements JoiningDepthTreeLike {


    public JoiningUnlimitedDepthTree() {
    }

    @Override
    public Optional<JoiningDepthTreeLike> subTree(String key) {
        return Optional.empty();
    }

    @Override
    public Boolean isLeaf() {
        return false;
    }

    @Override
    public DepthTreeType type() {
        return DepthTreeType.UNLIMITED;
    }

    @Override
    public Boolean contains(String key) {
        return true;
    }

    @Deprecated
    @Override
    public Set<String> keySet() {
        throw new RuntimeException("no keySet");
    }

    @Override
    public String treeString() {
        return "JoiningUnlimitedDepthTree";
    }
}
