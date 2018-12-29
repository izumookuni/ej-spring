package cc.domovoi.ej.spring.mapper;

import cc.domovoi.ej.spring.entity.BaseJoiningEntityInterface;

public interface BaseMapperInterface<E extends BaseJoiningEntityInterface> extends BaseRetrieveMapperInterface<E> {

    Integer addBase(E entity);

    Integer updateBase(E entity);

    Integer deleteBase(E entity);
}
