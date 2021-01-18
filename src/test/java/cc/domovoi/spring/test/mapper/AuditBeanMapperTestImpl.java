package cc.domovoi.spring.test.mapper;

import cc.domovoi.spring.mapper.StandardMapperInterface;
import cc.domovoi.spring.test.entity.AuditBeanEntityTestImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AuditBeanMapperTestImpl implements StandardMapperInterface<AuditBeanEntityTestImpl> {

    private static Logger logger = LoggerFactory.getLogger(AuditMapperTestImpl.class);

    private List<AuditBeanEntityTestImpl> auditBeanEntityTestImplList;

    public AuditBeanMapperTestImpl() {
        auditBeanEntityTestImplList = new ArrayList<>();
    }

    @Override
    public Integer add(AuditBeanEntityTestImpl entity) {
        logger.debug("addBase: " + entity);
        if (findById(entity.getId()) != null) {
            auditBeanEntityTestImplList.add(entity);
            return 1;
        }
        else {
            return 0;
        }
    }

    @Override
    public Integer update(AuditBeanEntityTestImpl entity) {
        logger.debug("updateBase: " + entity);
        AuditBeanEntityTestImpl auditDisplayEntityImpl = findById(entity.getId());
        if (auditDisplayEntityImpl != null) {
            auditBeanEntityTestImplList.remove(auditDisplayEntityImpl);
            auditBeanEntityTestImplList.add(entity);
            return 1;
        }
        else {
            return 0;
        }
    }

    @Override
    public Integer updateForced(AuditBeanEntityTestImpl entity) {
        return update(entity);
    }

    @Override
    public Integer updateSetNull(AuditBeanEntityTestImpl entity, List<String> setNull) {
        return update(entity);
    }

    @Override
    public Integer delete(AuditBeanEntityTestImpl entity) {
        logger.debug("deleteBase: " + entity);
        AuditBeanEntityTestImpl auditDisplayEntityImpl = findById(entity.getId());
        if (auditDisplayEntityImpl != null) {
            auditBeanEntityTestImplList.remove(auditDisplayEntityImpl);
            return 1;
        }
        else {
            return 0;
        }
    }

    @Override
    public Integer deleteById(List<String> idList) {
        logger.debug("deleteById: " + idList);
        List<AuditBeanEntityTestImpl> auditDisplayEntityImplList = findListById(idList);
        auditBeanEntityTestImplList.removeAll(auditDisplayEntityImplList);
        return auditDisplayEntityImplList.size();
    }

    @Override
    public AuditBeanEntityTestImpl findById(String id) {
        return auditBeanEntityTestImplList.stream().filter(auditBeanEntityTestImpl -> Objects.equals(auditBeanEntityTestImpl.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<AuditBeanEntityTestImpl> findListById(List<String> idList) {
        return auditBeanEntityTestImplList.stream().filter(auditBeanEntityTestImpl -> idList.contains(auditBeanEntityTestImpl.getId())).collect(Collectors.toList());
    }

    @Override
    public List<AuditBeanEntityTestImpl> findList(AuditBeanEntityTestImpl entity) {
        return auditBeanEntityTestImplList.stream().filter(auditBeanEntityTestImpl -> Objects.equals(auditBeanEntityTestImpl, entity)).collect(Collectors.toList());
    }
}
