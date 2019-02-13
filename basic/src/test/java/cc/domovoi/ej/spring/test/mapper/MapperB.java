package cc.domovoi.ej.spring.test.mapper;

import cc.domovoi.ej.spring.test.entity.EntityB;

import java.util.ArrayList;

public class MapperB extends BaseMapper<EntityB> {

    public MapperB() {
        entityList = new ArrayList<>();
        entityList.add(new EntityB("b1"));
        entityList.add(new EntityB("b2"));
        entityList.add(new EntityB("b3"));
        entityList.add(new EntityB("b4"));
        entityList.add(new EntityB("b5"));
    }


}
