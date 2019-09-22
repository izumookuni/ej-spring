package cc.domovoi.spring.controller.extension;

import cc.domovoi.spring.controller.MvcControllerInterface;
import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.entity.extension.PagingEntityInterface;
import cc.domovoi.spring.service.extension.PagingServiceInterface;

import java.util.List;

/**
 * PagingControllerInterface.
 *
 * @param <E> Entity type.
 * @param <S> Service type.
 */
public interface PagingControllerInterface<E extends GeneralJoiningEntityInterface<?> & PagingEntityInterface, S extends PagingServiceInterface<E, ?>> extends GeneralPagingControllerInterface<E>, MvcControllerInterface<S> {

    @Override
    default Integer findCountFunction(E entity) {
        return service().findCount(entity);
    }

    @Override
    default List<E> findPagingListFunction(E entity) {
        return service().findPagingList(entity);
    }
}
