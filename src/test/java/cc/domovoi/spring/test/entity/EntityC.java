package cc.domovoi.spring.test.entity;

import cc.domovoi.spring.entity.BaseJoiningEntityInterface;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityC extends BaseEntity implements BaseJoiningEntityInterface {

    private String dId;

    private EntityD dEntity;

    private String eId;

    private EntityE eEntity;

    @Override
    public String toString() {
        return "EntityC{" +
                "dEntity=" + dEntity +
                ", eEntity=" + eEntity +
                ", id='" + id + '\'' +
                '}';
    }

    public EntityC() {
    }

    public EntityC(String id, String dId, String eId) {
        this.id = id;
        this.dId = dId;
        this.eId = eId;
        LocalDateTime now = LocalDateTime.now();
        this.creationTime = now;
        this.updateTime = now;
    }

    @Override
    public Map<String, Supplier<? extends List<String>>> joiningKeyMap() {
        Map<String, Supplier<? extends List<String>>> joiningKeyMap = new HashMap<>();
        joiningKeyMap.put("d", () -> Collections.singletonList(getdId()));
        joiningKeyMap.put("e", () -> Collections.singletonList(geteId()));
        return joiningKeyMap;
    }

    @Override
    public Map<String, Consumer<? super Object>> joiningEntityMap() {
        Map<String, Consumer<? super Object>> joiningEntityMap = new HashMap<>();
        joiningEntityMap.put("d", (e) -> setdEntity((EntityD) e));
        joiningEntityMap.put("e", (e) -> seteEntity((EntityE) e));
        return joiningEntityMap;
    }

    public String getdId() {
        return dId;
    }

    public void setdId(String dId) {
        this.dId = dId;
    }

    public EntityD getdEntity() {
        return dEntity;
    }

    public void setdEntity(EntityD dEntity) {
        this.dEntity = dEntity;
    }

    public String geteId() {
        return eId;
    }

    public void seteId(String eId) {
        this.eId = eId;
    }

    public EntityE geteEntity() {
        return eEntity;
    }

    public void seteEntity(EntityE eEntity) {
        this.eEntity = eEntity;
    }
}
