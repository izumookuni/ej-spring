package cc.domovoi.spring.utils;

import org.springframework.util.StringUtils;

import java.util.function.Supplier;

@Deprecated
public class OptionalUtils {
    public static <T> T orElse(T value, Supplier<T> zero) {
        if (value == null) {
            return zero.get();
        }
        else {
            if (value instanceof String) {
                return StringUtils.hasText((String) value) ? value : zero.get();
            }
            else {
                return value;
            }
        }
    }

    public static Double orElseDouble(Double value) {
        return OptionalUtils.orElse(value, () -> 0.0);
    }

    public static Integer orElseInteger(Integer value) {
        return OptionalUtils.orElse(value, () -> 0);
    }
}
