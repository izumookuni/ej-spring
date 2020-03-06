package cc.domovoi.spring.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class ApplicationConfig {

    private static Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    private static Config rootConfig;

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
        String env = System.getProperty("application.conf.env");
        logger.debug("application.conf.env: " + env);
        String file0Path = System.getProperty("application.conf");
        File file1 = new File("./config/application.conf");
        File file2 = new File("./application.conf");
        if (Objects.nonNull(file0Path)) {
            logger.debug("file0Path: " + file0Path);
            File file0 = new File(file0Path);
            if (file0.exists()) {
                logger.debug("using " + file0Path);
                rootConfig = ConfigFactory.parseFile(file0);
            }
        }
        else if (file1.exists()) {
            logger.debug("using ./config/application.conf");
            rootConfig = ConfigFactory.parseFile(file1);
        }
        else if (file2.exists()) {
            logger.debug("using ./application.conf");
            rootConfig = ConfigFactory.parseFile(file2);
        }
        else {
            logger.debug("using default");
            rootConfig = ConfigFactory.load();
        }
        if (Objects.nonNull(env)) {
            config = ConfigFactory.empty();
            if (rootConfig.hasPath(env)) {
                config = config.withFallback(rootConfig.getConfig(env));
            }
            if (rootConfig.hasPath("global")) {
                config = config.withFallback(rootConfig.getConfig("global"));
            }
        }
        else {
            // no env
            if (rootConfig.hasPath("global")) {
                config = ConfigFactory.empty();
                config = config.withFallback(rootConfig.getConfig("global"));
                config = config.withFallback(rootConfig.withoutPath("global"));
            }
            else {
                config = rootConfig;
            }
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
