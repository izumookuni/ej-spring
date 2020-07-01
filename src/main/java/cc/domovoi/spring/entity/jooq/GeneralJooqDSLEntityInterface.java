package cc.domovoi.spring.entity.jooq;

import cc.domovoi.spring.entity.GeneralAnnotationEntityInterface;
import org.jooq.TableRecord;

public interface GeneralJooqDSLEntityInterface<K, R extends TableRecord<R>> extends GeneralAnnotationEntityInterface<K> {
}
