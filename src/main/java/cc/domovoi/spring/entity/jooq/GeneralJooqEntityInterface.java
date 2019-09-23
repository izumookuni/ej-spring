package cc.domovoi.spring.entity.jooq;

import cc.domovoi.spring.entity.GeneralJoiningEntityInterface;
import org.joor.Reflect;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.joor.Reflect.on;

public interface GeneralJooqEntityInterface<P, K> extends GeneralJoiningEntityInterface<K> {

    Class<P> pojoType();

    @SuppressWarnings("unchecked")
    default P toPojo() {
        return (P) this;
    }

    default Set<JoiningProperty> joiningPropertySet() {
        return joiningPropertySet(this.getClass());
    }

    default Map<String, Supplier<? extends List<Object>>> joiningKeyMap() {
        Map<String, Supplier<? extends List<Object>>> joiningKeyMap = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        Reflect reflect = on(this);
        for (Field field : fields) {
            JoiningProperty joiningProperty = field.getAnnotation(JoiningProperty.class);
            if (joiningProperty != null) {
                String name = field.getName();
                String joiningName = "".equals(joiningProperty.value()) ? name : joiningProperty.value();
                Class<?> fieldClass = field.getType();
                if (fieldClass.getSimpleName().matches("(List)|(ArrayList)")) {
                    joiningKeyMap.put(joiningName, () -> reflect.get(joiningProperty.sourceKey()));

                }
                else {
                    joiningKeyMap.put(joiningName, () -> Collections.singletonList(reflect.get(joiningProperty.sourceKey())));
                }
            }
        }
        return joiningKeyMap;
    }

    @SuppressWarnings("unchecked")
    default Map<String, Consumer<? super Object>> joiningEntityMap() {
        Map<String, Consumer<? super Object>> joiningEntityMap = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        Reflect reflect = on(this);
        for (Field field : fields) {
            JoiningProperty joiningProperty = field.getAnnotation(JoiningProperty.class);
            if (joiningProperty != null) {
                String name = field.getName();
                String joiningName = "".equals(joiningProperty.value()) ? name : joiningProperty.value();
                Class<?> fieldClass = field.getType();
                if (fieldClass.getSimpleName().matches("(List)|(ArrayList)")) {
                    joiningEntityMap.put(joiningName, (Object e) -> {
                        List<Object> list = reflect.get(name);
                        if (list == null) {
                            reflect.set(name, new ArrayList<>());
                        }
                        ((List<Object>) reflect.get(name)).add(e);
                    });
                }
                else {
                    joiningEntityMap.put(joiningName, (Object e) -> reflect.set(name, e));
                }
            }
        }
        return joiningEntityMap;
    }

    static Set<JoiningProperty> joiningPropertySet(Class<? extends GeneralJooqEntityInterface> eClass) {
        Field[] fields = eClass.getDeclaredFields();
        return Stream.of(fields).map(field -> field.getAnnotation(JoiningProperty.class)).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
