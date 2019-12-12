package cc.domovoi.spring.entity.audit;

import org.springframework.beans.BeanUtils;

public interface AuditInterface {

    default AuditDisplayEntity toAuditDisplayEntity() {
        if (this instanceof AuditDisplayEntity) {
            return (AuditDisplayEntity) this;
        }
        else {
            AuditDisplayEntity auditDisplayEntity = new AuditDisplayEntity();
            BeanUtils.copyProperties(this, auditDisplayEntity);
            return auditDisplayEntity;
        }
    }

    default void fromAuditDisplayEntity(AuditDisplayEntity auditDisplayEntity) {
        BeanUtils.copyProperties(auditDisplayEntity, this);
    }
}
