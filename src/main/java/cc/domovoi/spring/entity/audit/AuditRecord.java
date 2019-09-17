package cc.domovoi.spring.entity.audit;

import java.lang.annotation.*;

/**
 * 审计记录属性
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AuditRecord {

    // 含义
    String value() default "";

    // 关键字
    String key() default "";

    // 是否忽略（最低优先）
    boolean ignore() default false;

    // pid
    boolean pid() default false;

}
