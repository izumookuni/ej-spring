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
public interface BaseJoiningEntityInterface {

    /**
     * ID.
     *
     * @return ID.
     */
    String getId();

    /**
     * Set ID.
     *
     * @param id ID.
     */
    void setId(String id);

    /**
     * The creation time of this entity.
     *
     * @return The creation time of this entity.
     */
    @JsonSerialize(using = LocalDateTimeFormatter.class)
    LocalDateTime getCreationTime();

    /**
     * Set the creation time of this entity.
     *
     * @param creationTime The creation time of this entity.
     */
    void setCreationTime(LocalDateTime creationTime);

    /**
     * The update time of this row.
     *
     * @return The update time of this row.
     */
    @JsonSerialize(using = LocalDateTimeFormatter.class)
    LocalDateTime getUpdateTime();

    /**
     * Set the update time of this row.
     *
     * @param updateTime The update time of this row.
     */
    void setUpdateTime(LocalDateTime updateTime);

    /**
     * ID list of each joining entity. Entities of different types are distinguished using Map key.
     *
     * @return ID list of each joining entity.
     */
    Map<String, Supplier<? extends List<String>>> joiningKeyMap();

    /**
     * Joining Operation of each joining entity. Entities of different types are distinguished using Map key.
     *
     * @return Joining Operation of each joining entity.
     */
    Map<String, Consumer<? super Object>> joiningEntityMap();
}
