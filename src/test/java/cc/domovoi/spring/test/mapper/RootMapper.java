package cc.domovoi.spring.test.mapper;

import cc.domovoi.spring.test.entity.RootEntity;

import java.util.ArrayList;

public class RootMapper extends StandardMapper<RootEntity> {

    public RootMapper() {
        entityList = new ArrayList<>();
        entityList.add(new RootEntity("root1", "a1", "b2", "c3"));
        entityList.add(new RootEntity("root2", "a1", "b2", "c3"));
        entityList.add(new RootEntity("root3", "a1", "b2", "c3"));
        entityList.add(new RootEntity("root4", "a1", "b2", "c3"));
        entityList.add(new RootEntity("root5", "a1", "b2", "c3"));
    }
}
