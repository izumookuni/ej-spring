package cc.domovoi.ej.spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface BaseEntityPagingInterface {

    void setPageNum(Integer pageNum);

    @ApiModelProperty(value = "Number of pages")
    Integer getPageNum();

    void setPageSize(Integer pageSize);

    @ApiModelProperty(value = "Row size of pages")
    Integer getPageSize();

    @ApiModelProperty(value = "Sort key")
    List<String> getSortBy();

    @ApiModelProperty(value = "Sort order")
    String getSortOrder();

    default Function<String, Boolean> checkSortKey() {
        return (key) -> false;
    }

    /**
     * Start line, the minimum value is 1.
     *
     * @return Start line.
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    default Integer getRowFrom() {
        if (getPageNum() != null && getPageSize() != null) {
            return (getPageNum() - 1) * getPageSize() + 1;
        }
        else {
            return null;
        }
    }

    /**
     * End line, excluding this line.
     *
     * @return End line.
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    default Integer getRowUntil() {
        if (getPageNum() != null && getPageSize() != null) {
            return getPageNum() * getPageSize() + 1;
        }
        else {
            return null;
        }
    }

    /**
     * offset, the minimum value is 0.
     *
     * @return Offset.
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    default Integer getOffset() {
        return getRowFrom() != null ? getRowFrom() -1 : null;
    }

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    default List<String> getSortByCertified() {
        if (getSortBy() != null) {
            return getSortBy().stream().filter(checkSortKey()::apply).collect(Collectors.toList());
        }
        else {
            return Collections.emptyList();
        }
    }

    @ApiModelProperty(hidden = true)
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
        }
        else {
            return "";
        }
    }
}
