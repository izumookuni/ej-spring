package cc.domovoi.spring.service;

import cc.domovoi.collection.util.*;
import cc.domovoi.spring.annotation.condition.AddCondition;
import cc.domovoi.spring.annotation.condition.DeleteCondition;
import cc.domovoi.spring.annotation.condition.UpdateCondition;
import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.annotation.after.AfterAdd;
import cc.domovoi.spring.annotation.after.AfterDelete;
import cc.domovoi.spring.annotation.after.AfterUpdate;
import cc.domovoi.spring.annotation.before.BeforeAdd;
import cc.domovoi.spring.annotation.before.BeforeDelete;
import cc.domovoi.spring.annotation.before.BeforeUpdate;
import cc.domovoi.spring.utils.GeneralUtils;
import cc.domovoi.tools.defaults.NullDefaultUtils;
import org.jooq.lambda.tuple.Tuple2;

import java.time.LocalDateTime;
import java.util.*;

public interface GeneralJoiningServiceInterface<K, E extends GeneralJoiningEntityInterface<K>> extends GeneralRetrieveJoiningServiceInterface<K, E> {

    K idGenerator();

    Boolean checkEntityExists(E entity);

    Try<Tuple2<Integer, K>> innerAddEntity(E entity);

    Try<Integer> innerUpdateEntity(E entity);

    Try<Integer> innerUpdateEntityForced(E entity);

    Try<Tuple2<Integer, E>> innerUpdateEntitySetNull(E entity, List<String> setNull);

    Try<Integer> innerDeleteEntity(E entity);

    default Optional<String> addCondition(E entity, Map<String, Object> params) {
        return Optional.empty();
    }

    default Optional<String> updateCondition(E entity, Map<String, Object> params) {
        return Optional.empty();
    }

    default Optional<String> deleteCondition(E entity, Map<String, Object> params) {
        return Objects.nonNull(entity) && Objects.nonNull(entity.getId()) ? Optional.empty() : Optional.of("id must not be null");
    }

    default Optional<String> doAddCondition(String name, E entity, Map<String, Object> params) {
        Optional<String> addCondition = addCondition(entity, params);
        if (addCondition.isPresent()) {
            return addCondition;
        }
        return GeneralUtils.doCondition(this, AddCondition.class, 0, name, entity, params);
    }

    default Optional<String> doUpdateCondition(String name, E entity, Map<String, Object> params) {
        Optional<String> updateCondition = updateCondition(entity, params);
        if (updateCondition.isPresent()) {
            return updateCondition;
        }
        return GeneralUtils.doCondition(this, UpdateCondition.class, 0, name, entity, params);
    }

    default Optional<String> doDeleteCondition(String name, E entity, Map<String, Object> params) {
        Optional<String> deleteCondition = deleteCondition(entity, params);
        if (deleteCondition.isPresent()) {
            return deleteCondition;
        }
        return GeneralUtils.doCondition(this, DeleteCondition.class, 0, name, entity, params);
    }

    default void beforeAdd(E entity, Map<String, Object> params) {
    }

    default void beforeUpdate(E entity, Map<String, Object> params) {
    }

    default void beforeDelete(E entity, Map<String, Object> params) {
    }

    default void afterAdd(E entity, Try<Tuple2<Integer, K>> result, Map<String, Object> params) {
    }

    default void afterUpdate(E entity, Try<Integer> result, Map<String, Object> params) {
    }

    default void afterDelete(E entity, Try<Integer> result, Map<String, Object> params) {
    }

    default void doBeforeAdd(Integer scope, String name, E entity, Map<String, Object> params) {
        if (0 == scope) {
            beforeAdd(entity, params);
        }
        GeneralUtils.doAnnotationMethod(this, BeforeAdd.class, scope, name, entity, params);
    }

    default void doBeforeUpdate(Integer scope, String name, E entity, Map<String, Object> params) {
        if (0 == scope) {
            beforeUpdate(entity, params);
        }
        GeneralUtils.doAnnotationMethod(this, BeforeUpdate.class, scope, name, entity, params);
    }

    default void doBeforeDelete(Integer scope, String name, E entity, Map<String, Object> params) {
        if (0 == scope) {
            beforeDelete(entity, params);
        }
        GeneralUtils.doAnnotationMethod(this, BeforeDelete.class, scope, name, entity, params);
    }

    default void doAfterAdd(Integer scope, String name, E entity, Try<Tuple2<Integer, K>> result, Map<String, Object> params) {
        if (0 == scope) {
            afterAdd(entity, result, params);
        }
        GeneralUtils.doAnnotationMethod(this, AfterAdd.class, scope, name, entity, result, params);
    }

    default void doAfterUpdate(Integer scope, String name, E entity, Try<Integer> result, Map<String, Object> params) {
        if (0 == scope) {
            afterUpdate(entity, result, params);
        }
        GeneralUtils.doAnnotationMethod(this, AfterUpdate.class, scope, name, entity, result, params);
    }

    default void doAfterDelete(Integer scope, String name, E entity, Try<Integer> result, Map<String, Object> params) {
        if (0 == scope) {
            afterDelete(entity, result, params);
        }
        GeneralUtils.doAnnotationMethod(this, AfterDelete.class, scope, name, entity, result, params);
    }

    default Try<Tuple2<Integer, K>> addEntity(E entity) {
        return addEntity(entity, new HashMap<>());
    }

    default void initDefaultField(E entity) {
        // init default field
        if (Objects.isNull(entity.getId())) {
            entity.setId(idGenerator());
        }
        if (Objects.isNull(entity.getAvailable())) {
            entity.setAvailable(true);
        }
        LocalDateTime now = LocalDateTime.now();
        entity.setCreationTime(now);
        entity.setUpdateTime(now);
    }

    default Try<Tuple2<Integer, K>> addEntity(E entity, Map<String, Object> params) {
        return addEntity(entity, params, "addEntity");
    }

    default Try<Tuple2<Integer, K>> addEntity(E entity, String name) {
        return addEntity(entity, new HashMap<>(), name);
    }

    default Try<Tuple2<Integer, K>> addEntity(E entity, Map<String, Object> params, String name) {
        // before add 1
        if (Objects.nonNull(entity)) {
            doBeforeAdd(1, name, entity, params);
        }
        // addCondition
        Optional<String> addConditionResult = doAddCondition(name, entity, params);
        if (addConditionResult.isPresent()) {
            return new Failure<>(new RuntimeException(addConditionResult.get()));
        }
        final boolean idFlag;
        // generate id if not exists
        if (Objects.isNull(entity.getId())) {
            entity.setId(idGenerator());
            idFlag = false;
        } else {
            idFlag = true;
        }
        // before add 0
        if (Objects.nonNull(entity)) {
            doBeforeAdd(0, name, entity, params);
        }
        // check entity exists
        if (idFlag && checkEntityExists(entity)) {
            return new Success<>(new Tuple2<>(0, null));
        }
        // init default field
        initDefaultField(entity);

        Try<Tuple2<Integer, K>> innerAddResult = innerAddEntity(entity);
        // after add
        if (Objects.nonNull(entity)) {
            doAfterAdd(0, name, entity, innerAddResult, params);
        }
        return innerAddResult;
    }

    default Try<Integer> updateEntity(E entity) {
        return updateEntity(entity, new HashMap<>());
    }

    default Try<Integer> updateEntity(E entity, Map<String, Object> params) {
        return updateEntity(entity, params, "updateEntity");
    }

    default Try<Integer> updateEntity(E entity, String name) {
        return updateEntity(entity, new HashMap<>(), name);
    }

    default Try<Integer> updateEntity(E entity, Boolean forced, Optional<List<String>> setNull) {
        return updateEntity(entity, new HashMap<>(), forced, setNull);
    }

    default Try<Integer> updateEntity(E entity, Map<String, Object> params, Boolean forced, Optional<List<String>> setNull) {
        if (Objects.nonNull(forced)) {
            params.put("forced", forced);
        }
        setNull.ifPresent(sn -> params.put("setNull", sn));
        return updateEntity(entity, params, "updateEntity");
    }

    default Try<Integer> updateEntity(E entity, Map<String, Object> params, String name) {
        // before update 1
        if (Objects.nonNull(entity)) {
            doBeforeUpdate(1, name, entity, params);
        }
        // updateCondition
        Optional<String> updateConditionResult = doUpdateCondition(name, entity, params);
        if (updateConditionResult.isPresent()) {
            return new Failure<>(new RuntimeException(updateConditionResult.get()));
        }
        // before update 0
        if (Objects.nonNull(entity)) {
            doBeforeUpdate(0, name, entity, params);
        }
//        Try<Integer> innerUpdateResult = innerUpdateEntity(entity);
        Boolean forced = (Boolean) params.getOrDefault("forced", false);
        Optional<List<String>> setNull = Optional.ofNullable((List<String>) params.get("setNull"));
        Try<Integer> innerUpdateResult = NullDefaultUtils.defaultBooleanValue(forced) ?
                innerUpdateEntityForced(entity) :
                setNull.map(sn -> {
                    Try<Tuple2<Integer, E>> r = innerUpdateEntitySetNull(entity, sn);
                    r.foreach(t2 -> params.put("entityAfterUpdate", t2.v2()));
                    return r.map(Tuple2::v1);
                }).orElseGet(() -> innerUpdateEntity(entity));
        // after update
        if (Objects.nonNull(entity)) {
            doAfterUpdate(0, name, entity, innerUpdateResult, params);
        }
        return innerUpdateResult;
    }

    default Try<Integer> deleteEntity(E entity) {
        return deleteEntity(entity, new HashMap<>());
    }

    default Try<Integer> deleteEntity(E entity, Map<String, Object> params) {
        return deleteEntity(entity, params, "deleteEntity");
    }

    default Try<Integer> deleteEntity(E entity, String name) {
        return deleteEntity(entity, new HashMap<>(), name);
    }

    default Try<Integer> deleteEntity(E entity, Map<String, Object> params, String name) {
        // before delete 1
        if (Objects.nonNull(entity)) {
            doBeforeDelete(1, name, entity, params);
        }
        // deleteCondition
        Optional<String> deleteConditionResult = doDeleteCondition(name, entity, params);
        if (deleteConditionResult.isPresent()) {
            return new Failure<>(new RuntimeException(deleteConditionResult.get()));
        }
        // before delete 0
        if (Objects.nonNull(entity)) {
            doBeforeDelete(0, name, entity, params);
//            processBeforeDelete(entity);
        }
        Try<Integer> innerDeleteResult = innerDeleteEntity(entity);
        // after delete
        if (Objects.nonNull(entity)) {
//            processAfterDelete(entity, innerDeleteResult);
            doAfterDelete(0, name, entity, innerDeleteResult, params);
        }
        return innerDeleteResult;
    }

    default Try<Either<Integer, Tuple2<Integer, K>>> addOrUpdateEntity(E entity, Map<String, Object> params, String name) {
        return addEntity(entity, params, name).fold(failure -> updateEntity(entity, params, name).map(Left::apply), result -> {
            if (Objects.nonNull(result.v2())) {
                return new Success<>(new Right<>(result));
            } else {
                return updateEntity(entity, params, name).map(Left::apply);
            }
        });
//        return addEntity(entity, params, name).flatMap(result -> {
//            if (Objects.nonNull(result.v2())) {
//                return new Success<>(new Right<>(result));
//            }
//            else {
//                return updateEntity(entity, params, name).map(Left::apply);
//            }
//        });
    }

    default Try<Either<Integer, Tuple2<Integer, K>>> addOrUpdateEntity(E entity, Map<String, Object> params) {
        return addOrUpdateEntity(entity, params, "addOrUpdateEntity");
    }

    default Try<Either<Integer, Tuple2<Integer, K>>> addOrUpdateEntity(E entity) {
        return addOrUpdateEntity(entity, new HashMap<>(), "addOrUpdateEntity");
    }
}
