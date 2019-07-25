package cc.domovoi.spring.utils;

import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.BaseJoiningEntityInterface;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

    public static <E extends BaseJoiningEntityInterface> Map<String, Object> deleteBatch(Logger logger, String name, List<String> idList, Class<E> entityClass, Function<E, Try<Integer>> deleteEntityFunction) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger.info(String.format("deleteEntityBatch: %s", idList));
            List<Try<Integer>> batchResult = idList.stream().map(id -> {
                try {
                    E deleteQuery = entityClass.newInstance();
                    deleteQuery.setId(id);
                    return deleteEntityFunction.apply(deleteQuery);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }).collect(Collectors.toList());
            if (batchResult.stream().allMatch(Try::isSuccess)) {
                return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, batchResult.size());
            }
            else {
                throw new RuntimeException(batchResult.stream().filter(Try::isFailure).findFirst().get().failed().get().getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("error in %s, message: %s", name, e.getLocalizedMessage()));
            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    public static <E, R> Map<String, Object> commonTryFunction(Logger logger, String name, Supplier<? extends Try<E>> data, Function<? super E, ? extends R> op) {
        logger.info(name);
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            Try<E> result = data.get();
            if (result.isSuccess()) {
                return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, op.apply(result.get()));
            }
            else {
                throw new RuntimeException(result.failed().get().getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("error in %s, message: %s", name, e.getLocalizedMessage()));
            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    public static <E> Map<String, Object> commonTryFunction(Logger logger, String name, Supplier<? extends Try<E>> data) {
        return commonTryFunction(logger, name, data, Function.identity());
    }
}
