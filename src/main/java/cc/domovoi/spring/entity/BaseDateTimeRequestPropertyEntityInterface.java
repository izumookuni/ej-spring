package cc.domovoi.spring.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    default LocalDate getDateRangeFrom() {
        return getTimeRangeFrom() != null ? getTimeRangeFrom().toLocalDate() : null;
    }

    default void setDateRangeFrom(LocalDate dateRangeFrom) {
        if (dateRangeFrom != null) {
            setTimeRangeFrom(LocalDateTime.of(dateRangeFrom, LocalTime.MIDNIGHT));
        }
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    default LocalDate getDateRangeUntil() {
        return getTimeRangeUntil() != null ? getTimeRangeUntil().toLocalDate() : null;
    }

    default void setDateRangeUntil(LocalDate dateRangeUntil) {
        if (dateRangeUntil != null) {
            setTimeRangeUntil(LocalDateTime.of(dateRangeUntil, LocalTime.MIDNIGHT));
        }
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    default LocalDate getDateRangeTo() {
        return getTimeRangeUntil() != null ? getTimeRangeUntil().toLocalDate().minusDays(1L) : null;
    }

    default void setDateRangeTo(LocalDate dateRangeTo) {
        if (dateRangeTo != null) {
            setTimeRangeUntil(LocalDateTime.of(dateRangeTo.plusDays(1L), LocalTime.MIDNIGHT));
        }
    }
}
