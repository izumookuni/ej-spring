package cc.domovoi.spring.service.paging;

import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.service.GeneralJoiningServiceInterface;
import cc.domovoi.spring.utils.joiningdepthtree.JoiningDepthTreeLike;

import java.util.List;

public interface GeneralPagingServiceInterface<K, E extends GeneralJoiningEntityInterface<K>> extends GeneralJoiningServiceInterface<K, E> {

    Integer findCount(E entity);

    List<E> innerFindPagingList(E entity, Integer pageNum, Integer pageSize, List<String> sortBy, List<String> sortOrder);

    default List<E> findPagingList(E entity, Integer pageNum, Integer pageSize, List<String> sortBy, List<String> sortOrder, JoiningDepthTreeLike depthTree) {
        return findList(() -> innerFindPagingList(entity, pageNum, pageSize, sortBy, sortOrder), depthTree);
    }

    default List<E> findPagingList(E entity, Integer pageNum, Integer pageSize, List<String> sortBy, List<String> sortOrder) {
        return findList(() -> innerFindPagingList(entity, pageNum, pageSize, sortBy, sortOrder), depthTree());
    }

    default List<E> findPagingList(E entity, Integer pageNum, Integer pageSize) {
        return findList(() -> innerFindPagingList(entity, pageNum, pageSize, null, null), depthTree());
    }
}
