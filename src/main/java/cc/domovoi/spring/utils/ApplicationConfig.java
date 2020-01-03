package cc.domovoi.spring.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ApplicationConfig {

    private static Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    private static Config config;

    private static LocalDateTime lastUpdateTime;

    private static final Long expires = 5L;

    static {
        loadConfig();
        lastUpdateTime = LocalDateTime.now();
    }

    private static void loadConfig() {
        try {
            logger.debug(String.format("workspace: %s", new File(".").getCanonicalPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file1 = new File("./config/application.conf");
        File file2 = new File("./application.conf");
        if (file1.exists()) {
            config = ConfigFactory.parseFile(file1);
        }
        else if (file2.exists()) {
            config = ConfigFactory.parseFile(file2);
        }
        else {
            config = ConfigFactory.load();
        }
    }

    public static void reloadConfig() {
        loadConfig();
        lastUpdateTime = LocalDateTime.now();
    }

    public static void lazyReloadConfig() {
        LocalDateTime now = LocalDateTime.now();
        if (ChronoUnit.MINUTES.between(lastUpdateTime, now) >= expires) {
            loadConfig();
            lastUpdateTime = now;
        }
    }

    public static Config config() {
//        lazyReloadConfig();
        return config;
    }

}
