package cc.domovoi.spring.test.entity;

import cc.domovoi.spring.entity.StandardJoiningEntityInterface;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class StandardEntity implements StandardJoiningEntityInterface {

    protected String id;

    protected LocalDateTime creationTime;

    protected LocalDateTime updateTime;

    private Boolean available;

    public StandardEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StandardEntity that = (StandardEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public Boolean getAvailable() {
        return available;
    }

    @Override
    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
