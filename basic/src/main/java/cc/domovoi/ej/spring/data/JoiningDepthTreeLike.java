package cc.domovoi.ej.spring.data;

import java.util.Optional;

public interface JoiningDepthTreeLike {

    Optional<JoiningDepthTreeLike> subTree(String key);

    Boolean isLeaf();

    String treeString();
}
