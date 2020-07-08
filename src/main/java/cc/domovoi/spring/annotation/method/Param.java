package cc.domovoi.spring.annotation.method;

import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Param {

    String value();

    /**
     * Determine the rule of what time to use Param.alias rather than Param.value
     * if false, only check params contains key same as Param.value, otherwise, check the value of params whether is null or not.
     *
     * @return checkNull
     */
    boolean checkNull() default false;

    String[] alias() default {};

    String defaultValue() default ValueConstants.DEFAULT_NONE;
}
