package cc.domovoi.spring.utils.joiningdepthtree;

import java.util.Optional;
import java.util.Set;

public interface JoiningDepthTreeLike {

    Optional<JoiningDepthTreeLike> subTree(String key);

    Boolean isLeaf();

    default Boolean isTree() {
        return !isLeaf();
    }

    DepthTreeType type();

    Boolean contains(String key);

    @Deprecated
    Set<String> keySet();

    String treeString();
}
