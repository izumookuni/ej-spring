package cc.domovoi.spring.annotation.method;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ForcedThrow {

    String value() default "";
}
