package cc.domovoi.spring.service.jooq;

import cc.domovoi.spring.entity.jooq.StandardJooqEntityInterface;
import cc.domovoi.spring.service.StandardRetrieveJoiningServiceInterface;
import org.jooq.TableRecord;

public interface StandardJooqRetrieveJoiningServiceInterface<R extends TableRecord<R>, P, E extends StandardJooqEntityInterface<P>> extends GeneralJooqRetrieveJoiningServiceInterface<R, P, String, E>, StandardRetrieveJoiningServiceInterface<E> {
}
