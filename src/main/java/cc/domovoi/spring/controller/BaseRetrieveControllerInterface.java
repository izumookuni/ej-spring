package cc.domovoi.spring.controller;

import cc.domovoi.spring.entity.BaseJoiningEntityInterface;
import cc.domovoi.spring.service.BaseRetrieveJoiningServiceInterface;
import cc.domovoi.spring.utils.RestfulUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BaseRetrieveControllerInterface.
 *
 * @param <E> Entity type.
 * @param <S> Service type.
 */
public interface BaseRetrieveControllerInterface<E extends BaseJoiningEntityInterface, S extends BaseRetrieveJoiningServiceInterface<E, ?>> extends OriginalRetrieveControllerInterface<E>, GeneralControllerInterface<S> {

    @Override
    default List<E> findEntityFunction(E entity) {
        return service().findList(entity);
    }

    /**
     * The function that find entity.
     * @param id ID of entity.
     * @return Entity.
     */
    default E findEntityByIdFunction(String id) {
        return service().findEntity(id);
    }

    /**
     * Find entity by ID.
     *
     * @param id ID of entity.
     * @param request request
     * @param response response
     * @return Entity.
     */
    @ApiOperation(value = "Find entity by id", notes = "")
    @RequestMapping(
            value = "query/{id}",
            method = {RequestMethod.GET},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> findById(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("findById: %s", id));
            E entity = findEntityByIdFunction(id);
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
