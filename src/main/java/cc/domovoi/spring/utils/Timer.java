package cc.domovoi.spring.utils;

import org.jooq.lambda.tuple.Tuple2;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Timer {

    private static boolean enable = true;

    private static Map<String, LocalDateTime> recordStartTime = new HashMap<>();

    private static Map<String, Duration> recordDurationMap = new HashMap<>();

    public static void enable() {
        enable = true;
    }

    public static void disable() {
        enable = false;
    }

    public static void start(String key) {
        if (!enable) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        if (recordStartTime.containsKey(key)) {
            recordStartTime.replace(key, now);
        }
        else {
            recordStartTime.put(key, now);
        }
    }

    public static void stop(String key) {
        if (!enable) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        if (recordStartTime.containsKey(key)) {
            LocalDateTime startTime = recordStartTime.get(key);
            Duration duration = Duration.between(startTime, now);
            if (recordDurationMap.containsKey(key)) {
                recordDurationMap.replace(key, recordDurationMap.get(key).plus(duration));
            }
            else {
                recordDurationMap.put(key, duration);
            }
            recordStartTime.remove(key);
        }
    }

    public static Map<String, Long> duration(ChronoUnit chronoUnit) {
        if (!enable) {
            return Collections.emptyMap();
        }
        return recordDurationMap.entrySet().stream().map(entry -> new Tuple2<>(entry.getKey(), entry.getValue().get(chronoUnit))).collect(Collectors.toMap(Tuple2::v1, Tuple2::v2));
    }

    public static Map<String, Long> durationMillis() {
        return duration(ChronoUnit.NANOS);
    }

    public static void clear() {
        if (!enable) {
            return;
        }
        recordStartTime.clear();
        recordDurationMap.clear();
    }
}
