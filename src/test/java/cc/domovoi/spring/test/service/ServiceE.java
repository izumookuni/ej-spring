package cc.domovoi.spring.test.service;

import cc.domovoi.spring.service.BaseRetrieveJoiningServiceInterface;
import cc.domovoi.spring.test.entity.EntityE;
import cc.domovoi.spring.test.mapper.MapperE;

import java.util.Collections;
import java.util.Map;

public class ServiceE implements BaseRetrieveJoiningServiceInterface<EntityE, MapperE> {

    private MapperE mapper;

    private ServiceF serviceF;

    public ServiceE(MapperE mapper, ServiceF serviceF) {
        this.mapper = mapper;
        this.serviceF = serviceF;
    }

    @Override
    public Map<String, BaseRetrieveJoiningServiceInterface> joiningService() {
        return Collections.singletonMap("f", serviceF);
    }

    @Override
    public MapperE mvcMapper() {
        return mapper;
    }
}
