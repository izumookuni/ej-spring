package cc.domovoi.spring.utils;

import cc.domovoi.spring.entity.BaseJoiningEntityInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface TreeInterface<E extends TreeInterface> extends BaseJoiningEntityInterface, TreeDisplayInterface<E> {

    List<String> getCid();

    void setCid(List<String> cid);

//    List<E> getChildren();
//
//    void setChildren(List<E> children);

    Integer getOrder();

    void setOrder(Integer order);

    String getPid();

    void setPid(String pid);

    default Map<String, Supplier<? extends List<String>>> joiningKeyMap() {
        return Collections.singletonMap("children", this::getCid);
    }

    @SuppressWarnings("unchecked")
    default Map<String, Consumer<? super Object>> joiningEntityMap() {
        return Collections.singletonMap("children", (e) -> {
            if (this.getChildren() == null) {
                this.setChildren(new ArrayList<>());
            }
            if (e == null) {
                return;
            }
            this.getChildren().add((E) e);
        });
    }
}
