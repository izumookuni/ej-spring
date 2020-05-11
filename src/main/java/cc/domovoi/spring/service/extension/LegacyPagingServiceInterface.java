package cc.domovoi.spring.service.extension;

import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.entity.extension.PagingEntityInterface;
import cc.domovoi.spring.mapper.GeneralRetrieveMapperInterface;
import cc.domovoi.spring.mapper.extension.CountingMapperInterface;

import java.util.List;

@Deprecated
public interface LegacyPagingServiceInterface<E extends GeneralJoiningEntityInterface<?> & PagingEntityInterface, M extends CountingMapperInterface<E> & GeneralRetrieveMapperInterface<?, E>> extends PagingServiceInterface<E, M> {

    @Override
    default List<E> findPagingListByMapper(E entity) {
        return mvcMapper().findBaseList(entity);
    }
}
