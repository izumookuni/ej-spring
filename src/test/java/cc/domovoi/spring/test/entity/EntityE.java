package cc.domovoi.spring.test.entity;

import cc.domovoi.spring.entity.StandardJoiningEntityInterface;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityE extends StandardEntity implements StandardJoiningEntityInterface {

    private String fId;

    @Override
    public String toString() {
        return "EntityE{" +
                "fEntity=" + fEntity +
                ", id='" + id + '\'' +
                '}';
    }

    private EntityF fEntity;

    @Override
    public Map<String, Supplier<? extends List<Object>>> joiningKeyMap() {
        return Collections.singletonMap("f", () -> Collections.singletonList(getfId()));
    }

    @Override
    public Map<String, Consumer<? super Object>> joiningEntityMap() {
        return Collections.singletonMap("f", (e) -> setfEntity((EntityF) e));
    }

    public EntityE() {
    }

    public EntityE(String id, String fId) {
        this.id = id;
        this.fId = fId;
        LocalDateTime now = LocalDateTime.now();
        this.creationTime = now;
        this.updateTime = now;
    }

    public String getfId() {
        return fId;
    }

    public void setfId(String fId) {
        this.fId = fId;
    }

    public EntityF getfEntity() {
        return fEntity;
    }

    public void setfEntity(EntityF fEntity) {
        this.fEntity = fEntity;
    }
}
