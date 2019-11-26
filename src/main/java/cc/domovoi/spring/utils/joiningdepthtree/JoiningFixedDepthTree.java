package cc.domovoi.spring.utils.joiningdepthtree;

import java.util.*;
import java.util.stream.Collectors;

public class JoiningFixedDepthTree implements JoiningDepthTreeLike {

    private final Integer depth;

    private final List<String> exclude;

    public JoiningFixedDepthTree(Integer depth) {
        this.depth = depth;
        this.exclude = new ArrayList<>();
    }

    public JoiningFixedDepthTree(Integer depth, List<String> exclude) {
        this.depth = depth;
        this.exclude = exclude;
    }

    public static JoiningFixedDepthTree singleLayer() {
        return new JoiningFixedDepthTree(1);
    }

    public static JoiningFixedDepthTree emptyLayer() {
        return new JoiningFixedDepthTree(0);
    }

    public static JoiningFixedDepthTree multiLayer(Integer depth) {
        return new JoiningFixedDepthTree(depth);
    }

    public Boolean hasNext() {
        return this.depth > 0;
    }

    public JoiningFixedDepthTree next() {
        return new JoiningFixedDepthTree(this.depth - 1);
    }

    public Integer getDepth() {
        return depth;
    }

    public JoiningFixedDepthTree exclude(String... exclude) {
        this.exclude.addAll(Arrays.stream(exclude).collect(Collectors.toList()));
        return this;
    }

    public JoiningFixedDepthTree exclude(Collection<String> exclude) {
        this.exclude.addAll(exclude);
        return this;
    }

    @Override
    public Optional<JoiningDepthTreeLike> subTree(String key) {
        return Optional.of(next());
    }

    @Override
    public Boolean isLeaf() {
        return !hasNext();
    }

    @Override
    public DepthTreeType type() {
        return DepthTreeType.FIXED;
    }

    @Override
    public Boolean contains(String key) {
        return !exclude.contains(key) && hasNext();
    }

    @Deprecated
    @Override
    public Set<String> keySet() {
        throw new RuntimeException("no keySet");
    }

    @Override
    public String treeString() {
        return String.format("FixedTree(%s)", depth);
    }
}
