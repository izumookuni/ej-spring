package cc.domovoi.spring.test.service;

import cc.domovoi.spring.service.audit.AuditServiceInterface;
import cc.domovoi.spring.service.audit.GeneralAuditServiceInterface;
import cc.domovoi.spring.service.mvc.StandardMvcSingletonServiceInterface;
import cc.domovoi.spring.test.entity.AuditBeanEntityTestImpl;
import cc.domovoi.spring.test.mapper.AuditBeanMapperTestImpl;
import cc.domovoi.tools.utils.RandomUtils;

public class AuditBeanServiceTestImpl implements StandardMvcSingletonServiceInterface<AuditBeanEntityTestImpl, AuditBeanMapperTestImpl>, GeneralAuditServiceInterface<String, AuditBeanEntityTestImpl> {

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
