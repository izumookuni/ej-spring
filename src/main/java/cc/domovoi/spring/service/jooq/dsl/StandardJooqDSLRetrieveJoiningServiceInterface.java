package cc.domovoi.spring.service.jooq.dsl;

import cc.domovoi.spring.entity.jooq.StandardJooqDSLEntityInterface;
import cc.domovoi.spring.mapper.jooq.StandardJooqRetrieveJoiningMapperInterface;
import cc.domovoi.spring.service.StandardRetrieveJoiningServiceInterface;

public interface StandardJooqDSLRetrieveJoiningServiceInterface<E extends StandardJooqDSLEntityInterface<?>, M extends StandardJooqRetrieveJoiningMapperInterface<?, E>> extends GeneralJooqDSLRetrieveJoiningServiceInterface<String, E, M>, StandardRetrieveJoiningServiceInterface<E> {

    @Override
    default Class<String> keyClass() {
        return String.class;
    }
}
