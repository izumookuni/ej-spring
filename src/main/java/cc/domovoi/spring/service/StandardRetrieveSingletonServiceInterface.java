package cc.domovoi.spring.service;

import cc.domovoi.spring.entity.StandardSingletonEntityInterface;

public interface StandardRetrieveSingletonServiceInterface<E extends StandardSingletonEntityInterface> extends StandardRetrieveJoiningServiceInterface<E>, GeneralRetrieveSingletonServiceInterface<String, E> {
}
