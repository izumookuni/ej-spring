package cc.domovoi.spring.service.jooq;

import org.jooq.DSLContext;
import org.jooq.lambda.function.Function3;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@FunctionalInterface
public interface JoiningColumnFunctionInterface<E> extends Function3<DSLContext, List<Object>, Optional<E>, Map> {

    @Override
    Map apply(DSLContext dsl, List<Object> keyList, Optional<E> query);
}
