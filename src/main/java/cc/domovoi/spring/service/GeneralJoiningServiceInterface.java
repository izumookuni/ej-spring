package cc.domovoi.spring.service;

import cc.domovoi.collection.util.Failure;
import cc.domovoi.collection.util.Success;
import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import org.jooq.lambda.tuple.Tuple2;

import java.util.Objects;
import java.util.Optional;

public interface GeneralJoiningServiceInterface<K, E extends GeneralJoiningEntityInterface<K>> extends GeneralRetrieveJoiningServiceInterface<K, E> {

    K idGenerator();

    Boolean checkEntityExists(E entity);

    Try<Tuple2<Integer, K>> innerAddEntity(E entity);

    Try<Integer> innerUpdateEntity(E entity);

    Try<Integer> innerDeleteEntity(E entity);

    default void beforeAdd(E entity) {
    }

    default void beforeUpdate(E entity) {
    }

    default void beforeDelete(E entity) {
    }

    default Optional<String> addCondition(E entity) {
        return Optional.empty();
    }

    default Optional<String> updateCondition(E entity) {
        return Optional.empty();
    }

    default Optional<String> deleteCondition(E entity) {
        return Objects.nonNull(entity) && Objects.nonNull(entity.getId()) ? Optional.empty() : Optional.of("id must not be null");
    }

    default void afterAdd(E entity, Try<Tuple2<Integer, K>> result) {
    }

    default void afterUpdate(E entity, Try<Integer> result) {
    }

    default void afterDelete(E entity, Try<Integer> result) {
    }

    default void processBeforeAdd(E entity) {
    }

    default void processBeforeUpdate(E entity) {
    }

    default void processBeforeDelete(E entity) {
    }

    default void processAfterAdd(E entity, Try<Tuple2<Integer, K>> result) {
    }

    default void processAfterUpdate(E entity, Try<Integer> result) {
    }

    default void processAfterDelete(E entity, Try<Integer> result) {
    }

    default Try<Tuple2<Integer, K>> addEntity(E entity) {
        // addCondition
        Optional<String> addConditionResult = addCondition(entity);
        if (addConditionResult.isPresent()) {
            return new Failure<>(new RuntimeException(addConditionResult.get()));
        }
        final boolean idFlag;
        // generate id if not exists
        if (Objects.isNull(entity.getId())) {
            entity.setId(idGenerator());
            idFlag = false;
        }
        else {
            idFlag = true;
        }
        // before add
        if (Objects.nonNull(entity)) {
            beforeAdd(entity);
            processBeforeAdd(entity);
        }
        // check entity exists
        if (idFlag && checkEntityExists(entity)) {
            return new Success<>(new Tuple2<>(0, null));
        }
        Try<Tuple2<Integer, K>> innerAddResult = innerAddEntity(entity);
        // after add
        if (Objects.nonNull(entity)) {
            processAfterAdd(entity, innerAddResult);
            afterAdd(entity, innerAddResult);
        }
        return innerAddResult;
    }

    default Try<Integer> updateEntity(E entity) {
        // updateCondition
        Optional<String> updateConditionResult = updateCondition(entity);
        if (updateConditionResult.isPresent()) {
            return new Failure<>(new RuntimeException(updateConditionResult.get()));
        }
        // before update
        if (Objects.nonNull(entity)) {
            beforeUpdate(entity);
            processBeforeUpdate(entity);
        }
        Try<Integer> innerUpdateResult = innerUpdateEntity(entity);
        // after update
        if (Objects.nonNull(entity)) {
            processAfterUpdate(entity, innerUpdateResult);
            afterUpdate(entity, innerUpdateResult);
        }
        return innerUpdateResult;
    }

    default Try<Integer> deleteEntity(E entity) {
        // deleteCondition
        Optional<String> deleteConditionResult = deleteCondition(entity);
        if (deleteConditionResult.isPresent()) {
            return new Failure<>(new RuntimeException(deleteConditionResult.get()));
        }
        // before delete
        if (Objects.nonNull(entity)) {
            beforeDelete(entity);
            processBeforeDelete(entity);
        }
        Try<Integer> innerDeleteResult = innerDeleteEntity(entity);
        // after delete
        if (Objects.nonNull(entity)) {
            processAfterDelete(entity, innerDeleteResult);
            afterDelete(entity, innerDeleteResult);
        }
        return innerDeleteResult;
    }

}
