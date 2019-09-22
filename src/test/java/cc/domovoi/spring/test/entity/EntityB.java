package cc.domovoi.spring.test.entity;

import cc.domovoi.spring.entity.StandardSingletonEntityInterface;

import java.time.LocalDateTime;

public class EntityB extends StandardEntity implements StandardSingletonEntityInterface {

    @Override
    public String toString() {
        return "EntityB{" +
                "id='" + id + '\'' +
                '}';
    }

    public EntityB() {
    }

    public EntityB(String id) {
        this.id = id;
        LocalDateTime now = LocalDateTime.now();
        this.creationTime = now;
        this.updateTime = now;
    }
}
