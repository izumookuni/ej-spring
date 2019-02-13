package cc.domovoi.spring.entity;

import cc.domovoi.spring.format.json.LocalDateTimeFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;

/**
 * BaseJoiningEntityInterface.
 */
public interface BaseJoiningEntityInterface extends GeneralJoiningEntityInterface<String, LocalDateTime> {

    /**
     * The creation time of this entity.
     *
     * @return The creation time of this entity.
     */
    @JsonSerialize(using = LocalDateTimeFormatter.class)
    LocalDateTime getCreationTime();

    /**
     * The update time of this row.
     *
     * @return The update time of this row.
     */
    @JsonSerialize(using = LocalDateTimeFormatter.class)
    LocalDateTime getUpdateTime();
}
