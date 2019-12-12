package cc.domovoi.spring.annotation.before;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BeforeUpdate {

    String value() default "";

    int order() default Integer.MAX_VALUE;

    int scope() default 0;
}