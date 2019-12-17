package cc.domovoi.spring.entity.audit;

import io.swagger.annotations.ApiModelProperty;
import org.jooq.lambda.function.Consumer2;
import org.jooq.lambda.function.Consumer3;
import org.jooq.lambda.tuple.Tuple2;
import org.joor.Reflect;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuditUtils {

    public static List<Field> allFieldList(Class<?> clazz) {
        Map<String, Field> result = new HashMap<>();
        Class<?> t = clazz;
        do {
            for (Field field : t.getDeclaredFields()) {
//                if (type != object ^ Modifier.isStatic(field.getModifiers())) {
                    String name = field.getName();

                    if (!result.containsKey(name)) {
                        result.put(name, field);
                    }
//                }
            }

            t = t.getSuperclass();
        }
        while (t != null);

        return new ArrayList<>(result.values());
    }

    public static List<Method> allMethodList(Class<?> clazz) {
        Map<String, Method> result = new HashMap<>();
        Class<?> t = clazz;
        do {
            for (Method method : t.getDeclaredMethods()) {
                String name = method.getName();

                if (!result.containsKey(name)) {
                    result.put(name, method);
                }
            }
            t = t.getSuperclass();
        }
        while (t != null);

        return new ArrayList<>(result.values());
    }

    public static List<Tuple2<Field, AuditRecord>> auditRecordList(Class<? extends GeneralAuditEntityInterface> auditClass, Predicate<? super AuditRecord> filter) {
//        Field[] fields = auditClass.getDeclaredFields();
//        return Stream.of(fields).map(field -> new Tuple2<>(field, field.getAnnotation(AuditRecord.class))).filter(t2 -> Objects.nonNull(t2.v2())).filter(t2 -> filter.test(t2.v2())).collect(Collectors.toList());
        Optional<Audit> auditOptional = audit(auditClass);
        return auditOptional.flatMap(audit -> {
            if (audit.record().length != 0) {
                List<Field> fieldList = allFieldList(auditClass);
                return Optional.of(Stream.of(audit.record()).map(auditRecord -> {
                    Optional<Field> fieldOptional = fieldList.stream().filter(field -> Objects.equals(field.getName(), auditRecord.target())).findFirst();
                    return fieldOptional.map(field -> new Tuple2<>(field, auditRecord));
                }).filter(Optional::isPresent).map(Optional::get).filter(t2 -> filter.test(t2.v2())).collect(Collectors.toList()));
            }
            else {
                return Optional.empty();
            }
        }).orElseGet(() -> allFieldList(auditClass).stream().map(field -> new Tuple2<>(field, field.getAnnotation(AuditRecord.class))).filter(t2 -> Objects.nonNull(t2.v2())).filter(t2 -> filter.test(t2.v2())).collect(Collectors.toList()));
//        return allFieldList(auditClass).stream().map(field -> new Tuple2<>(field, field.getAnnotation(AuditRecord.class))).filter(t2 -> Objects.nonNull(t2.v2())).filter(t2 -> filter.test(t2.v2())).collect(Collectors.toList());
    }

    public static List<Tuple2<Field, AuditRecord>> auditRecordList(Class<? extends GeneralAuditEntityInterface> auditClass) {
        return auditRecordList(auditClass, ar -> true);
    }

    public static String contextName(Class<? extends GeneralAuditEntityInterface> auditClass) {
        return audit(auditClass).flatMap(audit -> !"".equals(audit.value()) ? Optional.of(audit.value()) : Optional.empty()).orElseGet(auditClass::getSimpleName);
    }

    public static Optional<Audit> audit(Class<? extends GeneralAuditEntityInterface> auditClass) {
        return Optional.ofNullable(auditClass.getAnnotation(Audit.class));
    }

    public static Optional<Tuple2<Field, AuditRecord>> contextPidField(Class<? extends GeneralAuditEntityInterface> auditClass) {
        List<Tuple2<Field, AuditRecord>> contextPidList = auditRecordList(auditClass, AuditRecord::pid);
        return contextPidList.size() == 1 ? Optional.of(contextPidList.get(0)) : Optional.empty();
    }

    public static Optional<Tuple2<Field, AuditRecord>> scopeIdField(Class<? extends GeneralAuditEntityInterface> auditClass) {
        List<Tuple2<Field, AuditRecord>> scopeIdList = auditRecordList(auditClass, AuditRecord::scopeId);
        return scopeIdList.size() == 1 ? Optional.of(scopeIdList.get(0)) : Optional.empty();
    }

    public static List<Tuple2<Field, AuditRecord>> scopeIdFieldList(Class<? extends GeneralAuditEntityInterface> auditClass) {
        return auditRecordList(auditClass, AuditRecord::scopeId);
    }

    @Deprecated
    public static void processAuditFieldList(List<Field> auditFieldList, Consumer2<String, Optional<ApiModelProperty>> noAuditRecordOp, Consumer3<String, AuditRecord, Optional<ApiModelProperty>> fieldNameAuditRecordOp1, Consumer3<String, AuditRecord, Optional<ApiModelProperty>> fieldNameAuditRecordOp2, Predicate<? super AuditRecord> auditRecordPredicate) {
        auditFieldList.forEach(field -> {
            AuditRecord auditRecord = field.getAnnotation(AuditRecord.class);
            Optional<ApiModelProperty> apiModelPropertyOptional = Optional.ofNullable(field.getAnnotation(ApiModelProperty.class));
            String name = field.getName();
            if (Objects.isNull(auditRecord)) {
                noAuditRecordOp.accept(name, apiModelPropertyOptional);
            }
            else if (!auditRecord.ignore() && auditRecordPredicate.test(auditRecord)) {
                fieldNameAuditRecordOp1.accept(name, auditRecord, apiModelPropertyOptional);
            }
            else if (!auditRecord.ignore()) {
                fieldNameAuditRecordOp2.accept(name, auditRecord, apiModelPropertyOptional);
            }
        });
    }

    public static void processAuditFieldListV2(List<Tuple2<Field, Optional<AuditRecord>>> auditFieldList, Consumer2<String, Optional<ApiModelProperty>> noAuditRecordOp, Consumer3<String, AuditRecord, Optional<ApiModelProperty>> fieldNameAuditRecordOp1, Consumer3<String, AuditRecord, Optional<ApiModelProperty>> fieldNameAuditRecordOp2, Predicate<? super AuditRecord> auditRecordPredicate) {
        auditFieldList.forEach(fR -> {
//            AuditRecord auditRecord = field.getAnnotation(AuditRecord.class);
            Field field = fR.v1();
            Optional<AuditRecord> auditRecord = fR.v2();
            Optional<ApiModelProperty> apiModelPropertyOptional = Optional.ofNullable(field.getAnnotation(ApiModelProperty.class));
            String name = field.getName();
            if (!auditRecord.isPresent()) {
                noAuditRecordOp.accept(name, apiModelPropertyOptional);
            }
            else if (!auditRecord.get().ignore() && auditRecordPredicate.test(auditRecord.get())) {
                fieldNameAuditRecordOp1.accept(name, auditRecord.get(), apiModelPropertyOptional);
            }
            else if (!auditRecord.get().ignore()) {
                fieldNameAuditRecordOp2.accept(name, auditRecord.get(), apiModelPropertyOptional);
            }
        });
    }

    @Deprecated
    public static List<Field> auditFieldList(Class<? extends GeneralAuditEntityInterface> auditClass) {
        Optional<Audit> auditOptional = audit(auditClass);
//        Field[] fields = auditClass.getDeclaredFields();
        List<Field> fieldList = allFieldList(auditClass);
        return auditOptional.map(audit -> {
            List<String> include = Arrays.asList(audit.include());
            List<String> skip = Arrays.asList(audit.skip());
            List<String> inner = auditRecordList(auditClass, auditRecord -> !auditRecord.ignore()).stream().map(t2 -> t2.v1().getName()).collect(Collectors.toList());
            return fieldList.stream().filter(field -> !skip.contains(field.getName()) && (audit.containsAll() || include.contains(field.getName()) || (include.isEmpty() && inner.contains(field.getName())))).collect(Collectors.toList());
        }).orElseGet(() -> auditRecordList(auditClass, auditRecord -> !auditRecord.ignore()).stream().map(Tuple2::v1).collect(Collectors.toList()));
    }

    public static List<Tuple2<Field, Optional<AuditRecord>>> auditFieldListV2(Class<? extends GeneralAuditEntityInterface> auditClass) {
        Optional<Audit> auditOptional = audit(auditClass);
        List<Field> fieldList = allFieldList(auditClass);
        return auditOptional.map(audit -> {
            List<Tuple2<Field, AuditRecord>> auditRecordFieldList = auditRecordList(auditClass, auditRecord -> !auditRecord.ignore());
            List<String> include = Arrays.asList(audit.include());
            List<String> skip = Arrays.asList(audit.skip());
            List<String> inner = auditRecordFieldList.stream().map(t2 -> t2.v1().getName()).collect(Collectors.toList());
//            return auditRecordFieldList.stream().filter(fR -> !skip.contains(fR.v1().getName()) && (audit.containsAll() || include.contains(fR.v1().getName()) || (include.isEmpty() && inner.contains(fR.v1().getName())))).collect(Collectors.toList());
            return fieldList.stream().filter(field -> !skip.contains(field.getName()) && (audit.containsAll() || include.contains(field.getName()) || (include.isEmpty() && inner.contains(field.getName())))).map(field -> {
                Optional<AuditRecord> auditRecord = auditRecordFieldList.stream().filter(fR -> Objects.equals(fR.v1().getName(), field.getName())).map(Tuple2::v2).findFirst();
                return new Tuple2<>(field, auditRecord);
            }).collect(Collectors.toList());
        }).orElseGet(() -> auditRecordList(auditClass, auditRecord -> !auditRecord.ignore()).stream().map(t2 -> t2.map2(Optional::of)).collect(Collectors.toList()));
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ipAddress = "";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }

}
