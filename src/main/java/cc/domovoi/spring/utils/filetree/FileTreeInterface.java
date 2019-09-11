package cc.domovoi.spring.utils.filetree;

import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public interface FileTreeInterface {

    String getName();

    void setName(String name);

    Boolean isFile();

    default Boolean isFolder() {
        return !isFile();
    }

    List<FileTreeInterface> getChildren();

    void setChildren(List<FileTreeInterface> children);

    default void addChild(FileTreeInterface child) {
        if (child == null) {
            return;
        }
        if (this.isFile()) {
            throw new RuntimeException("TreeFile has not got child");
        }
        if (getChildren() == null) {
            setChildren(new ArrayList<>());
        }
        getChildren().add(child);
    }

    default void addChildren(List<FileTreeInterface> children) {
        if (children == null) {
            return;
        }
        if (this.isFile()) {
            throw new RuntimeException("TreeFile has not got child");
        }
        if (getChildren() == null) {
            setChildren(new ArrayList<>());
        }
        getChildren().addAll(children);
    }

    default TreeFile asTreeFile() {
        if (this instanceof TreeFile) {
            return (TreeFile) this;
        }
        else {
            throw new ClassCastException(String.format("%s can not cast to TreeFile", this.getClass().getCanonicalName()));
        }
    }

    default TreeFolder asTreeFolder() {
        if (this instanceof TreeFolder) {
            return (TreeFolder) this;
        }
        else {
            throw new ClassCastException(String.format("%s can not cast to TreeFolder", this.getClass().getCanonicalName()));
        }
    }

    default Tuple2<List<TreeFile>, List<TreeFolder>> partitionChildren() {
        return Seq.partition(this.getChildren().stream(), FileTreeInterface::isFile).map1(t1 -> t1.map(FileTreeInterface::asTreeFile).toList()).map2(t2 -> t2.map(FileTreeInterface::asTreeFolder).toList());
    }

}
