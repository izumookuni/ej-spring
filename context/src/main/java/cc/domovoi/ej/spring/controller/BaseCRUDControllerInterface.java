package cc.domovoi.ej.spring.controller;

import cc.domovoi.ej.spring.entity.BaseJoiningEntityInterface;
import cc.domovoi.ej.spring.service.BaseJoiningServiceInterface;

import java.util.function.Function;

/**
 * BaseCRUDControllerInterface.
 *
 * @param <E> Entity type.
 * @param <S> Service type.
 */
public interface BaseCRUDControllerInterface<E extends BaseJoiningEntityInterface, S extends BaseJoiningServiceInterface<E, ?>> extends BaseRetrieveControllerInterface<E, S>, OriginalCRUDControllerInterface<E> {

    @Override
    default Function<E, Integer> addEntityFunction() {
        return service()::addEntity;
    }

    @Override
    default Function<E, Integer> updateEntityFunction() {
        return service()::updateEntity;
    }

    @Override
    default Function<E, Integer> deleteEntityFunction() {
        return service()::deleteEntity;
    }
}
