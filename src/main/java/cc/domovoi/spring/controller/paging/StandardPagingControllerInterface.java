package cc.domovoi.spring.controller.paging;

import cc.domovoi.spring.controller.MvcControllerInterface;
import cc.domovoi.spring.controller.StandardRetrieveControllerInterface;
import cc.domovoi.spring.entity.StandardJoiningEntityInterface;
import cc.domovoi.spring.service.paging.StandardPagingServiceInterface;

import java.util.List;

public interface StandardPagingControllerInterface<E extends StandardJoiningEntityInterface, S extends StandardPagingServiceInterface<E>> extends GeneralPagingControllerInterface<String, E, S>, StandardRetrieveControllerInterface<E, S>, MvcControllerInterface<S> {

    @Override
    default Integer findCountFunction(E entity) {
        return service().findCount(entity);
    }

    @Override
    default List<E> findPagingListFunction(E entity, Integer pageNum, Integer pageSize, List<String> sortBy, List<String> sortOrder) {
        return service().findPagingList(entity, pageNum, pageSize, sortBy, sortOrder);
    }
}
