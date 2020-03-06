package cc.domovoi.spring.test;

import cc.domovoi.spring.utils.ApplicationConfig;
import cc.domovoi.spring.utils.CommonLogger;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Objects;

public class ApplicationConfigTest {

    Logger logger = CommonLogger.logger;

    @Test
    public void testApplicationConfig() {

        String env = "dev";
        Config rootConfig = ApplicationConfig.config();
        Config config = null;

        if (Objects.nonNull(env)) {
            config = ConfigFactory.empty();

            if (rootConfig.hasPath(env)) {
                logger.debug("hasPath " + env);
                config = config.withFallback(rootConfig.getConfig(env));
            }
            if (rootConfig.hasPath("global")) {
                logger.debug("hasPath global");
                config = config.withFallback(rootConfig.getConfig("global"));
            }
        }
        else {
            config = rootConfig;
        }

        config.entrySet().forEach(entry -> logger.debug(entry.getKey() + " -> " + entry.getValue().render()));
    }
}
