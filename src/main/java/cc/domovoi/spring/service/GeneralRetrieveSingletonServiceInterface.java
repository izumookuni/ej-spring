package cc.domovoi.spring.service;

import cc.domovoi.spring.entity.GeneralSingletonEntityInterface;

import java.util.Collections;
import java.util.Map;

public interface GeneralRetrieveSingletonServiceInterface<K, E extends GeneralSingletonEntityInterface<K>> extends GeneralRetrieveJoiningServiceInterface<K, E> {

    @Override
    default Map<String, GeneralRetrieveJoiningServiceInterface> joiningService() {
        return Collections.emptyMap();
    }
}
