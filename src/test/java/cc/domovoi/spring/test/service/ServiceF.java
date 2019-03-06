package cc.domovoi.spring.test.service;

import cc.domovoi.spring.service.BaseRetrieveServiceInterface;
import cc.domovoi.spring.test.entity.EntityF;
import cc.domovoi.spring.test.mapper.MapperF;

public class ServiceF implements BaseRetrieveServiceInterface<EntityF, MapperF> {

    private MapperF mapper;

    public ServiceF(MapperF mapper) {
        this.mapper = mapper;
    }

    @Override
    public MapperF mapper() {
        return mapper;
    }
}
