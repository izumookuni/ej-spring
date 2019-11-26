package cc.domovoi.spring.test;

import cc.domovoi.spring.service.jooq.JoiningColumnFunctionInterface;
import org.jooq.DSLContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JoiningColumnFunctionTest implements JoiningColumnFunctionInterface {

    @Override
    public Map apply(DSLContext dsl, List<Object> keyList) {
        return Collections.emptyMap();
    }
}
