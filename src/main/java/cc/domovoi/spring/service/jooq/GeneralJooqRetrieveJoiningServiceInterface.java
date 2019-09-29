package cc.domovoi.spring.service.jooq;

import cc.domovoi.spring.entity.jooq.GeneralJooqEntityInterface;
import cc.domovoi.spring.service.GeneralRetrieveJoiningServiceInterface;
import org.jooq.*;
import org.joor.Reflect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.jooq.impl.DSL.*;
import static org.joor.Reflect.*;

/**
 * BaseRetrieveServiceInterface.
 *
 * @param <R> Record
 * @param <P> Pojo
 * @param <K> Key
 * @param <E> Entity
 */
public interface GeneralJooqRetrieveJoiningServiceInterface<R extends TableRecord<R>, P, K, E extends GeneralJooqEntityInterface<P, K>> extends DAO<R, P, K>, GeneralRetrieveJoiningServiceInterface<K, E> {

    Class<E> entityClass();

    DSLContext dsl();

    default R unMapper(P pojo) {
        R record = getTable().newRecord();
        record.from(pojo);
        return record;
    }

    default Condition initConditionUsingPojo(P pojo) {
        Record record = unMapper(pojo);
        Map<org.jooq.Field<?>, Object> conditionMap = new HashMap<>();
        for (org.jooq.Field<?> field : record.fields()) {
            Object value = field.getValue(record);
            if (Objects.nonNull(value)) {
                conditionMap.put(field, value);
            }
        }
        return condition(conditionMap);
    }

    @Override
    default Map<String, GeneralRetrieveJoiningServiceInterface> joiningService() {
        Map<String, GeneralRetrieveJoiningServiceInterface> joiningService = new HashMap<>();
        java.lang.reflect.Field[] fields = this.getClass().getDeclaredFields();
        Reflect reflect = on(this);
        for (java.lang.reflect.Field field : fields) {
            JoiningTable joiningTable = field.getAnnotation(JoiningTable.class);
            if (joiningTable != null) {
                String name = field.getName();
                String joiningName = "".equals(joiningTable.value()) ? name : joiningTable.value();
                joiningService.put(joiningName, reflect.get(name));
            }
        }
        return joiningService;
    }

    @Override
    default E innerFindEntity(K id) {
        return null;
    }

    @Override
    default List<E> innerFindList(E entity) {
        return null;
    }

    @Override
    default List<E> findListByKey(List<Object> keyList, String context) {
        return null;
    }

    default P findPojoByDao(P pojo) {
        return dsl().select(getTable().asterisk()).from(getTable()).where(initConditionUsingPojo(pojo)).fetchOne().into(getType());
    }

    default E findEntityByDao(E entity) {
        // Todo: ...
        return null;
    }

}
