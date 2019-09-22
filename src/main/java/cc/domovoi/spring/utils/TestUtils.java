package cc.domovoi.spring.utils;

import cc.domovoi.spring.entity.StandardJoiningEntityInterface;
import cc.domovoi.spring.entity.geometry.StandardGeometryMultipleJoiningEntityInterface;
import cc.domovoi.spring.service.BaseJoiningServiceInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.jooq.lambda.tuple.Tuple2;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class TestUtils {

    private static ObjectMapper jackson = new ObjectMapper();

    private static ObjectWriter writer = jackson.writerWithDefaultPrettyPrinter();

    public static <E extends StandardJoiningEntityInterface, S extends BaseJoiningServiceInterface<E, ?>> void testBeanEntityService(Class<E> entityClass, S service, Logger logger, Consumer<Boolean> assertFunction, String... ignore) throws Exception {
        E originEntity = RandomUtils.randomEntity(entityClass, ignore);
        logger.info(String.format("origin: %s", originEntity));
        Integer addResult = service.addEntity(originEntity).map(Tuple2::v1).getOrElse(() -> 0);
        assertFunction.accept(1 == addResult);

        E query = entityClass.newInstance();
        query.setId(originEntity.getId());
        List<E> eList1 = service.findList(query);
        logger.info(writer.writeValueAsString(eList1));

        E fixedEntity = RandomUtils.randomEntity(entityClass, ignore);
        fixedEntity.setId(originEntity.getId());
        logger.info(String.format("fixedEntity: %s", fixedEntity));
        Integer updateResult = service.updateEntity(fixedEntity).getOrElse(() -> 0);
        assertFunction.accept(1 == updateResult);

        List<E> eList2 = service.findList(query);
        logger.info(writer.writeValueAsString(eList2));

        E deleteQuery = entityClass.newInstance();
        deleteQuery.setId(originEntity.getId());
        Integer deleteResult = service.deleteEntity(deleteQuery).getOrElse(() -> 0);
        assertFunction.accept(1 == deleteResult || -1 == deleteResult);

        List<E> eList3 = service.findList(query);
        logger.info(writer.writeValueAsString(eList3));
    }

    public static <INNER, OUTER, E extends StandardGeometryMultipleJoiningEntityInterface<INNER, OUTER>, S extends BaseJoiningServiceInterface<E, ?>> void testGeometryEntityService(Class<E> entityClass, S service, Logger logger, Consumer<Boolean> assertFunction, Map<String, String>geometryPropertyTypeMap, String... ignore) throws Exception {
        E originEntity = RandomUtils.randomGeometryEntity(entityClass, geometryPropertyTypeMap, ignore);
        logger.info(String.format("origin: %s", originEntity));
        originEntity.geometricGetMap().forEach((key, supplier) -> {
            logger.info(String.format("key: %s -> content: %s", key, supplier.get().toString()));
        });
        Integer addResult = service.addEntity(originEntity).map(Tuple2::v1).getOrElse(() -> 0);
        assertFunction.accept(1 == addResult);
        originEntity.geometryGetMap().forEach((key, supplier) -> {
            logger.info(String.format("key: %s -> content: %s", key, supplier.get().toString()));
        });

        E query = entityClass.newInstance();
        query.setId(originEntity.getId());
        logger.debug(writer.writeValueAsString(query));

        List<E> eList1 = service.findList(query);
        logger.info("====insert====");
        logger.info(writer.writeValueAsString(eList1));

        Set<String> keySet = geometryPropertyTypeMap.keySet();

//        // 使用更新操作删除某个geometry
//        keySet.stream().findFirst().ifPresent(key -> {
//            originEntity.geometricSetMap().get(key).accept(null);
//            Integer updateResult1 = service.updateEntity(originEntity);
//            assertFunction.accept(1 == updateResult1 || 0 == updateResult1);
//        });



        // 更新整个entity
        E fixedEntity = RandomUtils.randomGeometryEntity(entityClass, geometryPropertyTypeMap, ignore);
        fixedEntity.setId(originEntity.getId());
        logger.info(String.format("fixedEntity: %s", fixedEntity));
        Integer updateResult2 = service.updateEntity(fixedEntity).getOrElse(() -> 0);
        assertFunction.accept(1 == updateResult2);

        List<E> eList2 = service.findList(query);
        logger.info("====update====");
        logger.info(writer.writeValueAsString(eList2));

        // 只删除某些geometry
        E deleteQuery1 = RandomUtils.randomGeometryEntity(entityClass, geometryPropertyTypeMap, ignore);
        deleteQuery1.setId(originEntity.getId());
        Integer deleteResult1 = service.deleteEntity(deleteQuery1).getOrElse(() -> 0);
        assertFunction.accept(-1 == deleteResult1);

        List<E> eList3 = service.findList(query);
        logger.info("====delete====");
        logger.info(writer.writeValueAsString(eList3));

        // 删除整个entity
        E deleteQuery2 = entityClass.newInstance();
        deleteQuery2.setId(originEntity.getId());
        Integer deleteResult2 = service.deleteEntity(deleteQuery2).getOrElse(() -> 0);
        assertFunction.accept(1 == deleteResult2);

        List<E> eList4 = service.findList(query);
        logger.info("====delete all====");
        logger.info(writer.writeValueAsString(eList4));
    }
}
