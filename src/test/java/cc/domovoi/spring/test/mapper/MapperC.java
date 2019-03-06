package cc.domovoi.spring.test.mapper;

import cc.domovoi.spring.test.entity.EntityC;

import java.util.ArrayList;

public class MapperC extends BaseMapper<EntityC> {

    public MapperC() {
        entityList = new ArrayList<>();
        entityList.add(new EntityC("c1", "d2", "e3"));
        entityList.add(new EntityC("c2", "d2", "e3"));
        entityList.add(new EntityC("c3", "d3", "e4"));
        entityList.add(new EntityC("c4", "d3", "e4"));
        entityList.add(new EntityC("c5", "d4", "e5"));
    }
}
