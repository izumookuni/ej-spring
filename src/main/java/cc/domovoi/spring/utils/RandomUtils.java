package cc.domovoi.spring.utils;

import cc.domovoi.spring.entity.geometry.GeometryMultipleJoiningEntityInterface;
import cc.domovoi.spring.geometry.impl.geometricmodel.GeoPoint;
import cc.domovoi.spring.geometry.impl.geometricmodel.GeometricModel;
import cc.domovoi.spring.geometry.impl.lgeometry.LArea;
import cc.domovoi.spring.geometry.impl.lgeometry.LLine;
import cc.domovoi.spring.geometry.impl.lgeometry.LPoint;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RandomUtils {

    private static Random r = new Random();

    private static Double xMin = 120.2;

    private static Double xMax = 121.0;

    private static Double yMin = 32.4;

    private static Double yMax = 32.7;

    private static List<String> ignoreKey = Arrays.asList("id", "creationTime", "updateTime", "pageNum", "pageSize", "sortBy", "sortOrder");


    // 62
    private static String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";

    private static int randomPosiNega() {
        return (r.nextInt(2) == 1 ? 1 : -1);
    }

    public static Integer randomInteger(Integer from, Integer until) {
        return from + r.nextInt(until - from);
    }

    public static Integer randomInteger(Integer until) {
        return r.nextInt(until);
    }

    public static Integer randomInteger() {
        return r.nextInt(Integer.MAX_VALUE) * randomPosiNega();
    }

    public static String randomString(Integer length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(str.charAt(r.nextInt(62)));
        }
        return sb.toString();
    }

    public static String randomString() {
        return randomString(10);
    }

    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    public static Double randomDouble(Double from, Double until) {
        return from + (until - from) * r.nextDouble();
    }

    public static Double randomDouble(Double until) {
        return r.nextDouble() * until * randomPosiNega();
    }

    public static Double randomDouble() {
        return r.nextDouble() * randomPosiNega();
    }

    public static Boolean randomBoolean() {
        return r.nextBoolean();
    }

    public static LocalTime randomLocalTime() {
        return LocalTime.of(r.nextInt(24), r.nextInt(60), r.nextInt(60));
    }

    public static LocalDate randomLocalDate() {
        return LocalDate.of(randomInteger(1970, 2031), r.nextInt(12) + 1, r.nextInt(28) + 1);
    }

    public static LocalDateTime randomLocalDateTime() {
        return LocalDateTime.of(randomLocalDate(), randomLocalTime());
    }

    public static LPoint randomLPoint() {
        return new LPoint(randomDouble(xMin, xMax), randomDouble(yMin, yMax));
    }

    public static LLine randomLLine() {
        return new LLine(IntStream.range(0, randomInteger(1, 10)).mapToObj(i -> randomLPoint()).collect(Collectors.toList()));
    }

    public static LArea randomLArea() {
        List<LPoint> points = IntStream.range(0, randomInteger(1, 10)).mapToObj(i -> randomLPoint()).collect(Collectors.toList());
        points.add(points.get(0));
        return new LArea(points);
    }

    public static List<GeometricModel> randomSingletonPointModel() {
        return Collections.singletonList(new GeometricModel("point" , Collections.singletonList(new GeoPoint(randomDouble(xMin, xMax), randomDouble(yMin, yMax)))));
    }

    public static List<GeometricModel> randomSingletonLineModel() {
        return Collections.singletonList(new GeometricModel("polyline" , IntStream.rangeClosed(0, randomInteger(1, 10)).mapToObj(i -> new GeoPoint(randomDouble(xMin, xMax), randomDouble(yMin, yMax))).collect(Collectors.toList())));
    }

    public static List<GeometricModel> randomSingletonAreaModel() {
        List<GeoPoint> points = IntStream.rangeClosed(0, randomInteger(2, 10)).mapToObj(i -> new GeoPoint(randomDouble(xMin, xMax), randomDouble(yMin, yMax))).collect(Collectors.toList());
        points.add(points.get(0));
        return Collections.singletonList(new GeometricModel("polygon" , points));
    }

    private static Map<String, Supplier<Object>> generateMap() {
        Map<String, Supplier<Object>> gMap = new HashMap<>();
        gMap.put("Integer", RandomUtils::randomInteger);
        gMap.put("Double", RandomUtils::randomDouble);
        gMap.put("Boolean", RandomUtils::randomBoolean);
        gMap.put("String", RandomUtils::randomString);
        gMap.put("LocalTime", RandomUtils::randomLocalTime);
        gMap.put("LocalDate", RandomUtils::randomLocalDate);
        gMap.put("LocalDateTime", RandomUtils::randomLocalDateTime);
        return gMap;
    }

    public static Map<String, Supplier<Object>> generateGeometryMap() {
        Map<String, Supplier<Object>> gMap = new HashMap<>();
        gMap.put("LPoint", RandomUtils::randomLPoint);
        gMap.put("LLine", RandomUtils::randomLLine);
        gMap.put("LArea", RandomUtils::randomLArea);
        return gMap;
    }

    public static Map<String, Supplier<Object>> generateGeometricModelMap() {
        Map<String, Supplier<Object>> gMap = new HashMap<>();
        gMap.put("LPoint", RandomUtils::randomSingletonPointModel);
        gMap.put("LLine", RandomUtils::randomSingletonLineModel);
        gMap.put("LArea", RandomUtils::randomSingletonAreaModel);
        return gMap;
    }

    private static void putBeanProperty(Set<String> fieldSet, List<String> ignoreList, BeanWrapper beanWrapper, Map<String, Supplier<Object>> gMap) {
        fieldSet.stream().filter(f -> !ignoreList.contains(f)).forEach(field -> {
            try {
                String propertyType = beanWrapper.getPropertyType(field).getSimpleName();
                if (gMap.containsKey(propertyType)) {
                    beanWrapper.setPropertyValue(field, gMap.get(propertyType).get());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static <INNER, OUTER, E extends GeometryMultipleJoiningEntityInterface<INNER, OUTER>> void putGeometryProperty(E entity, Map<String, Supplier<Object>> outerModelMap, Map<String, String> geometryPropertyTypeMap) {
        entity.geometryGetMap().forEach((key, supplier) -> {
            try {
                String propertyType = geometryPropertyTypeMap.get(key);
                if (outerModelMap.containsKey(propertyType)) {
                    entity.geometricSetMap().get(key).accept((OUTER) outerModelMap.get(propertyType).get());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static <E> E randomEntity(Class<E> beanClass, List<String> ignoreList) {
        try {
            E bean = beanClass.newInstance();
            BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
            final Set<String> fieldSet = (Set<String>) BeanMap.create(beanWrapper.getWrappedInstance()).keySet();
            Map<String, Supplier<Object>> gMap = generateMap();
            putBeanProperty(fieldSet, ignoreList, beanWrapper, gMap);
            return (E) beanWrapper.getWrappedInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <E> E randomEntity(Class<E> beanClass, String... ignore) {
        return randomEntity(beanClass, Stream.concat(Stream.of("id", "creationTime", "updateTime"), Stream.of(ignore)).collect(Collectors.toList()));
    }

    public static <E> E randomEntity(Class<E> beanClass) {
        return randomEntity(beanClass, Arrays.asList("id", "creationTime", "updateTime"));
    }

    public static <INNER, OUTER, E extends GeometryMultipleJoiningEntityInterface<INNER, OUTER>> E randomGeometryEntity(Class<E> beanClass, Map<String, String> geometryPropertyTypeMap, List<String> ignoreList) {
        try {
            E bean = beanClass.newInstance();
            BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
            final Set<String> fieldSet = (Set<String>) BeanMap.create(beanWrapper.getWrappedInstance()).keySet();
            Map<String, Supplier<Object>> gMap = generateMap();
//            Map<String, Supplier<Object>> gGeometryMap = generateGeometryMap();
            Map<String, Supplier<Object>> gGeometricModelMap = generateGeometricModelMap();
            Set<String> geoKey = bean.geometricGetMap().keySet();
            putBeanProperty(fieldSet, ignoreList, beanWrapper, gMap);
            putGeometryProperty(bean, gGeometricModelMap, geometryPropertyTypeMap);
            return (E) beanWrapper.getWrappedInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <INNER, OUTER, E extends GeometryMultipleJoiningEntityInterface<INNER, OUTER>> E randomGeometryEntity(Class<E> beanClass, Map<String, String> geometryPropertyTypeMap, String... ignore) {
        return randomGeometryEntity(beanClass, geometryPropertyTypeMap, Stream.concat(ignoreKey.stream(), Stream.of(ignore)).collect(Collectors.toList()));
    }

    public static <INNER, OUTER, E extends GeometryMultipleJoiningEntityInterface<INNER, OUTER>> E randomGeometryEntity(Class<E> beanClass, Map<String, String> geometryPropertyTypeMap) {
        return randomGeometryEntity(beanClass,geometryPropertyTypeMap, ignoreKey);
    }
}
