package cc.domovoi.spring.mapper.audit;

import cc.domovoi.spring.entity.audit.AuditDisplayEntity;

import java.util.List;

public interface AuditMapperInterface {

    AuditDisplayEntity findEntityById(String id);

    List<AuditDisplayEntity> findAuditList(AuditDisplayEntity entity);

    List<AuditDisplayEntity> findAuditListById(List<String> idList);

    Integer addEntity(AuditDisplayEntity entity);

    Integer updateEntity(AuditDisplayEntity entity);

    Integer deleteEntity(AuditDisplayEntity entity);

    List<AuditDisplayEntity> findListByScopeId(List<String> scopeIdList);

    List<AuditDisplayEntity> findListByContextName(String contextName);
}
