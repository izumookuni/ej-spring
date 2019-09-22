package cc.domovoi.spring.entity;

import org.joor.Reflect;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.joor.Reflect.on;

public interface GeneralJoiningEntityInterface<K> {

    /**
     * ID.
     *
     * @return ID.
     */
    K getId();

    /**
     * Set ID.
     *
     * @param id ID.
     */
    void setId(K id);

    /**
     * The creation time of this entity.
     *
     * @return The creation time of this entity.
     */
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
    Map<String, Supplier<? extends List<Object>>> joiningKeyMap();

    /**
     * Joining Operation of each joining entity. Entities of different types are distinguished using Map key.
     *
     * @return Joining Operation of each joining entity.
     */
    Map<String, Consumer<? super Object>> joiningEntityMap();

    default Reflect reflect() {
        return on(this);
    }
}
