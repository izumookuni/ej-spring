package cc.domovoi.spring.test.entity;

import cc.domovoi.spring.entity.StandardSingletonEntityInterface;
import cc.domovoi.spring.entity.audit.Audit;
import cc.domovoi.spring.entity.audit.AuditEntityInterface;
import cc.domovoi.spring.entity.audit.AuditRecord;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.Objects;

//@Audit(value = "日志记录测试", skip = "v5")
@Audit(value = "日志记录测试",
        skip = "v5",
        record = {
                @AuditRecord(target = "v0", value = "值0", key = "VALUE0"),
                @AuditRecord(target = "v1", value = "值1", key = "VALUE1"),
                @AuditRecord(target = "v2"),
                @AuditRecord(target = "v3"),
                @AuditRecord(target = "v4", value = "值4", key = "VALUE4"),
                @AuditRecord(target = "v5", value = "值5"),
                @AuditRecord(target = "v6", value = "值6"),
        })
public class AuditBeanEntityTestImpl extends AuditBeanEntityTestImpl0 implements StandardSingletonEntityInterface, AuditEntityInterface {

    private String id;

//    @AuditRecord(value = "值1", key = "VALUE1")
    private String v1;

//    @AuditRecord
    @ApiModelProperty("值2")
    private Integer v2;

//    @AuditRecord("值3")
    private Double v3;

//    @AuditRecord(value = "值4", key = "VALUE4")
    private Boolean v4;

//    @AuditRecord("值5")
    private LocalDateTime v5;

    private LocalDateTime creationTime;

    private LocalDateTime updateTime;

    private Boolean available;

    @Override
    public Boolean getAvailable() {
        return available;
    }

    @Override
    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditBeanEntityTestImpl that = (AuditBeanEntityTestImpl) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AuditBeanEntityTestImpl{" +
                "id='" + id + '\'' +
                ", v0='" + getV0() + '\'' +
                ", v1='" + v1 + '\'' +
                ", v2=" + v2 +
                ", v3=" + v3 +
                ", v4=" + v4 +
                ", v5=" + v5 +
                ", creationTime=" + creationTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public AuditBeanEntityTestImpl() {
    }

    public AuditBeanEntityTestImpl(String v1, Integer v2, Double v3, Boolean v4, LocalDateTime v5) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
    }

    public AuditBeanEntityTestImpl(String v0, String v1, Integer v2, Double v3, Boolean v4, LocalDateTime v5) {
        super(v0);
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getV1() {
        return v1;
    }

    public void setV1(String v1) {
        this.v1 = v1;
    }

    public Integer getV2() {
        return v2;
    }

    public void setV2(Integer v2) {
        this.v2 = v2;
    }

    public Double getV3() {
        return v3;
    }

    public void setV3(Double v3) {
        this.v3 = v3;
    }

    public Boolean getV4() {
        return v4;
    }

    public void setV4(Boolean v4) {
        this.v4 = v4;
    }

    public LocalDateTime getV5() {
        return v5;
    }

    public void setV5(LocalDateTime v5) {
        this.v5 = v5;
    }

    @Override
    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    @Override
    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
