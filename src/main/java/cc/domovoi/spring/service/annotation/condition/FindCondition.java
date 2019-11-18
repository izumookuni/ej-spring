package cc.domovoi.spring.service.annotation.condition;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface FindCondition {

    String value() default "";

    int order() default Integer.MAX_VALUE;
}
