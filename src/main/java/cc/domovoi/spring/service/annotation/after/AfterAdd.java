package cc.domovoi.spring.service.annotation.after;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AfterAdd {

    String value() default "";

    int order() default Integer.MAX_VALUE;
}
