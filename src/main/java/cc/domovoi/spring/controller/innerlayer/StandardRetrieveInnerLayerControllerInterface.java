package cc.domovoi.spring.controller.innerlayer;

import cc.domovoi.spring.controller.StandardRetrieveControllerInterface;
import cc.domovoi.spring.entity.StandardJoiningEntityInterface;
import cc.domovoi.spring.service.StandardRetrieveJoiningServiceInterface;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface StandardRetrieveInnerLayerControllerInterface<E extends StandardJoiningEntityInterface, S extends StandardRetrieveJoiningServiceInterface<E>> extends StandardRetrieveControllerInterface<E, S> {

    @ApiOperation(value = "Find raw entity list", notes = "")
    @RequestMapping(
            value = "inner-layer/query",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default List<E> findInnerLayerEntity(@RequestBody E entity, HttpServletRequest request, HttpServletResponse response) {
        logger().info(String.format("findInnerLayerEntity: %s", entity));
        doBeforeFindEntity(0, "findInnerLayerEntity", entity, request, response);
        List<E> entityList = findEntityFunction(entity);
        doAfterFindList(0, "findInnerLayerEntity", entityList, request, response);
        return entityList;
    }
}
