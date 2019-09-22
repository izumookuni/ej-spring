package cc.domovoi.spring.controller;

import cc.domovoi.spring.utils.CommonLogger;
import org.slf4j.Logger;

public interface OriginalControllerInterface {

    default Logger logger() {
        return CommonLogger.logger;
    }
}
