package cc.domovoi.ej.spring.entity;

import cc.domovoi.ej.spring.json.LocalDateTimeFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
