package cc.domovoi.spring.test;

import cc.domovoi.spring.entity.audit.AuditChangeContextGroupModel;
import cc.domovoi.spring.test.entity.AuditBeanEntityTestImpl;
import cc.domovoi.spring.test.mapper.AuditBeanMapperTestImpl;
import cc.domovoi.spring.test.mapper.AuditMapperTestImpl;
import cc.domovoi.spring.test.service.AuditBeanServiceTestImpl;
import cc.domovoi.spring.test.service.AuditServiceTestImpl;
import cc.domovoi.tools.jackson.ObjectMappers;
import cc.domovoi.tools.utils.RandomUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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
        AuditBeanEntityTestImpl auditBeanEntity1 = new AuditBeanEntityTestImpl(RandomUtils.randomString(), RandomUtils.randomInteger(), RandomUtils.randomDouble(), RandomUtils.randomBoolean(), RandomUtils.randomLocalDateTime());
        AuditBeanEntityTestImpl auditBeanEntity2 = new AuditBeanEntityTestImpl(RandomUtils.randomString(), RandomUtils.randomInteger(), RandomUtils.randomDouble(), RandomUtils.randomBoolean(), RandomUtils.randomLocalDateTime());
        auditBeanService.addEntity(auditBeanEntity1);
        auditBeanService.addEntity(auditBeanEntity2);
        auditMapper.showAllData();
    }

    @Test
    public void testFindChangeRecord() throws Exception {
        AuditBeanEntityTestImpl rootAuditBeanEntityTest = new AuditBeanEntityTestImpl(RandomUtils.randomString(), RandomUtils.randomInteger(), RandomUtils.randomDouble(), RandomUtils.randomBoolean(), RandomUtils.randomLocalDateTime());
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
}
