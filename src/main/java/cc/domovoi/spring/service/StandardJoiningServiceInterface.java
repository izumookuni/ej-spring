package cc.domovoi.spring.service;

import cc.domovoi.spring.entity.StandardJoiningEntityInterface;

public interface StandardJoiningServiceInterface<E extends StandardJoiningEntityInterface> extends StandardRetrieveJoiningServiceInterface<E> , GeneralJoiningServiceInterface<String, E> {
}
