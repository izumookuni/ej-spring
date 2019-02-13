package cc.domovoi.ej.spring.test.entity;

import cc.domovoi.ej.spring.entity.BaseEntityInterface;

import java.time.LocalDateTime;

public class EntityB extends BaseEntity implements BaseEntityInterface {

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
