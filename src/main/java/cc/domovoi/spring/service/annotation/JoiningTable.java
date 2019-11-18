package cc.domovoi.spring.service.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface JoiningTable {

    String[] value() default "";
}
