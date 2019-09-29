package cc.domovoi.spring.service.mvc;

import cc.domovoi.spring.entity.StandardJoiningEntityInterface;
import cc.domovoi.spring.mapper.StandardRetrieveMapperInterface;
import cc.domovoi.spring.service.StandardRetrieveJoiningServiceInterface;

public interface StandardMvcRetrieveJoiningServiceInterface<E extends StandardJoiningEntityInterface, M extends StandardRetrieveMapperInterface<E>> extends GeneralMvcRetrieveJoiningServiceInterface<String, E, M>, StandardRetrieveJoiningServiceInterface<E> {


}
