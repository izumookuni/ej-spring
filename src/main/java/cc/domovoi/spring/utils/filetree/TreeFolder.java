package cc.domovoi.spring.utils.filetree;

import java.util.ArrayList;
import java.util.List;

public class TreeFolder implements FileTreeInterface {

    private String name;

    private List<FileTreeInterface> children;

    public TreeFolder(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public TreeFolder(String name, List<FileTreeInterface> children) {
        this.name = name;
        this.children = children;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<FileTreeInterface> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<FileTreeInterface> children) {
        this.children = children;
    }

    @Override
    public Boolean isFile() {
        return false;
    }
}
