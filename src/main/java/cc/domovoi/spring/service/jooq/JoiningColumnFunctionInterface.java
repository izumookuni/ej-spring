package cc.domovoi.spring.service.jooq;

import org.jooq.DSLContext;
import org.jooq.lambda.function.Function2;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface JoiningColumnFunctionInterface extends Function2<DSLContext, List<Object>, Map> {

    @Override
    Map apply(DSLContext dsl, List<Object> keyList);
}
