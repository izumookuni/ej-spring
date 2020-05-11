package cc.domovoi.spring.test;

import cc.domovoi.spring.service.jooq.JoiningColumnFunctionInterface;
import org.jooq.DSLContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JoiningColumnFunctionTest implements JoiningColumnFunctionInterface {

    @Override
    public Map apply(DSLContext dsl, List keyList, Optional query) {
        return Collections.emptyMap();
    }

    @Override
    public Object apply(Object v1, Object v2, Object v3) {
        return null;
    }
}
