package cc.domovoi.spring.service.jooq;

import org.jooq.DSLContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JoiningColumnFunctionInterfaceImpl implements JoiningColumnFunctionInterface {

    @Override
    public Map apply(DSLContext dsl, List<Object> keyList) {
        return Collections.emptyMap();
    }
}
