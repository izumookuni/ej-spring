package cc.domovoi.spring.service;

import cc.domovoi.spring.entity.StandardJoiningEntityInterface;

import java.util.UUID;

public interface StandardJoiningServiceInterface<E extends StandardJoiningEntityInterface> extends StandardRetrieveJoiningServiceInterface<E> , GeneralJoiningServiceInterface<String, E> {

    @Override
    default String idGenerator() {
        return UUID.randomUUID().toString();
    }
}
