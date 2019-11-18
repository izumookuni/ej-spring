package cc.domovoi.spring.entity.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface JoiningProperty {

    /**
     * JoiningProperty name
     * @return name
     */
    String value() default "";

    /**
     * JoiningColumn name
     * @return JoiningColumn name
     */
    String sourceKey();

    /**
     * key in joining entity.
     * nameOfKey
     *
     * @return targetKey
     */
    String joiningKey() default "id";

    /**
     * key column in joining table.
     * name_of_column
     *
     * @return joiningColumn
     */
    String joiningColumn() default "id";
}
