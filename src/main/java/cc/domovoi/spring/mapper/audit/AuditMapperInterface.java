package cc.domovoi.spring.mapper.audit;

import cc.domovoi.spring.entity.audit.AuditDisplayEntity;
import cc.domovoi.spring.entity.audit.AuditEntityInterface;

import java.util.List;

public interface AuditMapperInterface {

    AuditDisplayEntity findEntityById(String id);

    List<AuditDisplayEntity> findList(AuditEntityInterface entity);

    List<AuditDisplayEntity> findListById(List<String> idList);

    Integer addEntity(AuditEntityInterface entity);

    Integer updateEntity(AuditEntityInterface entity);

    Integer deleteEntity(AuditEntityInterface entity);
}
