package cc.domovoi.spring.mapper;

import cc.domovoi.spring.entity.BaseJoiningEntityInterface;

/**
 * BaseRetrieveMapperInterface.
 *
 * @param <E> Entity type.
 */
public interface BaseRetrieveMapperInterface<E extends BaseJoiningEntityInterface> extends GeneralRetrieveMapperInterface<String, E> {
}
