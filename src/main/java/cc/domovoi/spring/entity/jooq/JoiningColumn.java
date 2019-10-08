package cc.domovoi.spring.entity.jooq;

import org.jooq.Table;

import java.lang.annotation.*;

/**
 * select table().targetKey() from this_table
 * left join table() on table().foreignKey() in this_table.key()
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface JoiningColumn {

    String value() default "";

    /**
     * key column in this table.
     * name_of_column
     *
     * @return key
     */
    String key() default "id";

    /**
     * key column in joining table.
     * name_of_column
     *
     * @return foreignKey
     */
    String foreignKey();

    /**
     * collect column in joining table.
     * name_of_column
     *
     * @return targetKey
     */
    String targetKey();

    Class<? extends Table> table();
}
