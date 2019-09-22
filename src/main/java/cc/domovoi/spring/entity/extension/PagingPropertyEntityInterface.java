package cc.domovoi.spring.entity.extension;

import java.util.List;

public interface PagingPropertyEntityInterface extends PagingEntityInterface, PropertyMapEntityInterface {

    @Override
    default void setPageNum(Integer pageNum) {
        propertyMap().put("pageNum", pageNum);
    }

    @Override
    default Integer getPageNum() {
        return (Integer) propertyMap().get("pageNum");
    }

    @Override
    default void setPageSize(Integer pageSize) {
        propertyMap().put("pageSize", pageSize);
    }

    @Override
    default Integer getPageSize() {
        return (Integer) propertyMap().get("pageSize");
    }

    @Override
    default void setSortBy(List<String> sortBy) {
        propertyMap().put("sortBy", sortBy);
    }

    @SuppressWarnings("unchecked")
    @Override
    default List<String> getSortBy() {
        return (List<String>) propertyMap().get("sortBy");
    }

    @Override
    default void setSortOrder(String sortOrder) {
        propertyMap().put("sortOrder", sortOrder);
    }

    @Override
    default String getSortOrder() {
        return (String) propertyMap().get("sortOrder");
    }
}
