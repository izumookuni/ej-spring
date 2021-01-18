package cc.domovoi.spring.service.mvc;

import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.mapper.GeneralRetrieveMapperInterface;
import cc.domovoi.spring.service.GeneralRetrieveJoiningServiceInterface;

import java.util.*;
import java.util.stream.Collectors;

public interface GeneralMvcRetrieveJoiningServiceInterface<K, E extends GeneralJoiningEntityInterface<K>, M extends GeneralRetrieveMapperInterface<K, E>> extends GeneralRetrieveJoiningServiceInterface<K, E>, MvcServiceInterface<M> {

    @Override
    default E innerFindEntity(K id) {
        return findEntityByMapper(id);
    }

    @Override
    default List<E> innerFindList(E entity) {
        return findListByMapper(entity);
    }

    @Override
    default List<E> innerFindListById(List<K> idList) {
        return findListUsingIdByMapper(idList);
    }

    @SuppressWarnings("unchecked")
    @Override
    default List<E> findListByKey(List<Object> keyList, String context, Class<?> entityClass, Map<String, Object> params, String name) {
        if (keyList.isEmpty()) {
            return Collections.emptyList();
        }
        List<K> idList = keyList.stream().map(key -> (K) key).collect(Collectors.toList());
        List<E> eList =  findListUsingIdByMapper(idList); // .stream().peek(this::doAfterFindEntity).collect(Collectors.toList());
        doAfterFindList(0, name, eList, params);
//        processAfterFindResult(eList);
        return eList;
    }

    default E findEntityByMapper(K id) {
        return mvcMapper().findById(id);
    }

    default List<E> findListByMapper(E entity) {
        return mvcMapper().findList(entity);
    }

    default List<E> findListUsingIdByMapper(List<K> idList) {
        if (Objects.isNull(idList) || idList.isEmpty()) {
            return Collections.emptyList();
        }
        List<K> normalizeIdList = idList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (normalizeIdList.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            int listSize = normalizeIdList.size();
            if (listSize <= 500) {
                return mvcMapper().findListById(normalizeIdList);
            }
            else {
                List<E> entityList = new ArrayList<>();
                for (int i = 0; i < listSize / 500; i++) {
                    List<K> innerIdList = normalizeIdList.subList(i * 500, (i + 1) * 500);
                    List<E> innerEntityList = mvcMapper().findListById(innerIdList);
                    entityList.addAll(innerEntityList);
                }
                return entityList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return normalizeIdList.stream().map(mvcMapper()::findById).collect(Collectors.toList());
        }
    }
}
