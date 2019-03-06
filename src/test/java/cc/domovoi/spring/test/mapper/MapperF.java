package cc.domovoi.spring.test.mapper;

import cc.domovoi.spring.test.entity.EntityF;

import java.util.ArrayList;

public class MapperF extends BaseMapper<EntityF> {

    public MapperF() {
        entityList = new ArrayList<>();
        entityList.add(new EntityF("f1"));
        entityList.add(new EntityF("f2"));
        entityList.add(new EntityF("f3"));
        entityList.add(new EntityF("f4"));
        entityList.add(new EntityF("f5"));
    }
}
