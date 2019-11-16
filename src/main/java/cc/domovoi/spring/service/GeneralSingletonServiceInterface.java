package cc.domovoi.spring.service;

import cc.domovoi.spring.entity.GeneralSingletonEntityInterface;
import cc.domovoi.spring.utils.joiningdepthtree.JoiningDepthTree;
import cc.domovoi.spring.utils.joiningdepthtree.JoiningDepthTreeLike;

public interface GeneralSingletonServiceInterface<K, E extends GeneralSingletonEntityInterface<K>> extends GeneralJoiningServiceInterface<K, E>, GeneralRetrieveSingletonServiceInterface<K, E> {

    @Override
    default JoiningDepthTreeLike depthTree() {
        return JoiningDepthTree.leaf;
    }
}
