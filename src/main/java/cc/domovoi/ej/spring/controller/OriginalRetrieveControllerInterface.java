package cc.domovoi.ej.spring.controller;

import cc.domovoi.ej.spring.utils.RestfulUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface OriginalRetrieveControllerInterface<E> {

    Logger logger();

    Function<E, List<E>> findEntityFunction();

    @ApiOperation(value="Find entity", notes="")
    @RequestMapping(
            value = "query",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    default Map<String, Object> findEntity(@RequestBody E entity) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger().info(String.format("findEntity: %s", entity));
            List<E> entityList = findEntityFunction().apply(entity);
            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, entityList);

        } catch (Exception e) {
            e.printStackTrace();
            logger().error(String.format("error in findEntity, message: %s", e.getLocalizedMessage()));
            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    @ApiOperation(value="Test whether the interface GET request is available", notes="")
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

    @ApiOperation(value="Test whether the interface POST request is available", notes="")
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
