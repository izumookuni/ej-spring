package cc.domovoi.spring.service.audit;

import cc.domovoi.spring.entity.audit.AuditDisplayEntity;
import cc.domovoi.spring.entity.audit.AuditEntityInterface;
import cc.domovoi.spring.mapper.audit.AuditMapperInterface;
import cc.domovoi.spring.utils.ServiceUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Function;

public interface AuditServiceInterface {

    AuditMapperInterface auditMapper();

    default AuditDisplayEntity findAudit(String id) {
        return auditMapper().findEntityById(id);
    }

    default List<AuditDisplayEntity> findAuditList(AuditEntityInterface entity) {
        return auditMapper().findList(entity);
    }

    default List<AuditDisplayEntity> findAuditListById(List<String> idList) {
        return ServiceUtils.findListGrouped(auditMapper(), idList, Function.identity(), (l, r) -> r, AuditMapperInterface::findListById);
    }

    default Integer addEntity(AuditEntityInterface entity) {
        entity.init();
        return auditMapper().addEntity(entity);
    }

    default Integer updateEntity(AuditEntityInterface entity) {
        if (!StringUtils.hasText(entity.getAuditId())) {
            return -1;
        }
        return auditMapper().updateEntity(entity);
    }

    default Integer deleteEntity(AuditEntityInterface entity) {
        if (!StringUtils.hasText(entity.getAuditId()) && !StringUtils.hasText(entity.getAuditAuthor()) && !StringUtils.hasText(entity.getAuditIp())) {
            return -1;
        }
        return auditMapper().deleteEntity(entity);
    }
}
