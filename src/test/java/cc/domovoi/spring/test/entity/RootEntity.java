package cc.domovoi.spring.test.entity;

import cc.domovoi.spring.entity.StandardJoiningEntityInterface;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RootEntity extends StandardEntity implements StandardJoiningEntityInterface {

    private String aId;

    private EntityA aEntity;

    private String bId;

    private EntityB bEntity;

    private String cId;

    private EntityC cEntity;

    @Override
    public String toString() {
        return "RootEntity{" +
                "aEntity=" + aEntity +
                ", bEntity=" + bEntity +
                ", cEntity=" + cEntity +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public Map<String, Supplier<? extends List<String>>> joiningKeyMap() {
        Map<String, Supplier<? extends List<String>>> joiningKeyMap = new HashMap<>();
        joiningKeyMap.put("a", () -> Collections.singletonList(getaId()));
        joiningKeyMap.put("b", () -> Collections.singletonList(getbId()));
        joiningKeyMap.put("c", () -> Collections.singletonList(getcId()));
        return joiningKeyMap;
    }

    @Override
    public Map<String, Consumer<? super Object>> joiningEntityMap() {
        Map<String, Consumer<? super Object>> joiningEntityMap = new HashMap<>();
        joiningEntityMap.put("a", (e) -> setaEntity((EntityA) e));
        joiningEntityMap.put("b", (e) -> setbEntity((EntityB) e));
        joiningEntityMap.put("c", (e) -> setcEntity((EntityC) e));
        return joiningEntityMap;
    }

    public RootEntity() {
    }

    public RootEntity(String id, String aId, String bId, String cId) {
        this.id = id;
        this.aId = aId;
        this.bId = bId;
        this.cId = cId;
        LocalDateTime now = LocalDateTime.now();
        this.creationTime = now;
        this.updateTime = now;
    }

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public EntityA getaEntity() {
        return aEntity;
    }

    public void setaEntity(EntityA aEntity) {
        this.aEntity = aEntity;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public EntityB getbEntity() {
        return bEntity;
    }

    public void setbEntity(EntityB bEntity) {
        this.bEntity = bEntity;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public EntityC getcEntity() {
        return cEntity;
    }

    public void setcEntity(EntityC cEntity) {
        this.cEntity = cEntity;
    }
}
