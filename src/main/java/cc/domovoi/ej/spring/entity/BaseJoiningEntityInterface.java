package cc.domovoi.ej.spring.entity;

import cc.domovoi.ej.spring.json.LocalDateTimeFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface BaseJoiningEntityInterface {

    @ApiModelProperty(value = "ID")
    String getId();

    void setId(String id);

    @ApiModelProperty(value = "The creation time of this row")
    @JsonSerialize(using = LocalDateTimeFormatter.class)
    LocalDateTime getCreationTime();

    void setCreationTime(LocalDateTime creationTime);

    @ApiModelProperty(value = "The update time of this row")
    @JsonSerialize(using = LocalDateTimeFormatter.class)
    LocalDateTime getUpdateTime();

    void setUpdateTime(LocalDateTime updateTime);

    Map<String, Supplier<? extends List<String>>> joiningKeyMap();

    Map<String, Consumer<? super Object>> joiningEntityMap();
}
