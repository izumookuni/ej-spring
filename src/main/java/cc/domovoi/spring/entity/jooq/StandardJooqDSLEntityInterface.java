package cc.domovoi.spring.entity.jooq;

import cc.domovoi.spring.entity.StandardJoiningEntityInterface;
import org.jooq.TableRecord;

public interface StandardJooqDSLEntityInterface<R extends TableRecord<R>> extends GeneralJooqDSLEntityInterface<String, R>, StandardJoiningEntityInterface {
}
