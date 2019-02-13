package cc.domovoi.ej.spring.data;

import java.util.Optional;
import java.util.Set;

public interface JoiningDepthTreeLike {

    Optional<JoiningDepthTreeLike> subTree(String key);

    Boolean isLeaf();

    default Boolean isTree() {
        return !isLeaf();
    }

    Boolean contains(String key);

    Set<String> keySet();

    String treeString();
}
