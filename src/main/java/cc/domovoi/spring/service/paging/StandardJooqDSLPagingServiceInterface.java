package cc.domovoi.spring.service.paging;

import cc.domovoi.spring.entity.jooq.StandardJooqDSLEntityInterface;

import cc.domovoi.spring.mapper.jooq.StandardJooqRetrieveJoiningMapperInterface;
import cc.domovoi.spring.service.jooq.dsl.StandardJooqDSLRetrieveJoiningServiceInterface;
import org.jooq.TableRecord;

public interface StandardJooqDSLPagingServiceInterface<R extends TableRecord<R>, E extends StandardJooqDSLEntityInterface<R>, M extends StandardJooqRetrieveJoiningMapperInterface<?, E>> extends GeneralJooqDSLPagingServiceInterface<String, E, M>, StandardPagingServiceInterface<E>, StandardJooqDSLRetrieveJoiningServiceInterface<E, M> {
}
