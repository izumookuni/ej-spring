package cc.domovoi.spring.test.service;

import cc.domovoi.spring.mapper.audit.AuditMapperInterface;
import cc.domovoi.spring.service.audit.AuditServiceInterface;
import cc.domovoi.tools.jackson.ObjectMappers;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuditServiceTestImpl implements AuditServiceInterface {

    private static ObjectMapper objectMapper = ObjectMappers.objectMapper;

    private AuditMapperInterface auditMapper;

    public AuditServiceTestImpl(AuditMapperInterface auditMapper) {
        this.auditMapper = auditMapper;
    }

    @Override
    public AuditMapperInterface auditMapper() {
        return auditMapper;
    }

    @Override
    public ObjectMapper objectMapper() {
        return objectMapper;
    }
}
