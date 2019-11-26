package cc.domovoi.spring.utils.joiningdepthtree;

import java.util.*;
import java.util.stream.Collectors;

public class JoiningUnlimitedDepthTree implements JoiningDepthTreeLike {

    private final List<String> exclude;

    public JoiningUnlimitedDepthTree() {
        this.exclude = new ArrayList<>();
    }

    public JoiningUnlimitedDepthTree(List<String> exclude) {
        this.exclude = exclude;
    }

    public JoiningUnlimitedDepthTree exclude(String... exclude) {
        this.exclude.addAll(Arrays.stream(exclude).collect(Collectors.toList()));
        return this;
    }

    public JoiningUnlimitedDepthTree exclude(Collection<String> exclude) {
        this.exclude.addAll(exclude);
        return this;
    }

    @Override
    public Optional<JoiningDepthTreeLike> subTree(String key) {
        return Optional.of(this);
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
        return !this.exclude.contains(key);
    }

    @Deprecated
    @Override
    public Set<String> keySet() {
        throw new RuntimeException("no keySet");
    }

    @Override
    public String treeString() {
        return "UnlimitedTree";
    }
}
