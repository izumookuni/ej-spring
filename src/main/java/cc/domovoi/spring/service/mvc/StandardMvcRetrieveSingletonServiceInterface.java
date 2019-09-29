package cc.domovoi.spring.service.mvc;

import cc.domovoi.spring.entity.StandardSingletonEntityInterface;
import cc.domovoi.spring.mapper.StandardRetrieveMapperInterface;
import cc.domovoi.spring.service.StandardRetrieveSingletonServiceInterface;

public interface StandardMvcRetrieveSingletonServiceInterface<E extends StandardSingletonEntityInterface, M extends StandardRetrieveMapperInterface<E>> extends StandardMvcRetrieveJoiningServiceInterface<E, M>, StandardRetrieveSingletonServiceInterface<E> {
}
