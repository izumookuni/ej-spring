package cc.domovoi.ej.spring.test.entity;

import cc.domovoi.ej.spring.entity.BaseEntityInterface;

import java.time.LocalDateTime;

public class EntityA extends BaseEntity implements BaseEntityInterface {

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
