package cc.domovoi.spring.service.jooq;

import org.jooq.DSLContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JoiningColumnFunctionInterfaceImpl implements JoiningColumnFunctionInterface<Object> {

    @Override
    public Map apply(DSLContext dsl, List<Object> keyList, Optional<Object> query) {
        return Collections.emptyMap();
    }
}
