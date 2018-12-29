package cc.domovoi.ej.spring.utils;

import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class RestfulUtils {

    public static Map<String, Object> fillOk(Map<String, Object> jsonMap, HttpStatus status) {
        return fill(jsonMap, status, Optional.empty(), Optional.empty());
    }

    public static Map<String, Object> fillOk(Map<String, Object> jsonMap, HttpStatus status, Object data) {
        return fill(jsonMap, status, Optional.of(data), Optional.empty());
    }

    public static Map<String, Object> fillOk(Map<String, Object> jsonMap, HttpStatus status, Object data, String message) {
        return fill(jsonMap, status, Optional.of(data), Optional.of(message));
    }
    public static Map<String, Object> fillError(Map<String, Object> jsonMap, HttpStatus status, String message) {
        return fill(jsonMap, status, Optional.empty(), Optional.of(message));
    }

    private static Map<String, Object> fill(Map<String, Object> jsonMap, HttpStatus status, Optional<Object> data, Optional<String> message) {
        jsonMap.put("timestamp", new Date());
        jsonMap.put("status", status.value());
        message.ifPresent(m -> jsonMap.put("message", m));
        data.ifPresent(d -> jsonMap.put("data", d));
        return jsonMap;
    }
}
