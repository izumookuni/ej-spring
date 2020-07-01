package cc.domovoi.spring.mapper.extension;

import cc.domovoi.spring.annotation.after.AfterAdd;
import cc.domovoi.spring.annotation.after.AfterDelete;
import cc.domovoi.spring.annotation.after.AfterUpdate;
import cc.domovoi.spring.annotation.before.BeforeAdd;
import cc.domovoi.spring.annotation.before.BeforeDelete;
import cc.domovoi.spring.annotation.before.BeforeUpdate;
import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.mapper.GeneralMapperInterface;
import cc.domovoi.spring.utils.GeneralUtils;

public interface RichMapperInterface<K, E extends GeneralJoiningEntityInterface<K>> extends RichRetrieveMapperInterface<K, E>, GeneralMapperInterface<K, E> {

    default void beforeAdd(E entity) {
    }

    default void beforeUpdate(E entity) {
    }

    default void beforeDelete(E entity) {
    }

    default void afterAdd(E entity, Integer result) {
    }

    default void afterUpdate(E entity, Integer result) {
    }

    default void afterDelete(E entity, Integer result) {
    }

    default void doBeforeAdd(Integer scope, String name, E entity) {
        if (0 == scope) {
            beforeAdd(entity);
        }
        GeneralUtils.doAnnotationMethod(this, BeforeAdd.class, scope, name, entity);
    }

    default void doBeforeUpdate(Integer scope, String name, E entity) {
        if (0 == scope) {
            beforeUpdate(entity);
        }
        GeneralUtils.doAnnotationMethod(this, BeforeUpdate.class, scope, name, entity);
    }

    default void doBeforeDelete(Integer scope, String name, E entity) {
        if (0 == scope) {
            beforeDelete(entity);
        }
        GeneralUtils.doAnnotationMethod(this, BeforeDelete.class, scope, name, entity);
    }

    default void doAfterAdd(Integer scope, String name, E entity, Integer result) {
        if (0 == scope) {
            afterAdd(entity, result);
        }
        GeneralUtils.doAnnotationMethod(this, AfterAdd.class, scope, name, entity, result);
    }

    default void doAfterUpdate(Integer scope, String name, E entity, Integer result) {
        if (0 == scope) {
            afterUpdate(entity, result);
        }
        GeneralUtils.doAnnotationMethod(this, AfterUpdate.class, scope, name, entity, result);
    }

    default void doAfterDelete(Integer scope, String name, E entity, Integer result) {
        if (0 == scope) {
            afterDelete(entity, result);
        }
        GeneralUtils.doAnnotationMethod(this, AfterDelete.class, scope, name, entity, result);
    }
}
