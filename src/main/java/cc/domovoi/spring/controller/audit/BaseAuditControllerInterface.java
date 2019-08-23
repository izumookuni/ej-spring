package cc.domovoi.spring.controller.audit;

import cc.domovoi.spring.controller.OriginalCRUDControllerInterface;
import cc.domovoi.spring.entity.audit.AuditEntityInterface;
import cc.domovoi.spring.service.audit.AuditServiceInterface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;

public interface BaseAuditControllerInterface<E extends AuditEntityInterface> extends OriginalCRUDControllerInterface<E> {

    AuditServiceInterface auditService();

    String auditAuthorGetter();

    @Override
    default void afterAdd(E entity, HttpServletRequest request, HttpServletResponse response) {
        recordAddAuditEntity(entity, request);
    }

    @Override
    default void afterUpdate(E entity, HttpServletRequest request, HttpServletResponse response) {
        recordUpdateAuditEntity(entity, request);
    }

    @Override
    default void afterDelete(E entity, HttpServletRequest request, HttpServletResponse response) {
        recordDeleteAuditEntity(entity, request);
    }

    default void recordAddAuditEntity(E entity, HttpServletRequest request) {
        entity.setAuditBehavior("add");
        entity.setAuditType("controller");
        entity.setAuditLevel("info");
        entity.setAuditAuthor(auditAuthorGetter());
        entity.setAuditIp(getIpAddr(request));
        entity.setAuditUri(request.getRequestURI());
        entity.init();
        auditService().addEntity(entity);
    }

    default void recordUpdateAuditEntity(E entity, HttpServletRequest request) {
        entity.setAuditBehavior("update");
        entity.setAuditType("controller");
        entity.setAuditLevel("info");
        entity.setAuditAuthor(auditAuthorGetter());
        entity.setAuditIp(getIpAddr(request));
        entity.setAuditUri(request.getRequestURI());
        entity.init();
        auditService().addEntity(entity);
    }

    default void recordDeleteAuditEntity(E entity, HttpServletRequest request) {
        entity.setAuditBehavior("delete");
        entity.setAuditType("controller");
        entity.setAuditLevel("info");
        entity.setAuditAuthor(auditAuthorGetter());
        entity.setAuditIp(getIpAddr(request));
        entity.setAuditUri(request.getRequestURI());
        entity.init();
        auditService().addEntity(entity);
    }

    static String getIpAddr(HttpServletRequest request) {
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
            ipAddress="";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }
}
