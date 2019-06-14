package cc.domovoi.spring.controller;

import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.BaseJoiningEntityInterface;
import cc.domovoi.spring.service.BaseJoiningServiceInterface;
import org.jooq.lambda.tuple.Tuple2;

import java.util.function.Function;

/**
 * BaseCRUDControllerInterface.
 *
 * @param <E> Entity type.
 * @param <S> Service type.
 */
public interface BaseCRUDControllerInterface<E extends BaseJoiningEntityInterface, S extends BaseJoiningServiceInterface<E, ?>> extends BaseRetrieveControllerInterface<E, S>, OriginalCRUDControllerInterface<E> {

    @Override
    default Try<Tuple2<Integer, String>> addEntityFunction(E entity) {
        return service().addEntity(entity);
    }

    @Override
    default Try<Integer> updateEntityFunction(E entity) {
        return service().updateEntity(entity);
    }

    @Override
    default Try<Integer> deleteEntityFunction(E entity) {
        return service().deleteEntity(entity);
    }
}
