package cc.domovoi.spring.service;

import cc.domovoi.spring.entity.StandardSingletonEntityInterface;

public interface StandardSingletonServiceInterface<E extends StandardSingletonEntityInterface> extends StandardJoiningServiceInterface<E>, GeneralSingletonServiceInterface<String, E> {
}
