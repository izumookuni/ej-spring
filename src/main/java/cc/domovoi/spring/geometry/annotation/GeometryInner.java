package cc.domovoi.spring.geometry.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface GeometryInner {

    String value() default "";
}
