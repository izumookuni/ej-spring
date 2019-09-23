package cc.domovoi.spring.service;

import cc.domovoi.spring.utils.CommonLogger;
import org.slf4j.Logger;

/**
 * OriginalServiceInterface.
 */
public interface OriginalServiceInterface {

    default Logger logger() {
        return CommonLogger.logger;
    }
}
