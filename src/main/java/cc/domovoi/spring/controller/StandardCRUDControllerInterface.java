package cc.domovoi.spring.controller;

import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.StandardJoiningEntityInterface;
import cc.domovoi.spring.service.StandardJoiningServiceInterface;
import org.jooq.lambda.tuple.Tuple2;

import java.util.Map;

/**
 * BaseCRUDControllerInterface.
 *
 * @param <E> Entity type.
 * @param <S> Service type.
 */
public interface StandardCRUDControllerInterface<E extends StandardJoiningEntityInterface, S extends StandardJoiningServiceInterface<E>> extends StandardRetrieveControllerInterface<E, S>, GeneralCRUDControllerInterface<String, E> {

    @Override
    default Try<Tuple2<Integer, String>> addEntityFunction(E entity, Map<String, Object> params) {
        return service().addEntity(entity, params);
    }

    @Override
    default Try<Integer> updateEntityFunction(E entity, Map<String, Object> params) {
        return service().updateEntity(entity, params);
    }

    @Override
    default Try<Integer> deleteEntityFunction(E entity, Map<String, Object> params) {
        return service().deleteEntity(entity, params);
    }
}
