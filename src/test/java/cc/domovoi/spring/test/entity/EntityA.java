package cc.domovoi.spring.test.entity;

import cc.domovoi.spring.entity.StandardSingletonEntityInterface;

import java.time.LocalDateTime;

public class EntityA extends StandardEntity implements StandardSingletonEntityInterface {

    @Override
    public String toString() {
        return "EntityA{" +
                "id='" + id + '\'' +
                '}';
    }

    public EntityA() {
    }

    public EntityA(String id) {
        this.id = id;
        LocalDateTime now = LocalDateTime.now();
        this.creationTime = now;
        this.updateTime = now;
    }
}
