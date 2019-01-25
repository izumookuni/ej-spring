package cc.domovoi.ej.spring.mapper;

import cc.domovoi.ej.spring.entity.BaseJoiningEntityInterface;

import java.util.List;

/**
 * BaseRetrieveMapperInterface.
 *
 * @param <E> Entity type.
 */
public interface BaseRetrieveMapperInterface<E extends BaseJoiningEntityInterface> extends GeneralRetrieveMapperInterface<String, E> {
}
