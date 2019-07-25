package cc.domovoi.spring.utils;

import java.util.List;

public interface TreeDisplayInterface<E> {

    List<E> getChildren();

    void setChildren(List<E> children);
}
