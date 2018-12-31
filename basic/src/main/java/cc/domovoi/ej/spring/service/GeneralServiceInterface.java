package cc.domovoi.ej.spring.service;

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
