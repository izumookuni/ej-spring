package cc.domovoi.ej.spring.test.service;

import cc.domovoi.ej.spring.service.BaseRetrieveServiceInterface;
import cc.domovoi.ej.spring.test.entity.EntityB;
import cc.domovoi.ej.spring.test.mapper.MapperB;

public class ServiceB implements BaseRetrieveServiceInterface<EntityB, MapperB> {

    private MapperB mapper;

    public ServiceB(MapperB mapper) {
        this.mapper = mapper;
    }

    @Override
    public MapperB mapper() {
        return mapper;
    }
}
