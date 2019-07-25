package cc.domovoi.spring.utils;

import io.swagger.annotations.ApiModelProperty;

public interface RecordAuthorInterface {

    @ApiModelProperty(value = "作者")
    String getAuthor();

    void setAuthor(String author);

    @ApiModelProperty(value = "作者ID")
    String getAuthorId();

    void setAuthorId(String authorId);
}
