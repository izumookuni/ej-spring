package cc.domovoi.spring.test.service;

import cc.domovoi.spring.service.BaseRetrieveJoiningServiceInterface;
import cc.domovoi.spring.test.entity.EntityC;
import cc.domovoi.spring.test.mapper.MapperC;

import java.util.HashMap;
import java.util.Map;

public class ServiceC implements BaseRetrieveJoiningServiceInterface<EntityC, MapperC> {

    private MapperC mapper;

    private ServiceD serviceD;

    private ServiceE serviceE;

    public ServiceC(MapperC mapper, ServiceD serviceD, ServiceE serviceE) {
        this.mapper = mapper;
        this.serviceD = serviceD;
        this.serviceE = serviceE;
    }

    @Override
    public Map<String, BaseRetrieveJoiningServiceInterface> joiningService() {
        Map<String, BaseRetrieveJoiningServiceInterface> joiningService = new HashMap<>();
        joiningService.put("d", serviceD);
        joiningService.put("e", serviceE);
        return joiningService;
    }

    @Override
    public MapperC mvcMapper() {
        return mapper;
    }
}
