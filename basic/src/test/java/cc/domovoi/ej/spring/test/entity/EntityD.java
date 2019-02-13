package cc.domovoi.ej.spring.test.entity;

import cc.domovoi.ej.spring.entity.BaseEntityInterface;

import java.time.LocalDateTime;

public class EntityD extends BaseEntity implements BaseEntityInterface {

    @Override
    public String toString() {
        return "EntityD{" +
                "id='" + id + '\'' +
                '}';
    }

    public EntityD() {
    }

    public EntityD(String id) {
        this.id = id;
        LocalDateTime now = LocalDateTime.now();
        this.creationTime = now;
        this.updateTime = now;
    }
}
