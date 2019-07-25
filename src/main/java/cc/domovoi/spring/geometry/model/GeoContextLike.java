package cc.domovoi.spring.geometry.model;

import java.util.List;

/**
 * GeoContextLike.
 */
public interface GeoContextLike {

    /**
     * Context ID.
     *
     * @return Context ID.
     */
    String getContextId();

    /**
     * Set context ID.
     *
     * @param contextId Context ID.
     */
    void setContextId(String contextId);

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
    List<String> getContextIdIn();

    /**
     * Set context ID List for query.
     *
     * @param contextIdIn Context ID List.
     */
    void setContextIdIn(List<String> contextIdIn);
}
