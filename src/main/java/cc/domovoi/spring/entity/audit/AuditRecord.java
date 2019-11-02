package cc.domovoi.spring.entity.audit;

import java.lang.annotation.*;

/**
 * Audit Record
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AuditRecord {

    // value
    String value() default "";

    // key
    String key() default "";

    // ignore (lowest order)
    boolean ignore() default false;

    // pid
    boolean pid() default false;

    // scope ID
    boolean scopeId() default false;

}
