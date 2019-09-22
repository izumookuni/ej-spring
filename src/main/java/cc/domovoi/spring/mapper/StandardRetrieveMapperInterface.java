package cc.domovoi.spring.mapper;

import cc.domovoi.spring.entity.StandardJoiningEntityInterface;

/**
 * BaseRetrieveMapperInterface.
 *
 * @param <E> Entity type.
 */
public interface StandardRetrieveMapperInterface<E extends StandardJoiningEntityInterface> extends GeneralRetrieveMapperInterface<String, E> {
}
