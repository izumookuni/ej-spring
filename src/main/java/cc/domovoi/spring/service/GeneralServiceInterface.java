package cc.domovoi.spring.service;

import cc.domovoi.spring.utils.CommonLogger;
import org.slf4j.Logger;

/**
 * GeneralServiceInterface.
 *
 * @param <M> Mapper type.
 */
@Deprecated
public interface GeneralServiceInterface<M> extends OriginalServiceInterface, MvcServiceInterface<M> {

}
