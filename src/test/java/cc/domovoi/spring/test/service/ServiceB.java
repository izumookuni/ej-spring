package cc.domovoi.spring.test.service;

import cc.domovoi.spring.service.BaseRetrieveServiceInterface;
import cc.domovoi.spring.test.entity.EntityB;
import cc.domovoi.spring.test.mapper.MapperB;

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
