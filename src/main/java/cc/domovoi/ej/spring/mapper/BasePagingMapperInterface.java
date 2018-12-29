package cc.domovoi.ej.spring.mapper;

public interface BasePagingMapperInterface<E> {

    Integer findCount(E entity);
}
