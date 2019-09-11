package cc.domovoi.spring.utils.filetree;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Deprecated
public class TreeFile implements FileTreeInterface {

    private String name;

    private File sourceFile;

    private Long fileSize;

    public TreeFile(String name, File sourceFile) {
        this.name = name;
        this.sourceFile = sourceFile;
        this.fileSize = null;
    }

    public TreeFile(String name, File sourceFile, Long fileSize) {
        this.name = name;
        this.sourceFile = sourceFile;
        this.fileSize = fileSize;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public Boolean isFile() {
        return true;
    }

    @Override
    public List<FileTreeInterface> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public void setChildren(List<FileTreeInterface> children) {
        throw new RuntimeException("TreeFile has not got children");
    }
}
