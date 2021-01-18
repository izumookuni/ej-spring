package cc.domovoi.spring.mapper.jooq;

import cc.domovoi.spring.entity.jooq.GeneralJooqDSLEntityInterface;
import cc.domovoi.spring.mapper.GeneralMapperInterface;
import cc.domovoi.spring.mapper.extension.RichMapperInterface;
import cc.domovoi.spring.utils.BeanMapUtils;
import org.jooq.UpdatableRecord;
import org.joor.Reflect;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;

public interface GeneralJooqJoiningMapperInterface<R extends UpdatableRecord<R>, K, E extends GeneralJooqDSLEntityInterface<K, R>> extends GeneralJooqRetrieveJoiningMapperInterface<R, K, E>, GeneralMapperInterface<K, E>, RichMapperInterface<K, E> {

    @Override
    default Integer add(E entity) {
        doBeforeAdd(0, "add", entity);
        Integer addResult = addEntityByDSL(entity);
        doAfterAdd(0, "add", entity, addResult);
        return addResult;
    }

    @Override
    default Integer update(E entity) {
        doBeforeUpdate(0, "update", entity);
        E e = findEntityUsingIdByDSL(entity.getId());
        if (Objects.nonNull(e)) {
            E e1 = BeanMapUtils.copyPropertyIgnoreNull(e, entity);
            Integer result = updateEntityByDSL(e1);
            doAfterUpdate(0, "update", entity, result);
            return result;
        }
        else {
            return 0;
        }

    }

    @Override
    default Integer updateForced(E entity) {
        doBeforeUpdate(0, "updateForced", entity);
        Integer result = updateEntityByDSL(entity);
        doAfterUpdate(0, "updateForced", entity, result);
        return result;
    }

    @Override
    default Integer updateSetNull(E entity, List<String> setNull) {
        doBeforeUpdate(0, "updateSetNull", entity);
        E e = findEntityUsingIdByDSL(entity.getId());
        if (Objects.nonNull(e)) {
            E e1 = BeanMapUtils.copyPropertyIgnoreNull(e, entity);
            Reflect r = Reflect.on(e1);
            setNull.forEach(s -> r.set(s, null));
            Integer result = updateEntityByDSL(e1);
            doAfterUpdate(0, "updateSetNull", entity, result);
            BeanUtils.copyProperties(e1, entity);
            return result;
        }
        else {
            return 0;
        }
    }

    @Override
    default Integer delete(E entity) {
        doBeforeDelete(0, "delete", entity);
        Integer result = deleteEntityByDSL(entity);
        doAfterDelete(0, "delete", entity, result);
        return result;
    }

    @Override
    default Integer deleteById(List<K> idList) {
        return deleteEntityListByDSL(idList);
    }

    default Boolean checkEntityExists(E entity) {
        if (Objects.isNull(entity.getId())) {
            return false;
        }
        E e = findEntityUsingIdByDSL(entity.getId());
        return Objects.nonNull(e);
    }

    default Integer addEntityByDSL(E entity) {
        return dsl().insertInto(table()).set(convertEntityToRecord(entity)).execute();
    }

    default Integer updateEntityByDSL(E entity) {
        return dsl().update(table()).set(convertEntityToRecord(entity)).where(initConditionUsingEntityIncludingField(entity, "id")).execute();
    }

    default Integer deleteEntityByDSL(E entity) {
        return dsl().deleteFrom(table()).where(initConditionUsingEntity(entity)).execute();
    }

    default Integer deleteEntityListByDSL(List<K> idList) {
        return dsl().deleteFrom(table()).where(table().field("id", keyClass()).in(idList)).execute();
    }

}
