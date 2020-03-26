package cc.domovoi.spring.service;

import cc.domovoi.spring.entity.StandardJoiningEntityInterface;

public interface StandardRetrieveJoiningServiceInterface<E extends StandardJoiningEntityInterface> extends GeneralRetrieveJoiningServiceInterface<String, E> {

    @Override
    default Class<String> keyClass() {
        return String.class;
    }
}
