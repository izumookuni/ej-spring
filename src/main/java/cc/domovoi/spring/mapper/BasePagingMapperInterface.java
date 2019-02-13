package cc.domovoi.spring.mapper;

/**
 * BasePagingMapperInterface
 *
 * @param <E> Entity type
 */
public interface BasePagingMapperInterface<E> {

    /**
     * Find Amount of entities that have certain attributes.
     *
     * @param entity Query conditions.
     * @return Amount of entities.
     */
    Integer findCount(E entity);
}
