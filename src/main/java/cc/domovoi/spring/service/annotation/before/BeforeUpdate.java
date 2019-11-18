package cc.domovoi.spring.service.annotation.before;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BeforeUpdate {

    String value() default "";

    int order() default Integer.MAX_VALUE;
}
