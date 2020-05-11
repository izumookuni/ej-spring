package cc.domovoi.spring.service.paging;

import cc.domovoi.spring.entity.StandardJoiningEntityInterface;
import cc.domovoi.spring.service.StandardJoiningServiceInterface;

public interface StandardPagingServiceInterface<E extends StandardJoiningEntityInterface> extends GeneralPagingServiceInterface<String, E>, StandardJoiningServiceInterface<E> {
}
