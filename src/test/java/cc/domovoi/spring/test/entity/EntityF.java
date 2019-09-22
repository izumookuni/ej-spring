package cc.domovoi.spring.test.entity;

import cc.domovoi.spring.entity.StandardSingletonEntityInterface;

import java.time.LocalDateTime;

public class EntityF extends StandardEntity implements StandardSingletonEntityInterface {

    public EntityF() {
    }

    public EntityF(String id) {
        this.id = id;
        LocalDateTime now = LocalDateTime.now();
        this.creationTime = now;
        this.updateTime = now;
    }

    @Override
    public String toString() {
        return "EntityF{" +
                "id='" + id + '\'' +
                '}';
    }
}
