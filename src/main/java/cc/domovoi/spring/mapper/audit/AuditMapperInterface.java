package cc.domovoi.spring.mapper.audit;

import cc.domovoi.spring.entity.audit.AuditDisplayEntity;

import java.util.List;

public interface AuditMapperInterface {

    AuditDisplayEntity findEntityById(String id);

    List<AuditDisplayEntity> findList(AuditDisplayEntity entity);

    List<AuditDisplayEntity> findListById(List<String> idList);

    Integer addEntity(AuditDisplayEntity entity);

    Integer updateEntity(AuditDisplayEntity entity);

    Integer deleteEntity(AuditDisplayEntity entity);

    List<AuditDisplayEntity> findListByScopeId(String scopeId);
}
