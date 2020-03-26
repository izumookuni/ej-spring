package cc.domovoi.spring.test.mapper;

import cc.domovoi.spring.entity.audit.AuditDisplayEntity;
import cc.domovoi.spring.mapper.audit.AuditMapperInterface;
import org.jooq.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AuditMapperTestImpl implements AuditMapperInterface {

    private static Logger logger = LoggerFactory.getLogger(AuditMapperTestImpl.class);

    private List<AuditDisplayEntity> auditDisplayEntityList;

    private List<Tuple2<String, String>> auditScopeList;

    public AuditMapperTestImpl() {
        auditDisplayEntityList = new ArrayList<>();
        auditScopeList = new ArrayList<>();
    }

    @Override
    public AuditDisplayEntity findEntityById(String id) {
        return auditDisplayEntityList.stream().filter(auditDisplayEntity -> Objects.equals(auditDisplayEntity.getAuditId(), id)).peek(auditDisplayEntity -> {
            List<String> scopeIdList = auditScopeList.stream().filter(t2 -> Objects.equals(t2.v1(), auditDisplayEntity.getAuditId())).map(Tuple2::v2).collect(Collectors.toList());
            auditDisplayEntity.setScopeIdList(scopeIdList);
        }).findFirst().orElse(null);
    }

    @Deprecated
    @Override
    public List<AuditDisplayEntity> findAuditList(AuditDisplayEntity entity) {
        return auditDisplayEntityList.stream().filter(auditDisplayEntity -> Objects.equals(auditDisplayEntity, entity)).peek(auditDisplayEntity -> {
            List<String> scopeIdList = auditScopeList.stream().filter(t2 -> Objects.equals(t2.v1(), auditDisplayEntity.getAuditId())).map(Tuple2::v2).collect(Collectors.toList());
            auditDisplayEntity.setScopeIdList(scopeIdList);
        }).collect(Collectors.toList());
    }

    @Override
    public List<AuditDisplayEntity> findAuditListById(List<String> idList) {
        return auditDisplayEntityList.stream().filter(auditDisplayEntity -> idList.contains(auditDisplayEntity.getAuditId())).peek(auditDisplayEntity -> {
            List<String> scopeIdList = auditScopeList.stream().filter(t2 -> Objects.equals(t2.v1(), auditDisplayEntity.getAuditId())).map(Tuple2::v2).collect(Collectors.toList());
            auditDisplayEntity.setScopeIdList(scopeIdList);
        }).collect(Collectors.toList());
    }

    @Override
    public Integer addEntity(AuditDisplayEntity entity) {
        logger.debug("addEntity: " + entity);
        if (findEntityById(entity.getAuditId()) == null) {
            auditDisplayEntityList.add(entity);
            auditScopeList.addAll(entity.getScopeIdList().stream().map(scopeId -> new Tuple2<>(entity.getAuditId(), scopeId)).collect(Collectors.toList()));
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
            List<Tuple2<String, String>> removedList = auditScopeList.stream().filter(t2 -> Objects.equals(t2.v1(), auditDisplayEntity.getAuditId())).collect(Collectors.toList());
            auditScopeList.removeAll(removedList);
            auditDisplayEntityList.add(entity);
            auditScopeList.addAll(entity.getScopeIdList().stream().map(scopeId -> new Tuple2<>(entity.getAuditId(), scopeId)).collect(Collectors.toList()));
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
            List<Tuple2<String, String>> removedList = auditScopeList.stream().filter(t2 -> Objects.equals(t2.v1(), auditDisplayEntity.getAuditId())).collect(Collectors.toList());
            auditScopeList.removeAll(removedList);
            return 1;
        }
        else {
            return 0;
        }
    }

    @Override
    public List<AuditDisplayEntity> findListByScopeId(List<String> scopeIdList) {
        return auditDisplayEntityList.stream().filter(auditDisplayEntity -> auditDisplayEntity.getScopeIdList().containsAll(scopeIdList)).peek(auditDisplayEntity -> {
            List<String> scopeIdList0 = auditScopeList.stream().filter(t2 -> Objects.equals(t2.v1(), auditDisplayEntity.getAuditId())).map(Tuple2::v2).collect(Collectors.toList());
            auditDisplayEntity.setScopeIdList(scopeIdList0);
        }).collect(Collectors.toList());
    }

    @Override
    public List<AuditDisplayEntity> findListByContextName(String contextName) {
        return auditDisplayEntityList.stream().filter(auditDisplayEntity -> Objects.equals(auditDisplayEntity.getContextName(), contextName)).peek(auditDisplayEntity -> {
            List<String> scopeIdList = auditScopeList.stream().filter(t2 -> Objects.equals(t2.v1(), auditDisplayEntity.getAuditId())).map(Tuple2::v2).collect(Collectors.toList());
            auditDisplayEntity.setScopeIdList(scopeIdList);
        }).collect(Collectors.toList());
    }

    public List<AuditDisplayEntity> findAllList() {
        return auditDisplayEntityList;
    }

    public void showAllData() {
        logger.debug("showAllData: " + auditDisplayEntityList.size());
        auditDisplayEntityList.forEach(auditDisplayEntity -> logger.debug(auditDisplayEntity.toString()));

    }
}
