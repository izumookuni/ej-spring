package cc.domovoi.spring.entity.audit;

import java.lang.annotation.*;

/**
 * Audit
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Audit {

    // value
    String value();

    // skip properties (high order)
    String[] skip() default {};

    // include properties (low order)
    String[] include() default {};

    // include all properties (low order)
    boolean containsAll() default false;
}
