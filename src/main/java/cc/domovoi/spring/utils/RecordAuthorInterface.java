package cc.domovoi.spring.utils;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public interface RecordAuthorInterface {

    @ApiModelProperty(value = "CreationAuthorName")
    default String getCreationAuthorName() {
        return null;
    }

    default void setCreationAuthorName(String creationAuthorName) {

    }

    @ApiModelProperty(value = "CreationAuthor")
    String getCreationAuthor();

    void setCreationAuthor(String creationAuthor);

    @ApiModelProperty(value = "UpdateAuthorName")
    default String getUpdateAuthorName() {
        return null;
    }

    default void setUpdateAuthorName(String updateAuthorName) {

    }

    @ApiModelProperty(value = "UpdateAuthor")
    String getUpdateAuthor();

    void setUpdateAuthor(String updateAuthor);

    @ApiModelProperty(value = "AuthorId")
    default String getAuthorId() {
        return Objects.nonNull(getCreationAuthor()) ? getCreationAuthor() : getUpdateAuthor();
    }
}
