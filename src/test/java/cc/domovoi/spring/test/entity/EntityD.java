package cc.domovoi.spring.test.entity;

import cc.domovoi.spring.entity.StandardSingletonEntityInterface;

import java.time.LocalDateTime;

public class EntityD extends StandardEntity implements StandardSingletonEntityInterface {

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
