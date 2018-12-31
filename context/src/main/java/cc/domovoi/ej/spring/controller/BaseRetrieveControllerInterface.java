package cc.domovoi.ej.spring.controller;

import cc.domovoi.ej.spring.entity.BaseJoiningEntityInterface;
import cc.domovoi.ej.spring.service.BaseRetrieveJoiningServiceInterface;
import cc.domovoi.ej.spring.utils.RestfulUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * BaseRetrieveControllerInterface.
 *
 * @param <E> Entity type.
 * @param <S> Service type.
 */
public interface BaseRetrieveControllerInterface<E extends BaseJoiningEntityInterface, S extends BaseRetrieveJoiningServiceInterface<E, ?>> extends OriginalRetrieveControllerInterface<E>, GeneralControllerInterface<S> {

    @Override
    default Function<E, List<E>> findEntityFunction() {
        return service()::findList;
    }

    /**
     * Find entity by ID.
     *
     * @param id ID of entity.
     * @return Entity.
     */
    @ApiOperation(value = "Find entity by id", notes = "")
    @RequestMapping(
            value = "query/{id}",
            method = {RequestMethod.GET},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> findById(@PathVariable String id) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("findById: %s", id));
            E entity = service().findEntity(id);
            if (entity != null) {
                return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, entity);
            } else {
                return RestfulUtils.fillError(jsonMap, HttpStatus.NOT_FOUND, String.format("Can't find Entity with id %s", id));
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger().error(String.format("error in findById, message: %s", e.getLocalizedMessage()));
            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }
}
