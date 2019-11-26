package cc.domovoi.spring.test.entity;

import cc.domovoi.spring.entity.StandardSingletonEntityInterface;
import cc.domovoi.spring.entity.annotation.JoiningColumn;
import cc.domovoi.spring.test.JoiningColumnFunctionTest;
import org.jooq.impl.TableImpl;

import java.time.LocalDateTime;

public class EntityA extends StandardEntity implements StandardSingletonEntityInterface {

    @JoiningColumn(foreignKey = "", targetKey = "", table = TableImpl.class, custom = JoiningColumnFunctionTest.class)
    private String haha;

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
