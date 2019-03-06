package cc.domovoi.spring.utils.joiningdepthtree;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JoiningDepthTree implements JoiningDepthTreeLike {

    public static JoiningDepthLeaf leaf = new JoiningDepthLeaf();

    private Map<String, JoiningDepthTreeLike> tree;

    public JoiningDepthTree() {
        this.tree = new HashMap<>();
    }

    public JoiningDepthTree(Map<String, JoiningDepthTreeLike> tree) {
        this.tree = tree;
    }

    public JoiningDepthTree put(String key, JoiningDepthTreeLike tree) {
        this.tree.put(key, tree);
        return this;
    }

    public JoiningDepthTree put(String key) {
        this.tree.put(key, leaf);
        return this;
    }

    @Override
    public Optional<JoiningDepthTreeLike> subTree(String key) {
        return Optional.ofNullable(tree.get(key));
    }

    @Override
    public Boolean isLeaf() {
        return false;
    }

    @Override
    public Boolean contains(String key) {
        return tree.containsKey(key);
    }

    @Override
    public Set<String> keySet() {
        return tree.keySet();
    }

    @Override
    public String treeString() {
        return tree.entrySet().stream().map(entry -> String.format("%s -> %s", entry.getKey(), entry.getValue().treeString())).collect(Collectors.joining(",", "{", "}"));
    }

    public Map<String, JoiningDepthTreeLike> getTree() {
        return tree;
    }

    public void setTree(Map<String, JoiningDepthTreeLike> tree) {
        this.tree = tree;
    }

}
