package cc.domovoi.spring.utils;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ControllerUtils {

    public static <E> Map<String, Object> commonFunction(Logger logger, String name, Supplier<? extends E> data) {
        logger.info(name);
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            Object result = data.get();
            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("error in %s, message: %s", name, e.getLocalizedMessage()));
            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }
}
