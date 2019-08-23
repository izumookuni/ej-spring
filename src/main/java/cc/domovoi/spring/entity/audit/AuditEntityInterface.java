package cc.domovoi.spring.entity.audit;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface AuditEntityInterface {

    String getAuditId();

    void setAuditId(String auditId);

    String getAuditAuthor();

    void setAuditAuthor(String auditAuthor);

    String getAuditIp();

    void setAuditIp(String auditIp);

    String getAuditUri();

    void setAuditUri(String auditUri);

    String getAuditLevel();

    void setAuditLevel(String auditLevel);

    String getAuditType();

    void setAuditType(String auditType);

    String getAuditScope();

    void setAuditScope(String auditScope);

    String getAuditBehavior();

    void setAuditBehavior(String auditBehavior);

    String getContextId();

    void setContextId(String contextId);

    String getContextPid();

    void setContextPid(String contextPid);

    String getContextName();

    void setContextName(String contextName);

    String getAuditField();

    void setAuditField(String auditField);

    String getAuditContent();

    void setAuditContent(String auditContent);

    LocalDateTime getAuditTime();

    void setAuditTime(LocalDateTime auditTime);

    @Deprecated
    default Map<String, Supplier<String>> auditContentMap() {
        return Collections.emptyMap();
    }

    @Deprecated
    default void init() {
        if (Objects.isNull(getAuditContent()) && !auditContentMap().isEmpty()) {
            String content = auditContentMap().entrySet().stream().map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue().get())).collect(Collectors.joining("|~|", "{", "}"));
            setAuditContent(content);
        }
        else if (Objects.isNull(getAuditContent())) {
            setAuditContent(this.toString());
        }
        setAuditId(UUID.randomUUID().toString());
        setAuditTime(LocalDateTime.now());
    }

    @Deprecated
    default AuditDisplayEntity asAuditDisplayEntity() {
        if (this instanceof AuditDisplayEntity) {
            return (AuditDisplayEntity) this;
        }
        AuditDisplayEntity auditDisplayEntity = new AuditDisplayEntity();
        auditDisplayEntity.setAuditId(this.getAuditId());
        auditDisplayEntity.setAuditAuthor(this.getAuditAuthor());
        auditDisplayEntity.setAuditIp(this.getAuditIp());
        auditDisplayEntity.setAuditUri(this.getAuditUri());
        auditDisplayEntity.setAuditLevel(this.getAuditLevel());
        auditDisplayEntity.setAuditType(this.getAuditType());
        auditDisplayEntity.setAuditScope(this.getAuditScope());
        auditDisplayEntity.setAuditBehavior(this.getAuditBehavior());
        auditDisplayEntity.setContextId(this.getContextId());
        auditDisplayEntity.setContextPid(this.getContextPid());
        auditDisplayEntity.setContextName(this.getContextName());
        auditDisplayEntity.setAuditField(this.getAuditField());
        auditDisplayEntity.setAuditContent(this.getAuditContent());
        auditDisplayEntity.setAuditTime(this.getAuditTime());
        return auditDisplayEntity;
    }
}
