package cc.domovoi.spring.entity.audit;

import cc.domovoi.spring.entity.StandardJoiningEntityInterface;
import cc.domovoi.tools.jackson.ObjectMappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModelProperty;
import org.jooq.lambda.function.Consumer2;
import org.jooq.lambda.function.Consumer3;
import org.jooq.lambda.function.Function2;
import org.jooq.lambda.tuple.Tuple2;
import org.joor.Reflect;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.joor.Reflect.*;

public interface AuditEntityInterface extends GeneralAuditEntityInterface<String>, StandardJoiningEntityInterface {

    @Override
    default String idFormatter(String id) {
        return id;
    }
}
