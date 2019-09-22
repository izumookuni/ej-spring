package cc.domovoi.spring.entity.jooq;

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
    String key();

    /**
     * JoiningTable name
     * @return JoiningTable name
     */
    String table();

    /**
     * JoiningType name
     * @return JoiningType name
     */
    JoiningType type();

    /**
     * key in joining entity.
     * nameOfKey
     *
     * @return targetKey
     */
    String targetKey() default "id";

    /**
     * key column in joining table.
     * name_of_column
     *
     * @return targetColumn
     */
    String targetColumn() default "id";
}
