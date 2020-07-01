package cc.domovoi.spring.mapper.extension;

import cc.domovoi.spring.annotation.after.AfterFind;
import cc.domovoi.spring.annotation.after.AfterFindList;
import cc.domovoi.spring.annotation.before.BeforeFind;
import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.mapper.GeneralRetrieveMapperInterface;
import cc.domovoi.spring.utils.GeneralUtils;

import java.util.List;

public interface RichRetrieveMapperInterface<K, E extends GeneralJoiningEntityInterface<K>> extends GeneralRetrieveMapperInterface<K, E> {

    default void beforeFindEntity(E entity) {
    }

    default void afterFindEntity(E entity) {
    }

    default void afterFindList(List<E> entityList) {
        entityList.forEach(this::afterFindEntity);
    }

    default void doBeforeFindEntity(Integer scope, String name, E entity) {
        if (0 == scope) {
            beforeFindEntity(entity);
        }
        GeneralUtils.doAnnotationMethod(this, BeforeFind.class, scope, name, entity);
    }

    default void doAfterFindEntity(Integer scope, String name, E entity) {
        if (0 == scope) {
            afterFindEntity(entity);
        }
        GeneralUtils.doAnnotationMethod(this, AfterFind.class, scope, name, entity);
    }

    default void doAfterFindList(Integer scope, String name, List<E> entityList) {
        if (0 == scope) {
            afterFindList(entityList);
        }
        GeneralUtils.doAnnotationMethod(this, AfterFindList.class, scope, name, entityList);
    }
}
