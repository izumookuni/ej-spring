package cc.domovoi.spring.annotation.after;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AfterDelete {

    String value() default "";

    int order() default Integer.MAX_VALUE;

    int scope() default 0;

    String[] skip() default {};

    String[] include() default {};
}
