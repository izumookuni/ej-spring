package cc.domovoi.spring.utils;

import cc.domovoi.collection.util.Failure;
import cc.domovoi.collection.util.Success;
import cc.domovoi.collection.util.Try;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanMap;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanMapUtils {

    private static Logger logger = LoggerFactory.getLogger(BeanMapUtils.class);

    private static ObjectMapper jackson = new ObjectMapper();

    /**
     * Convert Bean to Map.
     *
     * @param bean Bean
     * @param <T> Bean type
     * @return Map
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key.toString(), beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * Inject the Map into the Bean.
     *
     * @param map Map
     * @param bean Bean
     * @param <T> Bean type
     * @return The Bean after the Map injected
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * Copy Bean.
     *
     * @param origin Origin Bean
     * @param cover Current Bean
     * @param op The logic of coverage
     * @param <T> Bean type
     * @return The copied Bean
     */
    public static <T> T copyProperty(T origin, T cover, BiFunction<Object, Object, Object> op) {
        BeanWrapper wrapper = new BeanWrapperImpl(cover.getClass());
        Map<String, Object> currentBeanMap = BeanMapUtils.beanToMap(cover);
        Map<String, Object> originBeanMap = BeanMapUtils.beanToMap(origin);
        currentBeanMap.forEach((k, v) -> {
            try {
                wrapper.setPropertyValue(k, op.apply(originBeanMap.get(k), v));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return (T) wrapper.getWrappedInstance();
    }

    public static <T> T copyPropertyIgnoreNull(T origin, T cover, Function<Object, Object> op) {
        return copyProperty(origin, cover, (ov, cv) -> {
           if (cv == null) {
               return ov;
           }
           else {
               return op.apply(cv);
           }
        });
    }

    /**
     * Copy Bean.
     *
     * When the current Bean property is null, the original Bean property is used; Set null to the current Bean property when it is a special value; Otherwise, the current Bean attribute is used.
     *
     * @param origin Origin Bean
     * @param cover Current Bean
     * @param <T> Bean type
     * @return The copied Bean
     */
    public static <T> T copyPropertyIgnoreNullSpecial(T origin, T cover) {
        Date defaultDate = Date.from(ZonedDateTime.of(LocalDate.of(1899, 12, 31), LocalTime.MIDNIGHT, ZoneId.of("Asia/Shanghai")).toInstant());
        return copyPropertyIgnoreNull(origin, cover, (cv) -> {
            String className = cv.getClass().getSimpleName();
            if (("Integer".equals(className) && -99999 == (Integer) cv) || ("BigDecimal".equals(className) && BigDecimal.valueOf(-99999).equals((BigDecimal) cv)) || ("Date".equals(className) && defaultDate.equals((Date) cv))) {
                return null;
            }
            else {
                return cv;
            }
        });
    }

    public static <T> T copyPropertyIgnoreNull(T origin, T cover) {
        return copyPropertyIgnoreNull(origin, cover, Function.identity());
    }

    public static <T> T deepCopy(T bean) {
        return copyProperty(bean, bean, (ov, cv) -> ov);
    }

    private static <T> Try<T> convertTo(String value, Function<String, T> op) {
        return Try.apply(() -> op.apply(value.trim()));
    }

    private static Function<? super String, ? extends Try<String>> convertToString = (value) -> convertTo(value, Function.identity());

    private static Function<? super String, ? extends Try<Integer>> convertToInteger = (value) -> convertTo(value, Integer::valueOf);

    private static Function<? super String, ? extends Try<Boolean>> convertToBoolean = (value) -> convertTo(value, Boolean::valueOf);

    private static Function<? super String, ? extends Try<BigDecimal>> convertToBigDecimal = (value) -> convertTo(value, BigDecimal::new);

    private static Function<? super String, ? extends Try<Date>> convertToDate = (value) -> {
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return convertTo(value, (v) -> Date.from(ZonedDateTime.of(LocalDate.parse(v, dtf1), LocalTime.MIDNIGHT, ZoneId.of("Asia/Shanghai")).toInstant())).orElse(() -> convertTo(value, (v) -> Date.from(LocalDateTime.parse(v, dtf2).toInstant(ZoneOffset.of("+8")))));
    };

    public static Map<String, Function<? super String, ? extends Try<?>>> convertMap() {
        Map<String, Function<? super String, ? extends Try<?>>> map = new HashMap<>();
        map.put("String", convertToString);
        map.put("Integer", convertToInteger);
        map.put("Boolean", convertToBoolean);
        map.put("BigDecimal", convertToBigDecimal);
        map.put("Date", convertToDate);
        return map;
    }

    private static <T> Try<T> convertToBean(Map<String, String> propertyMap, final BeanWrapper beanWrapper) {
        try {
            T bean = deepCopy((T) beanWrapper.getWrappedInstance());
            BeanWrapper beanWrapper1 = new BeanWrapperImpl(bean);
            final Set<String> fieldSet = (Set<String>) BeanMap.create(beanWrapper.getWrappedInstance()).keySet();
            logger.debug("filedSet: " + fieldSet);
            final Map<String, Function<? super String, ? extends Try<?>>> convertMap = convertMap();
            propertyMap.entrySet().stream().filter(entry -> fieldSet.contains(entry.getKey())).forEach(entry -> {
                logger.debug(entry.getKey() + " -> " + entry.getValue());
                if (entry.getValue() != null) {
                    String propertyType = beanWrapper.getPropertyType(entry.getKey()).getSimpleName();
                    logger.debug("propertyType: " + propertyType);
                    Try<?> value = convertMap.get(propertyType).apply(entry.getValue());
                    if (value.isSuccess()) {
                        beanWrapper1.setPropertyValue(entry.getKey(), value.get());
                    }
                    else {
                        throw new RuntimeException(String.format("convert [%s] to [%s] type failed, reason: %s", entry.getValue(), propertyType, value.failed().get().getMessage()));
                    }
                }
                else {
                    beanWrapper1.setPropertyValue(entry.getKey(), null);
                }
            });
            return new Success<>((T) beanWrapper1.getWrappedInstance());
        } catch (Exception e) {
            e.printStackTrace();
            return new Failure<>(e);
        }
    }

    public static <T> Try<T> convertToBean(Class<T> beanClass, Map<String, String> propertyMap) {
        return convertToBean(propertyMap, new BeanWrapperImpl(beanClass));
    }

    public static <T> Try<T> convertToBean(T bean, Map<String, String> propertyMap) {
        return convertToBean(propertyMap, new BeanWrapperImpl(bean));
    }

    public static <T> Try<T> deleteBeanProperty(T bean, Set<String> propertySet) {
        Map<String, String> deleteMap = new HashMap<>();
        propertySet.forEach(property -> deleteMap.put(property, null));
        return convertToBean(bean, deleteMap);
    }

    public static <T> Try<T> deleteBeanProperty(T bean, String... property) {
        Set<String> propertySet = Stream.of(property).collect(Collectors.toSet());
        return deleteBeanProperty(bean, propertySet);
    }

    public static <T> Try<Set<String>> beanPropertySet(T bean) {
        try {
            return new Success<>((Set<String>) BeanMap.create(bean).keySet());
        } catch (Exception e) {
            e.printStackTrace();
            return new Failure<>(e);
        }
    }

    public static <T> Try<Set<String>> beanPropertySet(Class<T> beanClass) {
        try {
            return new Success<>((Set<String>) BeanMap.create(beanClass.newInstance()).keySet());
        } catch (Exception e) {
            e.printStackTrace();
            return new Failure<>(e);
        }
    }


}
