package cc.domovoi.spring.test.service;

import cc.domovoi.spring.service.BaseServiceInterface;
import cc.domovoi.spring.service.audit.AuditServiceInterface;
import cc.domovoi.spring.service.audit.BaseAuditServiceInterface;
import cc.domovoi.spring.test.entity.AuditBeanEntityTestImpl;
import cc.domovoi.spring.test.mapper.AuditBeanMapperTestImpl;
import cc.domovoi.tools.utils.RandomUtils;

public class AuditBeanServiceTestImpl implements BaseServiceInterface<AuditBeanEntityTestImpl, AuditBeanMapperTestImpl>, BaseAuditServiceInterface<AuditBeanEntityTestImpl, AuditBeanMapperTestImpl> {

    private AuditBeanMapperTestImpl auditBeanMapperTestImpl;

    private AuditServiceInterface auditService;

    public AuditBeanServiceTestImpl(AuditBeanMapperTestImpl auditBeanMapperTestImpl, AuditServiceInterface auditService) {
        this.auditBeanMapperTestImpl = auditBeanMapperTestImpl;
        this.auditService = auditService;
    }

    @Override
    public AuditServiceInterface auditService() {
        return auditService;
    }

    @Override
    public String auditAuthorGetter() {
        return RandomUtils.randomString();
    }

    @Override
    public Class<AuditBeanEntityTestImpl> auditClass() {
        return AuditBeanEntityTestImpl.class;
    }

    @Override
    public AuditBeanMapperTestImpl mapper() {
        return auditBeanMapperTestImpl;
    }
}