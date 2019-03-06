package cc.domovoi.spring.controller;

import cc.domovoi.ej.collection.tuple.Tuple2;
import cc.domovoi.ej.collection.util.Try;
import cc.domovoi.spring.entity.BaseJoiningEntityInterface;
import cc.domovoi.spring.service.BaseJoiningServiceInterface;

import java.util.function.Function;

/**
 * BaseCRUDControllerInterface.
 *
 * @param <E> Entity type.
 * @param <S> Service type.
 */
public interface BaseCRUDControllerInterface<E extends BaseJoiningEntityInterface, S extends BaseJoiningServiceInterface<E, ?>> extends BaseRetrieveControllerInterface<E, S>, OriginalCRUDControllerInterface<E> {

    @Override
    default Function<E, Try<Tuple2<Integer, String>>> addEntityFunction() {
        return service()::addEntity;
    }

    @Override
    default Function<E, Try<Integer>> updateEntityFunction() {
        return service()::updateEntity;
    }

    @Override
    default Function<E, Try<Integer>> deleteEntityFunction() {
        return service()::deleteEntity;
    }
}
