package cc.domovoi.spring.mapper;

import cc.domovoi.spring.entity.StandardJoiningEntityInterface;

/**
 * StandardMapperInterface.
 *
 * @param <E> Entity type.
 */
public interface StandardMapperInterface<E extends StandardJoiningEntityInterface> extends StandardRetrieveMapperInterface<E>, GeneralMapperInterface<String, E> {
}
