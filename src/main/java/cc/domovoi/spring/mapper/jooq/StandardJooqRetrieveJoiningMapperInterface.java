package cc.domovoi.spring.mapper.jooq;

import cc.domovoi.spring.entity.jooq.StandardJooqDSLEntityInterface;
import cc.domovoi.spring.mapper.StandardRetrieveMapperInterface;
import org.jooq.TableRecord;

public interface StandardJooqRetrieveJoiningMapperInterface<R extends TableRecord<R>, E extends StandardJooqDSLEntityInterface<R>> extends GeneralJooqRetrieveJoiningMapperInterface<R, String, E>, StandardRetrieveMapperInterface<E> {

    @Override
    default Class<String> keyClass() {
        return String.class;
    }
}
