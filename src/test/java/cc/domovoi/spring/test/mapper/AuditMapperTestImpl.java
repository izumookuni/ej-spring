package cc.domovoi.spring.test.mapper;

import cc.domovoi.spring.entity.audit.AuditDisplayEntity;
import cc.domovoi.spring.mapper.audit.AuditMapperInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AuditMapperTestImpl implements AuditMapperInterface {

    private static Logger logger = LoggerFactory.getLogger(AuditMapperTestImpl.class);

    private List<AuditDisplayEntity> auditDisplayEntityList;

    public AuditMapperTestImpl() {
        auditDisplayEntityList = new ArrayList<>();
    }

    @Override
    public AuditDisplayEntity findEntityById(String id) {
        return auditDisplayEntityList.stream().filter(auditDisplayEntity -> Objects.equals(auditDisplayEntity.getAuditId(), id)).findFirst().orElse(null);
    }

    @Deprecated
    @Override
    public List<AuditDisplayEntity> findList(AuditDisplayEntity entity) {
        return auditDisplayEntityList.stream().filter(auditDisplayEntity -> Objects.equals(auditDisplayEntity, entity)).collect(Collectors.toList());
    }

    @Override
    public List<AuditDisplayEntity> findListById(List<String> idList) {
        return auditDisplayEntityList.stream().filter(auditDisplayEntity -> idList.contains(auditDisplayEntity.getAuditId())).collect(Collectors.toList());
    }

    @Override
    public Integer addEntity(AuditDisplayEntity entity) {
        logger.debug("addEntity: " + entity);
        if (findEntityById(entity.getAuditId()) == null) {
            auditDisplayEntityList.add(entity);
            return 1;
        }
        else {
            return 0;
        }
    }

    @Override
    public Integer updateEntity(AuditDisplayEntity entity) {
        logger.debug("updateEntity: " + entity);
        AuditDisplayEntity auditDisplayEntity = findEntityById(entity.getAuditId());
        if (auditDisplayEntity != null) {
            auditDisplayEntityList.remove(auditDisplayEntity);
            auditDisplayEntityList.add(entity);
            return 1;
        }
        else {
            return 0;
        }
    }

    @Override
    public Integer deleteEntity(AuditDisplayEntity entity) {
        logger.debug("deleteEntity: " + entity);
        AuditDisplayEntity auditDisplayEntity = findEntityById(entity.getAuditId());
        if (auditDisplayEntity != null) {
            auditDisplayEntityList.remove(auditDisplayEntity);
            return 1;
        }
        else {
            return 0;
        }
    }

    @Override
    public List<AuditDisplayEntity> findListByScopeId(String scopeId) {
        return auditDisplayEntityList.stream().filter(auditDisplayEntity -> Objects.equals(auditDisplayEntity.getScopeId(), scopeId)).collect(Collectors.toList());
    }

    @Override
    public List<AuditDisplayEntity> findListByContextName(String contextName) {
        return auditDisplayEntityList.stream().filter(auditDisplayEntity -> Objects.equals(auditDisplayEntity.getContextName(), contextName)).collect(Collectors.toList());
    }

    public List<AuditDisplayEntity> findAllList() {
        return auditDisplayEntityList;
    }

    public void showAllData() {
        logger.debug("showAllData: " + auditDisplayEntityList.size());
        auditDisplayEntityList.forEach(auditDisplayEntity -> logger.debug(auditDisplayEntity.toString()));

    }
}
