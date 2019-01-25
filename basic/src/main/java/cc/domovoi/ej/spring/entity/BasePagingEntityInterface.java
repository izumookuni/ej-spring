package cc.domovoi.ej.spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * BasePagingEntityInterface.
 */
public interface BasePagingEntityInterface {

    /**
     * Set number of pages.
     *
     * @param pageNum Number of pages.
     */
    void setPageNum(Integer pageNum);

    /**
     * Number of pages.
     *
     * @return Number of pages.
     */
    Integer getPageNum();

    /**
     * Set row size of pages.
     *
     * @param pageSize Row size of pages.
     */
    void setPageSize(Integer pageSize);

    /**
     * Row size of pages.
     *
     * @return Row size of pages.
     */
    Integer getPageSize();

    /**
     * Set sort key.
     *
     * @param sortBy Sort key.
     */
    void setSortBy(List<String> sortBy);

    /**
     * Sort key.
     *
     * @return Sort key.
     */
    List<String> getSortBy();

    /**
     * Set sort order.
     *
     * @param sortOrder Sort order.
     */
    void setSortOrder(String sortOrder);

    /**
     * Sort order.
     *
     * @return Sort order.
     */
    String getSortOrder();

    /**
     * Check whether sort key is certified.
     *
     * @return Whether sort key is certified.
     */
    default Function<String, Boolean> checkSortKey() {
        return (key) -> false;
    }

    /**
     * Start line, the minimum value is 1.
     *
     * @return Start line.
     */
    @JsonIgnore
    default Integer getRowFrom() {
        if (getPageNum() != null && getPageSize() != null) {
            return (getPageNum() - 1) * getPageSize() + 1;
        } else {
            return null;
        }
    }

    /**
     * End line, excluding this line.
     *
     * @return End line.
     */
    @JsonIgnore
    default Integer getRowUntil() {
        if (getPageNum() != null && getPageSize() != null) {
            return getPageNum() * getPageSize() + 1;
        } else {
            return null;
        }
    }

    /**
     * offset, the minimum value is 0.
     *
     * @return Offset.
     */
    @JsonIgnore
    default Integer getOffset() {
        return getRowFrom() != null ? getRowFrom() - 1 : null;
    }

    /**
     * Certified sort key.
     *
     * @return Certified sort key.
     */
    @JsonIgnore
    default List<String> getSortByCertified() {
        if (getSortBy() != null) {
            return getSortBy().stream().filter(checkSortKey()::apply).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Certified sort order.
     *
     * @return Certified sort order.
     */
    @JsonIgnore
    default String getSortOrderCertified() {
        if (getSortOrder() == null) {
            return "";
        }
        String sortOrder = getSortOrder().toLowerCase();
        if ("asc".equals(sortOrder) || "desc".equals(sortOrder) ||
                "asc nulls first".equals(sortOrder) || "asc nulls last".equals(sortOrder) ||
                "desc nulls first".equals(sortOrder) || "desc nulls last".equals(sortOrder)) {
            return sortOrder;
        } else {
            return "";
        }
    }
}
