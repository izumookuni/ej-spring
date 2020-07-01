package cc.domovoi.spring.utils;

import cc.domovoi.spring.entity.annotation.JoiningColumn;
import cc.domovoi.spring.entity.audit.AuditUtils;
import cc.domovoi.spring.service.jooq.JoiningColumnFunctionInterfaceImpl;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.joor.Reflect;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.joor.Reflect.on;
import static org.joor.Reflect.onClass;

public class JooqUtils {

    private static Logger logger = CommonLogger.logger;

    @SuppressWarnings("unchecked")
    public static <E> void joiningColumn(List<E> entityList, Optional<E> query, Class<E> clazz, DSLContext dsl) {
        logger.debug("joiningColumn");
        if (!entityList.isEmpty()) {
//            java.lang.reflect.Field[] fields = entityClass().getDeclaredFields();
            List<java.lang.reflect.Field> fields = AuditUtils.allFieldList(clazz);
            for (java.lang.reflect.Field field : fields) {
                JoiningColumn joiningColumn = field.getAnnotation(JoiningColumn.class);
                if (joiningColumn != null) {
                    logger.debug("joiningColumn field " + field.getName());
                    List<Object> keyList = entityList.stream().map(entity -> on(entity).get(joiningColumn.key())).filter(Objects::nonNull).collect(Collectors.toList());
                    if (keyList.isEmpty()) {
                        return;
                    }
                    Map targetKeyMap;
                    if (!joiningColumn.custom().equals(JoiningColumnFunctionInterfaceImpl.class)) {
                        targetKeyMap = onClass(joiningColumn.custom()).create().call("apply", dsl, keyList, query).get();
                    }
                    else {
                        Class<? extends Table> tableClass = joiningColumn.table();
                        Table<Record> table = onClass(tableClass).create().get();
                        Field<?> foreignKeyField = table.field(joiningColumn.foreignKey());
                        Field<?> targetKeyField = table.field(joiningColumn.targetKey());
                        targetKeyMap = dsl.select(foreignKeyField, targetKeyField).from(table).where(foreignKeyField.in(keyList)).fetch().intoGroups(foreignKeyField, targetKeyField);
                    }
//                    Map targetKeyMap = dsl().select(foreignKeyField, targetKeyField).from(table).where(foreignKeyField.in(keyList)).fetch().intoGroups(foreignKeyField, targetKeyField);
                    entityList.forEach(entity -> {
                        Reflect reflect = on(entity);
                        if (targetKeyMap.containsKey(reflect.get(joiningColumn.key()))) {
                            Class<?> fieldClass = field.getType();
                            if (fieldClass.getSimpleName().matches("(List)|(ArrayList)")) {
                                reflect.set(field.getName(), targetKeyMap.get(reflect.get(joiningColumn.key())));
                            }
                            else {
                                reflect.set(field.getName(), ((List<Object>) targetKeyMap.get(reflect.get(joiningColumn.key()))).get(0));
                            }
                        }
                    });
                }
            }
        }
    }

}
