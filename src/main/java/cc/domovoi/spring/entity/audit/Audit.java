package cc.domovoi.spring.entity.audit;

import java.lang.annotation.*;

/**
 * 审计记录实体
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Audit {

    // 含义
    String value();

    // 忽略属性（高优先）
    String[] skip() default {};

    // 包含属性（低优先）
    String[] include() default {};

    // 包含所有属性（低优先）
    boolean containsAll() default false;
}
