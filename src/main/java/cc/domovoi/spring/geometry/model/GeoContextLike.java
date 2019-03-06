package cc.domovoi.spring.geometry.model;

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

}
