package cc.domovoi.spring.entity;

import cc.domovoi.spring.entity.GeneralPropertyEntityInterface;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface BaseDateTimeRequestPropertyEntityInterface extends BaseDateTimeRequestEntityInterface, GeneralPropertyEntityInterface {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Override
    default String getCustomTime() {
        return (String) propertyMap().get("customTime");
    }

    @Override
    default void setCustomTime(String customTime) {
        propertyMap().put("customTime", customTime);
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Override
    default String getCustomTimeFormat() {
        return (String) propertyMap().get("customTimeFormat");
    }

    @Override
    default void setCustomTimeFormat(String customTimeFormat) {
        propertyMap().put("customTimeFormat", customTimeFormat);
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Override
    default String getCustomTimeField() {
        return (String) propertyMap().get("customTimeField");
    }

    @Override
    default void setCustomTimeField(String customTimeField) {
        propertyMap().put("customTimeField", customTimeField);
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Override
    default LocalDate getCreationDate() {
        return (LocalDate) propertyMap().get("creationDate");
    }

    @Override
    default void setCreationDate(LocalDate creationDate) {
        propertyMap().put("creationDate", creationDate);
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Override
    default LocalDate getUpdateDate() {
        return (LocalDate) propertyMap().get("updateDate");
    }

    @Override
    default void setUpdateDate(LocalDate updateDate) {
        propertyMap().put("updateDate", updateDate);
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Override
    default LocalDateTime getTimeRangeFrom() {
        return (LocalDateTime) propertyMap().get("timeRangeFrom");
    }

    @Override
    default void setTimeRangeFrom(LocalDateTime timeRangeFrom) {
        propertyMap().put("timeRangeFrom", timeRangeFrom);
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Override
    default LocalDateTime getTimeRangeUntil() {
        return (LocalDateTime) propertyMap().get("timeRangeUntil");
    }

    @Override
    default void setTimeRangeUntil(LocalDateTime timeRangeUntil) {
        propertyMap().put("timeRangeUntil", timeRangeUntil);
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Override
    default String getTimeRangeField() {
        return (String) propertyMap().get("timeRangeField");
    }

    @Override
    default void setTimeRangeField(String timeRangeField) {
        propertyMap().put("timeRangeField", timeRangeField);
    }
}
