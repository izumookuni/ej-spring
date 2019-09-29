package cc.domovoi.spring.service;

import cc.domovoi.spring.service.mvc.MvcServiceInterface;

/**
 * GeneralServiceInterface.
 *
 * @param <M> Mapper type.
 */
@Deprecated
public interface GeneralServiceInterface<M> extends OriginalServiceInterface, MvcServiceInterface<M> {

}
