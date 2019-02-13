package cc.domovoi.spring.entity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * BaseEntityInterface.
 */
public interface BaseEntityInterface extends BaseJoiningEntityInterface {

    @Override
    default Map<String, Supplier<? extends List<String>>> joiningKeyMap() {
        return Collections.emptyMap();
    }

    @Override
    default Map<String, Consumer<? super Object>> joiningEntityMap() {
        return Collections.emptyMap();
    }
}
