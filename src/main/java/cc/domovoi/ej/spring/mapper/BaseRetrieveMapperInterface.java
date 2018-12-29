package cc.domovoi.ej.spring.mapper;

import cc.domovoi.ej.spring.entity.BaseJoiningEntityInterface;

import java.util.List;

public interface BaseRetrieveMapperInterface<E extends BaseJoiningEntityInterface> {

    E findBaseById(String id);

    List<E> findBaseList(E entity);
}
