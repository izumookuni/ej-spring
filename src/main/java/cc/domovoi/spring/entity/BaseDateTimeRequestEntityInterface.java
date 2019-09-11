package cc.domovoi.spring.entity;

import cc.domovoi.collection.util.Try;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.CaseFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public interface BaseDateTimeRequestEntityInterface {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String getCustomTime();

    void setCustomTime(String customTime);

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String getCustomTimeFormat();

    void setCustomTimeFormat(String customTimeFormat);

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String getCustomTimeField();

    void setCustomTimeField(String customTimeField);

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    LocalDate getCreationDate();

    void setCreationDate(LocalDate creationDate);

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    LocalDate getUpdateDate();

    void setUpdateDate(LocalDate updateDate);

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    LocalDateTime getTimeRangeFrom();

    void setTimeRangeFrom(LocalDateTime timeRangeFrom);

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    LocalDateTime getTimeRangeUntil();

    void setTimeRangeUntil(LocalDateTime timeRangeUntil);

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String getTimeRangeField();

    void setTimeRangeField(String timeRangeField);

    default Boolean checkCustomTimeField(String key) {
        return false;
    }

    default Boolean checkTimeRangeField(String key) {
        return false;
    }

    default LocalDateTime getCustomTimeCertified() {
        try {
            return getCustomTime() != null && getCustomTimeFormat() != null && getCustomTimeField() != null ?
                    LocalDateTime.parse(getCustomTime(), DateTimeFormatter.ofPattern(getCustomTimeFormat())) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    default String getCustomTimeFieldCertified() {
        return getCustomTimeField() != null && ("creationTime".equals(getCustomTimeField()) || "updateTime".equals(getCustomTimeField()) || checkCustomTimeField(getCustomTimeField())) ?
                CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE).convert(getCustomTimeField()) : null;
    }

    default String getTimeRangeFieldCertified() {
        return getTimeRangeField() != null && ("creationTime".equals(getTimeRangeField()) || "updateTime".equals(getTimeRangeField()) || checkTimeRangeField(getTimeRangeField())) ?
                CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE).convert(getTimeRangeField()) : null;
    }

    default Integer getTimeRangeYear() {
        Integer year1 = getTimeRangeFrom() != null ? getTimeRangeFrom().getYear() : null;
        Integer year2 = getTimeRangeUntil() != null ? getTimeRangeUntil().getYear() : null;
        if (year1 != null && year2 != null && year1 == year2 - 1) {
            return year1;
        }
        else {
            return null;
        }
    }

    default void setTimeRangeYear(Integer timeRangeYear) {
        if (timeRangeYear != null) {
            setTimeRangeFrom(LocalDateTime.of(timeRangeYear, 1, 1, 0, 0, 0));
            setTimeRangeUntil(LocalDateTime.of(timeRangeYear + 1, 1, 1, 0, 0, 0));
        }
    }

    default String getTimeRangeYearMonth() {
        if (getTimeRangeFrom() != null && getTimeRangeUntil() != null &&
                ((getTimeRangeFrom().getYear() == getTimeRangeUntil().getYear() && getTimeRangeFrom().getMonthValue() == getTimeRangeUntil().getMonthValue() - 1) ||
                        (getTimeRangeFrom().getYear() == getTimeRangeUntil().getYear() - 1) && getTimeRangeFrom().getMonthValue() == 12 && getTimeRangeUntil().getMonthValue() == 1)) {
            if (getCustomTimeFormat() != null) {
                try {
                    return getTimeRangeFrom().format(DateTimeFormatter.ofPattern(getCustomTimeFormat()));
                } catch (Exception e) {
                    e.printStackTrace();
                    return getTimeRangeFrom().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                }
            }
            else {
                return getTimeRangeFrom().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            }
        }
        else {
            return null;
        }
    }

    default void setTimeRangeYearMonth(String timeRangeYearMonth) {
        if (timeRangeYearMonth != null) {
            Try.apply(() -> {
                if (getCustomTimeFormat() != null) {
                    return LocalDateTime.parse(timeRangeYearMonth, DateTimeFormatter.ofPattern(getCustomTimeFormat()));
                }
                else {
                    return LocalDateTime.parse(timeRangeYearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
                }
            }).foreach(t -> {
                setTimeRangeFrom(t);
                setTimeRangeUntil(t.plusMonths(1L));
            });
        }
    }

    default LocalDate getTimeRangeDate() {
        LocalDate date1 = getTimeRangeFrom() != null ? getTimeRangeFrom().toLocalDate() : null;
        LocalDate date2 = getTimeRangeUntil() != null ? getTimeRangeUntil().toLocalDate() : null;
        if (date1 != null && date2 != null && date1 == date2.minusDays(1L)) {
            return date1;
        }
        else {
            return null;
        }
    }

    default void setTimeRangeDate(LocalDate timeRangeDate) {
        if (timeRangeDate != null) {
            setTimeRangeFrom(LocalDateTime.of(timeRangeDate, LocalTime.MIDNIGHT));
            setTimeRangeUntil(LocalDateTime.of(timeRangeDate.plusDays(1L), LocalTime.MIDNIGHT));
        }
    }
}
