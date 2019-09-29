package cc.domovoi.spring.service.mvc;

import cc.domovoi.spring.entity.StandardSingletonEntityInterface;
import cc.domovoi.spring.mapper.StandardMapperInterface;
import cc.domovoi.spring.service.StandardSingletonServiceInterface;

public interface StandardMvcSingletonServiceInterface<E extends StandardSingletonEntityInterface, M extends StandardMapperInterface<E>> extends StandardMvcJoiningServiceInterface<E, M>, StandardSingletonServiceInterface<E> {
}
