package cc.domovoi.spring.entity.audit;

import io.swagger.annotations.ApiModelProperty;
import org.jooq.lambda.function.Consumer2;
import org.jooq.lambda.function.Consumer3;
import org.jooq.lambda.tuple.Tuple2;
import org.joor.Reflect;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuditUtils {

    public static List<Tuple2<Field, AuditRecord>> auditRecordList(Class<? extends AuditEntityInterface> auditClass, Predicate<? super AuditRecord> filter) {
        Field[] fields = auditClass.getDeclaredFields();
        return Stream.of(fields).map(field -> new Tuple2<>(field, field.getAnnotation(AuditRecord.class))).filter(t2 -> Objects.nonNull(t2.v2())).filter(t2 -> filter.test(t2.v2())).collect(Collectors.toList());
    }

    public static List<Tuple2<Field, AuditRecord>> auditRecordList(Class<? extends AuditEntityInterface> auditClass) {
        return auditRecordList(auditClass, ar -> true);
    }

    public static String contextName(Class<? extends AuditEntityInterface> auditClass) {
        return audit(auditClass).flatMap(audit -> !"".equals(audit.value()) ? Optional.of(audit.value()) : Optional.empty()).orElseGet(auditClass::getSimpleName);
    }

    public static Optional<Audit> audit(Class<? extends AuditEntityInterface> auditClass) {
        return Optional.ofNullable(auditClass.getAnnotation(Audit.class));
    }

    public static Optional<Tuple2<Field, AuditRecord>> contextPidField(Class<? extends AuditEntityInterface> auditClass) {
        List<Tuple2<Field, AuditRecord>> contextPidList = auditRecordList(auditClass, AuditRecord::pid);
        return contextPidList.size() == 1 ? Optional.of(contextPidList.get(0)) : Optional.empty();
    }

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

    public static List<Field> auditFieldList(Class<? extends AuditEntityInterface> auditClass) {
        Optional<Audit> auditOptional = audit(auditClass);
        Field[] fields = auditClass.getDeclaredFields();
        return auditOptional.map(audit -> {
            List<String> include = Arrays.asList(audit.include());
            List<String> skip = Arrays.asList(audit.skip());
            List<String> inner = auditRecordList(auditClass, auditRecord -> !auditRecord.ignore()).stream().map(t2 -> t2.v1().getName()).collect(Collectors.toList());
            return Stream.of(fields).filter(field -> !skip.contains(field.getName()) && (audit.containsAll() || include.contains(field.getName()) || (include.isEmpty() && inner.contains(field.getName())))).collect(Collectors.toList());
        }).orElseGet(() -> auditRecordList(auditClass, auditRecord -> !auditRecord.ignore()).stream().map(Tuple2::v1).collect(Collectors.toList()));
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
            ipAddress="";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }

}
