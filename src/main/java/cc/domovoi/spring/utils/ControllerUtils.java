package cc.domovoi.spring.utils;

import cc.domovoi.collection.util.Try;
import cc.domovoi.collection.util.TrySupplier;
import cc.domovoi.spring.entity.StandardJoiningEntityInterface;
import org.jooq.lambda.function.Function2;
import org.jooq.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ControllerUtils {

    public static <E> Map<String, Object> commonFunction(Logger logger, String name, TrySupplier<? extends E> data) throws Exception {
        logger.info(name);
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            E result = data.get();
            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("error in %s, message: %s", name, e.getMessage()));
//            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            throw e;
        }
    }

    public static <E extends StandardJoiningEntityInterface> Map<String, Object> deleteBatch(Logger logger, String name, List<String> idList, Class<E> entityClass, Map<String, Object> params, Function2<E, Map<String, Object>, Try<Integer>> deleteEntityFunction) throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            logger.info(String.format("deleteEntityBatch: %s", idList));
            List<Try<Integer>> batchResult = idList.stream().map(id -> {
                try {
                    E deleteQuery = entityClass.newInstance();
                    deleteQuery.setId(id);
                    return deleteEntityFunction.apply(deleteQuery, params);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                }
            }).collect(Collectors.toList());
            if (batchResult.stream().allMatch(Try::isSuccess)) {
                return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, batchResult.size());
            }
            else {
                throw batchResult.stream().filter(Try::isFailure).findFirst().get().failed().get();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("error in %s, message: %s", name, e.getMessage()));
            throw e;
//            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public static <E, R> Map<String, Object> commonTryFunction(Logger logger, String name, Supplier<? extends Try<E>> data, Function<? super E, ? extends R> op) throws Exception {
        logger.info(name);
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            Try<E> result = data.get();
            if (result.isSuccess()) {
                return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, op.apply(result.get()));
            }
            else {
                throw result.failed().get();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("error in %s, message: %s", name, e.getMessage()));
            throw e;
//            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public static <E> Map<String, Object> commonTryFunction(Logger logger, String name, Supplier<? extends Try<E>> data) throws Exception {
        return commonTryFunction(logger, name, data, Function.identity());
    }

    public static <K> Map<String, Object> addBatchTryFunction(Logger logger, String name, Supplier<? extends Try<Tuple2<Integer, List<K>>>> data) throws Exception {
        return commonTryFunction(logger, name, data, t2 -> {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("result", t2.v1());
            dataMap.put("id", t2.v2());
            return dataMap;
        });
    }

    public static <K> Map<String, Object> addTryFunction(Logger logger, String name, Supplier<? extends Try<Tuple2<Integer, K>>> data) throws Exception {
        return commonTryFunction(logger, name, data, t2 -> {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("result", t2.v1());
            dataMap.put("id", t2.v2());
            return dataMap;
        });
    }

    public static <E, R> Map<String, Object> commonOptionalFunction(Logger logger, String name, Supplier<? extends Optional<E>> data, Function<? super E, ? extends R> op) throws Exception {
        logger.info(name);
        Map<String, Object> jsonMap = new HashMap<>();
        try {
//            Object result = data.get();
            Optional<E> result = data.get();
            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK, result.map(op).orElse(null));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("error in %s, message: %s", name, e.getMessage()));
//            return RestfulUtils.fillError(jsonMap, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            throw e;
        }
    }

    public static <E, R> Map<String, Object> commonOptionalFunction(Logger logger, String name, Supplier<? extends Optional<E>> data) throws Exception {
        return commonOptionalFunction(logger, name, data, Function.identity());
    }

    public static <R> Map<String, Object> commonPagingFunction(Logger logger, String name, Supplier<? extends Tuple2<Integer, List<R>>> data) throws Exception {
        logger.info(name);
        try {
            Map<String, Object> jsonMap = new HashMap<>();
            Tuple2<Integer, List<R>> result = data.get();
            jsonMap.put("total", result.v1());
            jsonMap.put("rows", result.v2());
            return RestfulUtils.fillOk(jsonMap, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("error in %s, message: %s", name, e.getMessage()));
            throw e;
        }
    }
}
