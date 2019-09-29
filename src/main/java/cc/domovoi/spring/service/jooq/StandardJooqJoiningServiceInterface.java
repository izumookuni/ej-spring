package cc.domovoi.spring.service.jooq;

import cc.domovoi.spring.entity.jooq.StandardJooqEntityInterface;
import cc.domovoi.spring.service.StandardJoiningServiceInterface;
import org.jooq.UpdatableRecord;

import java.util.UUID;

public interface StandardJooqJoiningServiceInterface<R extends UpdatableRecord<R>, P, E extends StandardJooqEntityInterface<P>> extends StandardJooqRetrieveJoiningServiceInterface<R, P, E>, GeneralJooqJoiningServiceInterface<R, P, String, E>, StandardJoiningServiceInterface<E> {

    @Override
    default String idGenerator() {
        return UUID.randomUUID().toString();
    }
}
