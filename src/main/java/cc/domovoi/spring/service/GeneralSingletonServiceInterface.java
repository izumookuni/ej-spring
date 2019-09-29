package cc.domovoi.spring.service;

import cc.domovoi.spring.entity.GeneralSingletonEntityInterface;

public interface GeneralSingletonServiceInterface<K, E extends GeneralSingletonEntityInterface<K>> extends GeneralJoiningServiceInterface<K, E>, GeneralRetrieveSingletonServiceInterface<K, E> {
}
