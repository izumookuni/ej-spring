package cc.domovoi.spring.test.mapper;

import cc.domovoi.spring.test.entity.EntityD;

import java.util.ArrayList;

public class MapperD extends StandardMapper<EntityD> {

    public MapperD() {
        entityList = new ArrayList<>();
        entityList.add(new EntityD("d1"));
        entityList.add(new EntityD("d2"));
        entityList.add(new EntityD("d3"));
        entityList.add(new EntityD("d4"));
        entityList.add(new EntityD("d5"));
    }
}
