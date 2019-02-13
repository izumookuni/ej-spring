package cc.domovoi.ej.spring.test.service;

import cc.domovoi.ej.spring.service.BaseRetrieveServiceInterface;
import cc.domovoi.ej.spring.test.entity.EntityD;
import cc.domovoi.ej.spring.test.mapper.MapperD;

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
