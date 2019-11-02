package cc.domovoi.spring.utils;

import cc.domovoi.spring.entity.StandardJoiningEntityInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Deprecated
public interface TreeInterface<E extends TreeInterface> extends StandardJoiningEntityInterface, TreeDisplayInterface<E> {

    List<String> getCid();

    void setCid(List<String> cid);

//    List<E> getChildren();
//
//    void setChildren(List<E> children);

    Integer getOrder();

    void setOrder(Integer order);

    String getPid();

    void setPid(String pid);

    @Override
    default Map<String, Supplier<? extends List<Object>>> joiningKeyMap() {
        return Collections.singletonMap("children", () -> this.getCid().stream().map(s -> (Object) s).collect(Collectors.toList()));
    }

    //    default Map<String, Supplier<? extends List<String>>> joiningKeyMap() {
//        return Collections.singletonMap("children", this::getCid);
//    }

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
