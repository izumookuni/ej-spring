package cc.domovoi.spring.geometry.joining;

import cc.domovoi.spring.geometry.converter.GeometryLoader;

public interface GeometryLoaderJoiningInterface<INNER, OUTER> {

    /**
     * A geometry loader to load INNER object from OUTER object.
     *
     * @return A geometry loader.
     */
    GeometryLoader<INNER, OUTER> loader();
}
