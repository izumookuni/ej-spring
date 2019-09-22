package cc.domovoi.spring.test.mapper;

import cc.domovoi.spring.test.entity.EntityA;

import java.util.ArrayList;

public class MapperA extends StandardMapper<EntityA> {

    public MapperA() {
        entityList = new ArrayList<>();
        entityList.add(new EntityA("a1"));
        entityList.add(new EntityA("a2"));
        entityList.add(new EntityA("a3"));
        entityList.add(new EntityA("a4"));
        entityList.add(new EntityA("a5"));
    }
}
