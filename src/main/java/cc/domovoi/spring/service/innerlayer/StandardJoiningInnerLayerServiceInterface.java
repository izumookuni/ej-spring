package cc.domovoi.spring.service.innerlayer;

import cc.domovoi.collection.util.Success;
import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.controller.innerlayer.response.InnerLayerAddResultResponse;
import cc.domovoi.spring.controller.innerlayer.response.InnerLayerUpdateDeleteResultResponse;
import cc.domovoi.spring.entity.StandardJoiningEntityInterface;
import cc.domovoi.spring.service.StandardJoiningServiceInterface;
import cc.domovoi.spring.utils.RestTemplateUtils;
import com.fasterxml.jackson.databind.JavaType;
import org.jooq.lambda.tuple.Tuple2;

import java.io.IOException;

public interface StandardJoiningInnerLayerServiceInterface<E extends StandardJoiningEntityInterface> extends StandardJoiningServiceInterface<E>, StandardRetrieveJoiningInnerLayerServiceInterface<E> {

    default String addEntityUrl() {
        return "/inner-layer/add";
    }

    default String updateEntityUrl() {
        return "/inner-layer/update";
    }

    default String deleteEntityUrl() {
        return "/inner-layer/delete";
    }

    default Try<Tuple2<Integer, String>> addInnerLayerEntity(E entity) throws IOException {
        JavaType javaType = RestTemplateUtils.objectMapper.getTypeFactory().constructParametricType(InnerLayerAddResultResponse.class, String.class, entityClass());
        InnerLayerAddResultResponse<String, E> response = RestTemplateUtils.postForObject(restTemplate(), host() + addEntityUrl(), entity, javaType);
        return new Success<>(new Tuple2<>(response.getResult(), response.getId()));
    }

    default Try<Integer> updateInnerLayerEntity(E entity) throws IOException {
        JavaType javaType = RestTemplateUtils.objectMapper.getTypeFactory().constructParametricType(InnerLayerUpdateDeleteResultResponse.class, entityClass());
        InnerLayerUpdateDeleteResultResponse<E> response = RestTemplateUtils.postForObject(restTemplate(), host() + updateEntityUrl(), entity, javaType);
        return new Success<>(response.getResult());
    }

    default Try<Integer> deleteInnerLayerEntity(E entity) throws IOException {
        JavaType javaType = RestTemplateUtils.objectMapper.getTypeFactory().constructParametricType(InnerLayerUpdateDeleteResultResponse.class, entityClass());
        InnerLayerUpdateDeleteResultResponse<E> response = RestTemplateUtils.postForObject(restTemplate(), host() + deleteEntityUrl(), entity, javaType);
        return new Success<>(response.getResult());
    }
}
