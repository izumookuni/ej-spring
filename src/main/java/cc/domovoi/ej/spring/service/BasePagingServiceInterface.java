package cc.domovoi.ej.spring.service;

import cc.domovoi.ej.spring.entity.BaseEntityPagingInterface;
import cc.domovoi.ej.spring.mapper.BasePagingMapperInterface;

public interface BasePagingServiceInterface<E extends BaseEntityPagingInterface, M extends BasePagingMapperInterface<E>> extends GeneralServiceInterface<M> {

    default Integer findCount(E entity) {
        return findCountByMapper(entity);
    }

    default Integer findCountByMapper(E entity) {
        return mapper().findCount(entity);
    }
}
