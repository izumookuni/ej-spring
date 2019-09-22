package cc.domovoi.spring.controller;

import cc.domovoi.spring.utils.CommonLogger;
import cc.domovoi.spring.utils.RestfulUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OriginalRetrieveControllerInterface.
 *
 * @param <E> Entity type.
 */
public interface GeneralRetrieveControllerInterface<E> extends OriginalControllerInterface {

    /**
     * The function that find entity list.
     *
     * @param entity entity
     * @return Entity list.
     */
    List<E> findEntityFunction(E entity);

    /**
     * Find entity list.
     *
     * @param entity Query conditions.
     * @param request request
     * @param response response
     * @return Entity list.
     */
    @ApiOperation(value = "Find entity list", notes = "")
    @RequestMapping(
            value = "query",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> findEntity(@RequestBody E entity, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("findEntity: %s", entity));
            List<E> entityList = findEntityFunction(entity);
            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, entityList);

        } catch (Exception e) {
            e.printStackTrace();
            logger().error(String.format("error in findEntity, message: %s", e.getLocalizedMessage()));
            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    /**
     * Test whether the interface GET request is available.
     *
     * @param content Content.
     * @return Response.
     */
    @ApiOperation(value = "Test whether the interface GET request is available", notes = "")
    @RequestMapping(
            value = "test",
            method = {RequestMethod.GET},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> testGet(String content) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("testGet: %s", content));
            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, String.format("Your input is %s", content));
        } catch (Exception e) {
            e.printStackTrace();
            logger().error(String.format("error in testGet, message: %s", e.getLocalizedMessage()));
            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    /**
     * Test whether the interface POST request is available.
     *
     * @param body Request body.
     * @return Response.
     */
    @ApiOperation(value = "Test whether the interface POST request is available", notes = "")
    @RequestMapping(
            value = "test",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> testPost(@RequestBody Map<String, String> body) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("testPost: %s", body));
            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, String.format("Your input is %s", body));
        } catch (Exception e) {
            e.printStackTrace();
            logger().error(String.format("error in testPost, message: %s", e.getLocalizedMessage()));
            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }
}
