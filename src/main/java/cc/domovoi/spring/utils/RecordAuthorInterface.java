package cc.domovoi.spring.utils;

import io.swagger.annotations.ApiModelProperty;

public interface RecordAuthorInterface {

    @ApiModelProperty(value = "Author")
    String getAuthor();

    void setAuthor(String author);

    @ApiModelProperty(value = "AuthorId")
    String getAuthorId();

    void setAuthorId(String authorId);
}
