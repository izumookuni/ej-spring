package cc.domovoi.spring.service.jooq;

import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.jooq.GeneralJooqEntityInterface;
import cc.domovoi.spring.service.GeneralJoiningServiceInterface;
import org.jooq.UpdatableRecord;
import org.jooq.lambda.tuple.Tuple2;

public interface GeneralJooqJoiningServiceInterface<R extends UpdatableRecord<R>, P, K, E extends GeneralJooqEntityInterface<P, K>> extends GeneralJooqRetrieveJoiningServiceInterface<R, P, K, E>, GeneralJoiningServiceInterface<K, E> {

    @Override
    default Boolean checkEntityExists(E entity) {
        return null;
    }

    @Override
    default Try<Tuple2<Integer, K>> innerAddEntity(E entity) {
        return null;
    }

    @Override
    default Try<Integer> innerUpdateEntity(E entity) {
        return null;
    }

    @Override
    default Try<Integer> innerDeleteEntity(E entity) {
        return null;
    }
}
