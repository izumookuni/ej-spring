package cc.domovoi.spring.service;

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

}
