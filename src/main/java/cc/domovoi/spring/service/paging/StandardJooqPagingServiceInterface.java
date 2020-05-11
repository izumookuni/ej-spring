package cc.domovoi.spring.service.paging;

import cc.domovoi.spring.entity.jooq.StandardJooqEntityInterface;
import cc.domovoi.spring.service.jooq.StandardJooqRetrieveJoiningServiceInterface;
import org.jooq.TableRecord;

public interface StandardJooqPagingServiceInterface<R extends TableRecord<R>, P, E extends StandardJooqEntityInterface<P>> extends GeneralJooqPagingServiceInterface<R, P, String, E>, StandardPagingServiceInterface<E>, StandardJooqRetrieveJoiningServiceInterface<R, P, E> {
}
