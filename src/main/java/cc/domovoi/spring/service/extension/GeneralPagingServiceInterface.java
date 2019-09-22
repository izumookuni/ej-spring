package cc.domovoi.spring.service.extension;

import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.entity.extension.PagingEntityInterface;
import cc.domovoi.spring.mapper.extension.PagingMapperInterface;

import java.util.List;

public interface GeneralPagingServiceInterface<E extends GeneralJoiningEntityInterface<?> & PagingEntityInterface, M extends PagingMapperInterface<E>> extends PagingServiceInterface<E, M> {

    default List<E> findPagingListByMapper(E entity) {
        return mapper().findPagingList(entity);
    }
}
