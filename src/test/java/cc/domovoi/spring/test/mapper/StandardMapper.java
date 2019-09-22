package cc.domovoi.spring.test.mapper;

import cc.domovoi.spring.mapper.StandardRetrieveMapperInterface;
import cc.domovoi.spring.test.entity.StandardEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StandardMapper<E extends StandardEntity> implements StandardRetrieveMapperInterface<E> {

    protected List<E> entityList;

    public StandardMapper() {
    }

    @Override
    public E findBaseById(String id) {
        Optional<E> entity = entityList.stream().filter(e -> e.getId().equals(id)).findFirst();
        return entity.orElseGet(() -> null);
    }

    @Override
    public List<E> findBaseListById(List<String> idList) {
        if (idList == null || idList.isEmpty()) {
            return Collections.emptyList();
        }
        return entityList.stream().filter(e -> idList.contains(e.getId())).collect(Collectors.toList());
    }

    @Override
    public List<E> findBaseList(E entity) {
        if (entity.getId() == null) {
            return entityList;
        }
        return entityList.stream().filter(e -> e.equals(entity)).collect(Collectors.toList());
    }
}
