package cc.domovoi.spring.service.jooq.dsl;

import cc.domovoi.spring.entity.jooq.StandardJooqDSLEntityInterface;
import cc.domovoi.spring.mapper.jooq.StandardJooqJoiningMapperInterface;
import cc.domovoi.spring.service.StandardJoiningServiceInterface;

public interface StandardJooqDSLJoiningServiceInterface<E extends StandardJooqDSLEntityInterface<?>, M extends StandardJooqJoiningMapperInterface<?, E>> extends StandardJooqDSLRetrieveJoiningServiceInterface<E, M>, GeneralJooqDSLJoiningServiceInterface<String, E, M>, StandardJoiningServiceInterface<E> {
}
