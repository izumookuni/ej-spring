package cc.domovoi.spring.service;

import cc.domovoi.spring.utils.CommonLogger;
import org.slf4j.Logger;

/**
 * GeneralServiceInterface.
 *
 * @param <M> Mapper type.
 */
public interface GeneralServiceInterface<M> extends OriginalServiceInterface {

    /**
     * Mapper.
     *
     * @return Mapper.
     */
    M mapper();

    default Logger logger() {
        return CommonLogger.logger;
    }
}
