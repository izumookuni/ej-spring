package cc.domovoi.spring.service.innerlayer;

import cc.domovoi.collection.util.Try;
import cc.domovoi.spring.entity.StandardJoiningEntityInterface;
import cc.domovoi.spring.service.StandardRetrieveJoiningServiceInterface;
import cc.domovoi.spring.utils.RestTemplateUtils;
import cc.domovoi.spring.utils.joiningdepthtree.JoiningDepthTreeLike;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.joor.Reflect.onClass;

public interface StandardRetrieveJoiningInnerLayerServiceInterface<E extends StandardJoiningEntityInterface> extends StandardRetrieveJoiningServiceInterface<E> {

    RestTemplate restTemplate();

    String host();

    default String findEntityUrl() {
        return "/inner-layer/query";
    }

    default E findInnerLayerEntity(String id, JoiningDepthTreeLike depthTree) throws IOException {
        E entity = onClass(entityClass()).create().get();
        entity.setId(id);
        List<E> entityList = RestTemplateUtils.postForObjectList(restTemplate(), host() + findEntityUrl(), entity, entityClass());
        if (entityList.size() == 1) {
            return entityList.get(0);
        }
        else if (entityList.isEmpty()) {
            return null;
        }
        else {
            throw new RuntimeException("too many entity");
        }
    }

    default E findInnerLayerEntity(String id) throws IOException {
        return findInnerLayerEntity(id, depthTree());
    }

    default E findInnerLayerEntity(E entity, JoiningDepthTreeLike depthTree) throws IOException {

        List<E> entityList = RestTemplateUtils.postForObjectList(restTemplate(), host() + findEntityUrl(), entity, entityClass());
        if (entityList.size() == 1) {
            return entityList.get(0);
        }
        else if (entityList.isEmpty()) {
            return null;
        }
        else {
            throw new RuntimeException("too many entity");
        }
    }

    default E findInnerLayerEntity(E entity) throws IOException {
        return findInnerLayerEntity(entity, depthTree());
    }

    default List<E> findInnerLayerList(E entity, JoiningDepthTreeLike depthTree) throws IOException {
        return RestTemplateUtils.postForObjectList(restTemplate(), host() + findEntityUrl(), entity, entityClass());
    }

    default List<E> findInnerLayerList(E entity) throws IOException {
        return findInnerLayerList(entity, depthTree());
    }

}
