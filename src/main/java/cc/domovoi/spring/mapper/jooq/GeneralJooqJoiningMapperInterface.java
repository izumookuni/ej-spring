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
    default Integer addBase(E entity) {
        doBeforeAdd(0, "addBase", entity);
        Integer addResult = addEntityByDSL(entity);
        doAfterAdd(0, "addBase", entity, addResult);
        return addResult;
    }

    @Override
    default Integer updateBase(E entity) {
        doBeforeUpdate(0, "updateBase", entity);
        E e = findEntityUsingIdByDSL(entity.getId());
        if (Objects.nonNull(e)) {
            E e1 = BeanMapUtils.copyPropertyIgnoreNull(e, entity);
            Integer result = updateEntityByDSL(e1);
            doAfterUpdate(0, "updateBase", entity, result);
            return result;
        }
        else {
            return 0;
        }

    }

    @Override
    default Integer updateBaseForced(E entity) {
        doBeforeUpdate(0, "updateBase", entity);
        Integer result = updateEntityByDSL(entity);
        doAfterUpdate(0, "updateBase", entity, result);
        return result;
    }

    @Override
    default Integer updateBaseSetNull(E entity, List<String> setNull) {
        doBeforeUpdate(0, "updateBase", entity);
        E e = findEntityUsingIdByDSL(entity.getId());
        if (Objects.nonNull(e)) {
            E e1 = BeanMapUtils.copyPropertyIgnoreNull(e, entity);
            Reflect r = Reflect.on(e1);
            setNull.forEach(s -> r.set(s, null));
            Integer result = updateEntityByDSL(e1);
            doAfterUpdate(0, "updateBase", entity, result);
            BeanUtils.copyProperties(e1, entity);
            return result;
        }
        else {
            return 0;
        }
    }

    @Override
    default Integer deleteBase(E entity) {
        doBeforeDelete(1, "deleteBase", entity);
        Integer result = deleteEntityByDSL(entity);
        doBeforeDelete(0, "deleteBase", entity);
        return result;
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

}
