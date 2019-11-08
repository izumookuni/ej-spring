package cc.domovoi.spring.test.service;

import cc.domovoi.spring.service.BaseRetrieveServiceInterface;
import cc.domovoi.spring.test.entity.EntityA;
import cc.domovoi.spring.test.mapper.MapperA;

public class ServiceA implements BaseRetrieveServiceInterface<EntityA, MapperA> {

    private MapperA mapper;

    public ServiceA(MapperA mapper) {
        this.mapper = mapper;
    }

    @Override
    public MapperA mvcMapper() {
        return mapper;
    }
}
