package cc.domovoi.spring.service.annotation.before;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BeforeAdd {

    String value() default "";

    int order() default Integer.MAX_VALUE;

    int scope() default 0;
}
