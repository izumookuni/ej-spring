package cc.domovoi.spring.geometry.model;

import java.util.List;

/**
 * GeoContextLike.
 * @param <K> contextId type
 */
public interface GeoContextLike<K> {

    /**
     * Context ID.
     *
     * @return Context ID.
     */
    K getContextId();

    /**
     * Set context ID.
     *
     * @param contextId Context ID.
     */
    void setContextId(K contextId);

    /**
     * Context name.
     *
     * @return Context name.
     */
    String getContextName();

    /**
     * Set context name.
     *
     * @param contextName Context name.
     */
    void setContextName(String contextName);

    /**
     * Context ID List for query.
     *
     * @return Context ID List.
     */
    List<K> getContextIdIn();

    /**
     * Set context ID List for query.
     *
     * @param contextIdIn Context ID List.
     */
    void setContextIdIn(List<K> contextIdIn);
}
