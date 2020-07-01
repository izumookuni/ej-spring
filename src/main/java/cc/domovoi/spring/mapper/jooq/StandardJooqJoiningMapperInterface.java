package cc.domovoi.spring.mapper.jooq;

import cc.domovoi.spring.entity.jooq.StandardJooqDSLEntityInterface;
import org.jooq.UpdatableRecord;

public interface StandardJooqJoiningMapperInterface<R extends UpdatableRecord<R>, E extends StandardJooqDSLEntityInterface<R>> extends GeneralJooqJoiningMapperInterface<R, String, E>, StandardJooqRetrieveJoiningMapperInterface<R, E> {
}
