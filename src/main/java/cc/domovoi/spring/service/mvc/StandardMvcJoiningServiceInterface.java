package cc.domovoi.spring.service.mvc;

import cc.domovoi.spring.entity.StandardJoiningEntityInterface;
import cc.domovoi.spring.mapper.StandardMapperInterface;
import cc.domovoi.spring.service.StandardJoiningServiceInterface;

import java.util.UUID;

public interface StandardMvcJoiningServiceInterface<E extends StandardJoiningEntityInterface, M extends StandardMapperInterface<E>> extends StandardMvcRetrieveJoiningServiceInterface<E, M>, GeneralMvcJoiningServiceInterface<String, E, M>, StandardJoiningServiceInterface<E> {

}
