package cc.domovoi.spring.controller;

/**
 * GeneralControllerInterface.
 *
 * @param <S> Service type.
 */
public interface MvcControllerInterface<S> {

    S service();
}
