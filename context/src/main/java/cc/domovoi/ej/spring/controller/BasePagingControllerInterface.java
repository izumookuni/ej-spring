package cc.domovoi.ej.spring.controller;

import cc.domovoi.ej.spring.entity.BasePagingEntityInterface;
import cc.domovoi.ej.spring.service.BasePagingServiceInterface;

import java.util.function.Function;

/**
 * BasePagingControllerInterface.
 *
 * @param <E> Entity type.
 * @param <S> Service type.
 */
public interface BasePagingControllerInterface<E extends BasePagingEntityInterface, S extends BasePagingServiceInterface<E, ?>> extends OriginalPagingControllerInterface<E>, GeneralControllerInterface<S> {

    @Override
    default Function<E, Integer> findCountFunction() {
        return service()::findCount;
    }
}
