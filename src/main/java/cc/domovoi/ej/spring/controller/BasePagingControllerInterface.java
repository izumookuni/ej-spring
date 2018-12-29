package cc.domovoi.ej.spring.controller;

import cc.domovoi.ej.spring.entity.BaseEntityPagingInterface;
import cc.domovoi.ej.spring.service.BasePagingServiceInterface;

import java.util.function.Function;

public interface BasePagingControllerInterface<E extends BaseEntityPagingInterface, S extends BasePagingServiceInterface<E, ?>> extends OriginalPagingControllerInterface<E>, GeneralControllerInterface<S> {

    @Override
    default Function<E, Integer> findCountFunction() {
        return service()::findCount;
    }
}
