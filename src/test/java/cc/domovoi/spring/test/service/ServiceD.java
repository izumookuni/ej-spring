package cc.domovoi.spring.test.service;

import cc.domovoi.spring.service.BaseRetrieveServiceInterface;
import cc.domovoi.spring.test.entity.EntityD;
import cc.domovoi.spring.test.mapper.MapperD;

public class ServiceD implements BaseRetrieveServiceInterface<EntityD, MapperD> {

    private MapperD mapper;

    public ServiceD(MapperD mapper) {
        this.mapper = mapper;
    }

    @Override
    public MapperD mapper() {
        return mapper;
    }
}
