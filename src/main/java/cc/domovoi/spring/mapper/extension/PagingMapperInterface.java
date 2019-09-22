package cc.domovoi.spring.mapper.extension;

import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.entity.extension.PagingEntityInterface;

import java.util.List;

public interface PagingMapperInterface<E extends GeneralJoiningEntityInterface<?> & PagingEntityInterface> extends CountingMapperInterface<E> {

    List<E> findPagingList(E entity);
}
