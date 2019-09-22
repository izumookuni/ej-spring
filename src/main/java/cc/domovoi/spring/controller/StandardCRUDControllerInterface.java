package cc.domovoi.spring.controller;

import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.StandardJoiningEntityInterface;
import cc.domovoi.spring.service.BaseJoiningServiceInterface;
import org.jooq.lambda.tuple.Tuple2;

/**
 * BaseCRUDControllerInterface.
 *
 * @param <E> Entity type.
 * @param <S> Service type.
 */
public interface StandardCRUDControllerInterface<E extends StandardJoiningEntityInterface, S extends BaseJoiningServiceInterface<E, ?>> extends StandardRetrieveControllerInterface<E, S>, GeneralCRUDControllerInterface<E> {

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
