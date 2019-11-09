package cc.domovoi.spring.entity.jooq;

import cc.domovoi.spring.entity.GeneralAnnotationEntityInterface;

public interface GeneralJooqEntityInterface<P, K> extends GeneralAnnotationEntityInterface<K> {

    Class<P> pojoType();

    @SuppressWarnings("unchecked")
    default P toPojo() {
        return (P) this;
    }
}
