package cc.domovoi.ej.spring.test.mapper;

import cc.domovoi.ej.spring.test.entity.EntityE;

import java.util.ArrayList;

public class MapperE extends BaseMapper<EntityE> {

    public MapperE() {
        entityList = new ArrayList<>();
        entityList.add(new EntityE("e1", "f5"));
        entityList.add(new EntityE("e2", "f4"));
        entityList.add(new EntityE("e3", "f3"));
        entityList.add(new EntityE("e4", "f2"));
        entityList.add(new EntityE("e5", "f1"));
    }
}
