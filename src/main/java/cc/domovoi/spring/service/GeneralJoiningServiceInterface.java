package cc.domovoi.spring.service;

import cc.domovoi.collection.util.Failure;
import cc.domovoi.collection.util.Success;
import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import cc.domovoi.spring.annotation.after.AfterAdd;
import cc.domovoi.spring.annotation.after.AfterDelete;
import cc.domovoi.spring.annotation.after.AfterUpdate;
import cc.domovoi.spring.annotation.before.BeforeAdd;
import cc.domovoi.spring.annotation.before.BeforeDelete;
import cc.domovoi.spring.annotation.before.BeforeUpdate;
import cc.domovoi.spring.utils.GeneralUtils;
import org.jooq.lambda.tuple.Tuple2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public interface GeneralJoiningServiceInterface<K, E extends GeneralJoiningEntityInterface<K>> extends GeneralRetrieveJoiningServiceInterface<K, E> {

    K idGenerator();

    Boolean checkEntityExists(E entity);

    Try<Tuple2<Integer, K>> innerAddEntity(E entity);

    Try<Integer> innerUpdateEntity(E entity);

    Try<Integer> innerDeleteEntity(E entity);

    default Optional<String> addCondition(E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        return Optional.empty();
    }

    default Optional<String> updateCondition(E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        return Optional.empty();
    }

    default Optional<String> deleteCondition(E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        return Objects.nonNull(entity) && Objects.nonNull(entity.getId()) ? Optional.empty() : Optional.of("id must not be null");
    }

    default void beforeAdd(E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
    }

    default void beforeUpdate(E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
    }

    default void beforeDelete(E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
    }

    default void afterAdd(E entity, Try<Tuple2<Integer, K>> result, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
    }

    default void afterUpdate(E entity, Try<Integer> result, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
    }

    default void afterDelete(E entity, Try<Integer> result, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
    }

    default void doBeforeAdd(Integer scope, E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        if (0 == scope) {
            beforeAdd(entity, request, response);
        }
        GeneralUtils.doFindAnnotationMethod(this, BeforeAdd.class, scope, entity, request, response);
    }

    default void doBeforeUpdate(Integer scope, E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        if (0 == scope) {
            beforeUpdate(entity, request, response);
        }
        GeneralUtils.doFindAnnotationMethod(this, BeforeUpdate.class, scope, entity, request, response);
    }

    default void doBeforeDelete(Integer scope, E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        if (0 == scope) {
            beforeDelete(entity, request, response);
        }
        GeneralUtils.doFindAnnotationMethod(this, BeforeDelete.class, scope, entity, request, response);
    }

    default void doAfterAdd(Integer scope, E entity, Try<Tuple2<Integer, K>> result, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        if (0 == scope) {
            afterAdd(entity, result, request, response);
        }
        GeneralUtils.doFindAnnotationMethod(this, AfterAdd.class, scope, entity, result, request, response);
    }

    default void doAfterUpdate(Integer scope, E entity, Try<Integer> result, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        if (0 == scope) {
            afterUpdate(entity, result, request, response);
        }
        GeneralUtils.doFindAnnotationMethod(this, AfterUpdate.class, scope, entity, result, request, response);
    }

    default void doAfterDelete(Integer scope, E entity, Try<Integer> result, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        if (0 == scope) {
            afterDelete(entity, result, request, response);
        }
        GeneralUtils.doFindAnnotationMethod(this, AfterDelete.class, scope, entity, result, request, response);
    }

    @Deprecated
    default void processBeforeAdd(E entity) {
    }

    @Deprecated
    default void processBeforeUpdate(E entity) {
    }

    @Deprecated
    default void processBeforeDelete(E entity) {
    }

    @Deprecated
    default void processAfterAdd(E entity, Try<Tuple2<Integer, K>> result) {
    }

    @Deprecated
    default void processAfterUpdate(E entity, Try<Integer> result) {
    }

    @Deprecated
    default void processAfterDelete(E entity, Try<Integer> result) {
    }

    default Try<Tuple2<Integer, K>> addEntity(E entity) {
        return addEntity(entity, Optional.empty(), Optional.empty());
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

    default Try<Tuple2<Integer, K>> addEntity(E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        // before add 1
        if (Objects.nonNull(entity)) {
            doBeforeAdd(1, entity, request, response);
        }
        // addCondition
        Optional<String> addConditionResult = addCondition(entity, request, response);
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
        // before add 0
        if (Objects.nonNull(entity)) {
            doBeforeAdd(0, entity, request, response);
//            processBeforeAdd(entity);
        }
        // check entity exists
        if (idFlag && checkEntityExists(entity)) {
            return new Success<>(new Tuple2<>(0, null));
        }
        // init default field
        initDefaultField(entity);
//        if (Objects.isNull(entity.getId())) {
//            entity.setId(idGenerator());
//        }
//        if (Objects.isNull(entity.getAvailable())) {
//            entity.setAvailable(true);
//        }
//        LocalDateTime now = LocalDateTime.now();
//        entity.setCreationTime(now);
//        entity.setUpdateTime(now);

        Try<Tuple2<Integer, K>> innerAddResult = innerAddEntity(entity);
        // after add
        if (Objects.nonNull(entity)) {
//            processAfterAdd(entity, innerAddResult);
            doAfterAdd(0, entity, innerAddResult, request, response);
        }
        return innerAddResult;
    }

    default Try<Integer> updateEntity(E entity) {
        return updateEntity(entity, Optional.empty(), Optional.empty());
    }

    default Try<Integer> updateEntity(E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        // before update 1
        if (Objects.nonNull(entity)) {
            doBeforeUpdate(1, entity, request, response);
        }
        // updateCondition
        Optional<String> updateConditionResult = updateCondition(entity, request, response);
        if (updateConditionResult.isPresent()) {
            return new Failure<>(new RuntimeException(updateConditionResult.get()));
        }
        // before update 0
        if (Objects.nonNull(entity)) {
            doBeforeUpdate(0, entity, request, response);
//            processBeforeUpdate(entity);
        }
        Try<Integer> innerUpdateResult = innerUpdateEntity(entity);
        // after update
        if (Objects.nonNull(entity)) {
//            processAfterUpdate(entity, innerUpdateResult);
            doAfterUpdate(0, entity, innerUpdateResult, request, response);
        }
        return innerUpdateResult;
    }

    default Try<Integer> deleteEntity(E entity) {
        return deleteEntity(entity, Optional.empty(), Optional.empty());
    }

    default Try<Integer> deleteEntity(E entity, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        // before delete 1
        if (Objects.nonNull(entity)) {
            doBeforeDelete(1, entity, request, response);
        }
        // deleteCondition
        Optional<String> deleteConditionResult = deleteCondition(entity, request, response);
        if (deleteConditionResult.isPresent()) {
            return new Failure<>(new RuntimeException(deleteConditionResult.get()));
        }
        // before delete 0
        if (Objects.nonNull(entity)) {
            doBeforeDelete(0, entity, request, response);
//            processBeforeDelete(entity);
        }
        Try<Integer> innerDeleteResult = innerDeleteEntity(entity);
        // after delete
        if (Objects.nonNull(entity)) {
//            processAfterDelete(entity, innerDeleteResult);
            doAfterDelete(0, entity, innerDeleteResult, request, response);
        }
        return innerDeleteResult;
    }

}
