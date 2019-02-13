package cc.domovoi.ej.spring.test.service;

import cc.domovoi.ej.spring.service.BaseRetrieveServiceInterface;
import cc.domovoi.ej.spring.test.entity.EntityA;
import cc.domovoi.ej.spring.test.mapper.MapperA;

public class ServiceA implements BaseRetrieveServiceInterface<EntityA, MapperA> {

    private MapperA mapper;

    public ServiceA(MapperA mapper) {
        this.mapper = mapper;
    }

    @Override
    public MapperA mapper() {
        return mapper;
    }
}
