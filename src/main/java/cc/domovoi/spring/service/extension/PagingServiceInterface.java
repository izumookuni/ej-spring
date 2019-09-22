package cc.domovoi.spring.service.extension;

import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.entity.extension.PagingEntityInterface;
import cc.domovoi.spring.mapper.extension.CountingMapperInterface;

import java.util.List;

public interface PagingServiceInterface<E extends GeneralJoiningEntityInterface<?> & PagingEntityInterface, M extends CountingMapperInterface<E>> extends CountingServiceInterface<E, M> {

    List<E> findPagingListByMapper(E entity);

    default List<E> findPagingList(E entity) {
        return findPagingListByMapper(entity);
    }
}
