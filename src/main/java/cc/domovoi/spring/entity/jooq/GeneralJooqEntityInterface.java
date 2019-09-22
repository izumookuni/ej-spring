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
        Field[] fields = this.getClass().getDeclaredFields();
        return Stream.of(fields).map(field -> field.getAnnotation(JoiningProperty.class)).filter(Objects::nonNull).collect(Collectors.toSet());
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
                switch (joiningProperty.type()) {
                    case SINGLETON:
                        joiningKeyMap.put(joiningName, () -> Collections.singletonList(reflect.get(joiningProperty.key())));
                        break;
                    case LIST:
                        joiningKeyMap.put(joiningName, () -> reflect.get(joiningProperty.key()));
                        break;
                    default:
                        break;
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
                switch (joiningProperty.type()) {
                    case SINGLETON:
                        joiningEntityMap.put(joiningName, (Object e) -> reflect.set(name, e));
                        break;
                    case LIST:
                        joiningEntityMap.put(joiningName, (Object e) -> {
                            List<Object> list = reflect.get(name);
                            if (list == null) {
                                reflect.set(name, new ArrayList<>());
                            }
                            ((List<Object>) reflect.get(name)).add(e);
                        });
                        break;
                    default:
                        break;
                }
            }
        }
        return joiningEntityMap;
    }
}
