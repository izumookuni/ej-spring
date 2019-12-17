package cc.domovoi.spring.controller;

import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.StandardJoiningEntityInterface;
import cc.domovoi.spring.service.StandardJoiningServiceInterface;
import org.jooq.lambda.tuple.Tuple2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * BaseCRUDControllerInterface.
 *
 * @param <E> Entity type.
 * @param <S> Service type.
 */
public interface StandardCRUDControllerInterface<E extends StandardJoiningEntityInterface, S extends StandardJoiningServiceInterface<E>> extends StandardRetrieveControllerInterface<E, S>, GeneralCRUDControllerInterface<String, E> {

    @Override
    default Try<Tuple2<Integer, String>> addEntityFunction(E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        return service().addEntity(entity, request, response);
    }

    @Override
    default Try<Integer> updateEntityFunction(E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        return service().updateEntity(entity, request, response);
    }

    @Override
    default Try<Integer> deleteEntityFunction(E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        return service().deleteEntity(entity, request, response);
    }
}
