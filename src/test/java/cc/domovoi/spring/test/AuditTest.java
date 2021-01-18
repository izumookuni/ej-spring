package cc.domovoi.spring.test;

import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.audit.fieldbase.AuditChangeContextGroupModel;
import cc.domovoi.spring.entity.audit.batch.AuditChangeContextGroupBatchModel;
import cc.domovoi.spring.test.entity.AuditBeanEntityTestImpl;
import cc.domovoi.spring.test.entity.AuditBeanEntityTestImpl2;
import cc.domovoi.spring.test.mapper.AuditBeanMapperTestImpl;
import cc.domovoi.spring.test.mapper.AuditMapperTestImpl;
import cc.domovoi.spring.test.service.AuditBeanServiceTestImpl;
import cc.domovoi.spring.test.service.AuditServiceTestImpl;
import cc.domovoi.spring.utils.ReflectUtils;
import cc.domovoi.tools.jackson.ObjectMappers;
import cc.domovoi.tools.utils.RandomUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.lambda.tuple.Tuple2;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AuditTest {

    private static Logger logger = LoggerFactory.getLogger(AuditTest.class);

    private static ObjectMapper objectMapper = ObjectMappers.objectMapper;

    private AuditMapperTestImpl auditMapper;

    private AuditServiceTestImpl auditService;

    private AuditBeanMapperTestImpl auditBeanMapper;

    private AuditBeanServiceTestImpl auditBeanService;

    @Before
    public void initMapperService() {
        auditMapper = new AuditMapperTestImpl();
        auditService = new AuditServiceTestImpl(auditMapper);
        auditBeanMapper = new AuditBeanMapperTestImpl();
        auditBeanService = new AuditBeanServiceTestImpl(auditBeanMapper, auditService);
    }

    @Test
    public void testAddChangeRecord() throws Exception {
        AuditBeanEntityTestImpl auditBeanEntity1 = new AuditBeanEntityTestImpl(RandomUtils.randomString(), RandomUtils.randomString(), RandomUtils.randomInteger(), RandomUtils.randomDouble(), RandomUtils.randomBoolean(), RandomUtils.randomLocalDateTime());
        AuditBeanEntityTestImpl auditBeanEntity2 = new AuditBeanEntityTestImpl(RandomUtils.randomString(), RandomUtils.randomString(), RandomUtils.randomInteger(), RandomUtils.randomDouble(), RandomUtils.randomBoolean(), RandomUtils.randomLocalDateTime());
        auditBeanService.addEntity(auditBeanEntity1);
        auditBeanService.addEntity(auditBeanEntity2);
        auditMapper.showAllData();
    }

    @Test
    public void testFindChangeRecord() throws Exception {
        AuditBeanEntityTestImpl rootAuditBeanEntityTest = new AuditBeanEntityTestImpl(RandomUtils.randomString(), RandomUtils.randomString(), RandomUtils.randomInteger(), RandomUtils.randomDouble(), RandomUtils.randomBoolean(), RandomUtils.randomLocalDateTime());
        auditBeanService.addEntity(rootAuditBeanEntityTest);
        IntStream.range(0, 5).forEach(idx -> {
            AuditBeanEntityTestImpl auditBeanEntityTest = new AuditBeanEntityTestImpl();
            auditBeanEntityTest.setId(rootAuditBeanEntityTest.getId());
//            auditBeanEntityTest.setV1(rootAuditBeanEntityTest.getV1());
//            auditBeanEntityTest.setV2(rootAuditBeanEntityTest.getV2());
//            auditBeanEntityTest.setV3(rootAuditBeanEntityTest.getV3());
//            auditBeanEntityTest.setV4(rootAuditBeanEntityTest.getV4());
//            auditBeanEntityTest.setV5(rootAuditBeanEntityTest.getV5());

            if (RandomUtils.randomBoolean()) {
                auditBeanEntityTest.setV0(RandomUtils.randomString());
            }
            if (RandomUtils.randomBoolean()) {
                auditBeanEntityTest.setV1(RandomUtils.randomString());
            }
            if (RandomUtils.randomBoolean()) {
                auditBeanEntityTest.setV2(RandomUtils.randomInteger());
            }
            if (RandomUtils.randomBoolean()) {
                auditBeanEntityTest.setV3(RandomUtils.randomDouble());
            }
            if (RandomUtils.randomBoolean()) {
                auditBeanEntityTest.setV4(RandomUtils.randomBoolean());
            }
            if (RandomUtils.randomBoolean()) {
                auditBeanEntityTest.setV5(RandomUtils.randomLocalDateTime());
            }
            auditBeanService.updateEntity(auditBeanEntityTest);
        });
        auditMapper.showAllData();

//        List<AuditDisplayEntity> auditDisplayEntityList = auditMapper.findAllList();
//        List<AuditChangeContextGroupModel> auditChangeContextGroupModelList = auditService.findAuditChangeRecord(auditDisplayEntityList, AuditBeanEntityTestImpl.class, Optional.empty(), Optional.empty(), Optional.empty());
//        logger.debug(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(auditChangeContextGroupModelList));

        List<AuditChangeContextGroupModel> auditChangeContextGroupModelList = auditBeanService.findAuditChangeContextGroupModel();
        logger.debug(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(auditChangeContextGroupModelList));
    }

    @Test
    public void testAllFieldList() {
        AuditBeanEntityTestImpl2 entity = new AuditBeanEntityTestImpl2();
        List<Field> fieldList = ReflectUtils.allFieldList(entity.getClass());
        logger.debug("fieldList size " + fieldList.size());
        fieldList.forEach(field -> logger.debug("field " + field.getName()));
    }

//    @Test
//    public void testAddChangeRecord2() throws Exception {
//        AuditBeanEntityTestImpl2 auditBeanEntity1 = new AuditBeanEntityTestImpl2(RandomUtils.randomString(), RandomUtils.randomInteger(), RandomUtils.randomDouble(), RandomUtils.randomBoolean(), RandomUtils.randomLocalDateTime(), RandomUtils.randomString(), RandomUtils.randomString());
//        AuditBeanEntityTestImpl2 auditBeanEntity2 = new AuditBeanEntityTestImpl2(RandomUtils.randomString(), RandomUtils.randomInteger(), RandomUtils.randomDouble(), RandomUtils.randomBoolean(), RandomUtils.randomLocalDateTime(), RandomUtils.randomString());
//        auditBeanService.addEntity(auditBeanEntity1);
//        auditBeanService.addEntity(auditBeanEntity2);
//        auditMapper.showAllData();
//    }

    @Test
    public void testFindBatchChangeRecord() throws Exception {
//        List<AuditBeanEntityTestImpl> rootAuditBeanEntityTestList = IntStream.range(0, RandomUtils.randomInteger(1, 3)).mapToObj(idx -> new AuditBeanEntityTestImpl(RandomUtils.randomString(), RandomUtils.randomString(), RandomUtils.randomInteger(), RandomUtils.randomDouble(), RandomUtils.randomBoolean(), RandomUtils.randomLocalDateTime())).collect(Collectors.toList());
//        List<String> rootIdList = rootAuditBeanEntityTestList.stream().map(auditBeanService::addEntity).filter(Try::isSuccess).map(Try::get).map(Tuple2::v2).collect(Collectors.toList());
        IntStream.range(0, 5).forEach(idx -> {
            logger.debug(String.format("=== %s ===", idx));
            List<AuditBeanEntityTestImpl> auditBeanEntityTestList = IntStream.range(0, RandomUtils.randomInteger(1, 3)).mapToObj(i -> new AuditBeanEntityTestImpl(RandomUtils.randomString(), RandomUtils.randomString(), RandomUtils.randomInteger(), RandomUtils.randomDouble(), RandomUtils.randomBoolean(), RandomUtils.randomLocalDateTime(), IntStream.range(0, 3).mapToObj(ii -> RandomUtils.randomInteger()).collect(Collectors.toList()))).collect(Collectors.toList());
            List<String> idList = auditBeanEntityTestList.stream().map(auditBeanService::addEntity).filter(Try::isSuccess).map(Try::get).map(Tuple2::v2).collect(Collectors.toList());
            logger.debug("=== add ===");
            auditMapper.showAllData();
            idList.forEach(id -> {
                AuditBeanEntityTestImpl entity = new AuditBeanEntityTestImpl();
                entity.setId(id);
                auditBeanService.deleteEntity(entity);
            });
            logger.debug("=== delete ===");
            auditMapper.showAllData();
        });

        logger.debug("=== final ===");
        auditMapper.showAllData();

        logger.debug("=== change record ===");
        List<AuditChangeContextGroupBatchModel<AuditBeanEntityTestImpl>> auditChangeContextGroupBatchModelList = auditBeanService.findAuditChangeContextGroupBatchModel();
        logger.debug(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(auditChangeContextGroupBatchModelList));
    }
}
