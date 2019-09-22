package cc.domovoi.spring.entity.extension;

import java.util.Map;

/**
 * PropertyMapEntityInterface
 */
public interface PropertyMapEntityInterface {

    /**
     * A map used to store entity attributes
     * @return Map.
     */
    Map<String, Object> propertyMap();
}
