package cc.domovoi.ej.spring.test.entity;

import cc.domovoi.ej.spring.entity.BaseEntityInterface;

import java.time.LocalDateTime;

public class EntityF extends BaseEntity implements BaseEntityInterface {

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
