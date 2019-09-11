package cc.domovoi.spring.utils;

import cc.domovoi.collection.util.*;
import cc.domovoi.spring.utils.filetree.FileTreeInterface;
import cc.domovoi.spring.utils.filetree.TreeFile;
import cc.domovoi.spring.utils.filetree.TreeFolder;
import org.apache.commons.io.IOUtils;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Deprecated
public class CompressUtils {

    /**
     * Compress file.
     *
     * @param fileList A list of files that need to be compressed.
     * @param output Output file.
     * @param fileNameMap Filename conversion rule.
     * @return If the operation succeeds, return the output compressed file; if it fails, return the reason of failure.
     */
    public static Either<String, File> compress(List<File> fileList, File output, Function<File, String> fileNameMap) {
        try {
            if (fileList.isEmpty() || output.isDirectory() || (!output.getParentFile().exists() && output.getParentFile().mkdirs())) {
                return new Left<>("fileList is empty, or output is not file, or can not mkdir");
            }
            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(output))) {
                zos.setComment(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                List<Boolean> compressResult = fileList.stream().map(file -> {
                    String mappedFileName = fileNameMap.apply(file);
                    try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                        ZipEntry zipEntry = new ZipEntry(mappedFileName);
                        zipEntry.setSize(file.length());
                        zos.putNextEntry(zipEntry);
                        IOUtils.copy(bis, zos);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }).collect(Collectors.toList());
                if (compressResult.stream().allMatch(Boolean::booleanValue)) {
                    return new Right<>(output);
                }
                else {
                    if (output.delete()) {
                        return new Left<>("compressResult not all successful");
                    }
                    else {
                        return new Left<>("compressResult not all successful, and delete output failure");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Left<>(e.getMessage());
        }
    }

    /**
     * Compress file.
     *
     * @param fileList A list of files that need to be compressed.
     * @param output Output file.
     * @return If the operation succeeds, return the output compressed file; if it fails, return the reason of failure.
     */
    public static Either<String, File> compress(List<File> fileList, File output) {
        return compress(fileList, output, File::getName);
    }

    public static Either<String, List<File>> uncompress(File compressedFile, File outpath) {
        try {
            if (compressedFile.exists() && outpath.isDirectory() && (outpath.exists() || outpath.mkdirs())) {
                ZipFile zipFile = new ZipFile(compressedFile);
                try (ZipInputStream zis = new ZipInputStream(new FileInputStream(compressedFile))) {
                    List<Either<Optional<File>, File>> uncompressFile = new ArrayList<>();
                    ZipEntry zipEntry = zis.getNextEntry();
                    while (zipEntry != null) {
                        File outputFile = new File(outpath.getPath() + File.separator + zipEntry.getName());
                        if (!outputFile.exists() && !outputFile.createNewFile()) {
                            uncompressFile.add(new Left<>(Optional.empty()));
                            continue;
                        }
                        try (
                                BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
                                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
                                ) {
                            IOUtils.copy(bis, bos);
                            uncompressFile.add(new Right<>(outputFile));
                        } catch (Exception e) {
                            e.printStackTrace();
                            uncompressFile.add(new Left<>(Optional.of(outputFile)));
                        }
                        zipEntry = zis.getNextEntry();
                    }
                    if (uncompressFile.stream().allMatch(Either::isRight)) {
                        return new Right<>(uncompressFile.stream().map(file -> file.right().get()).collect(Collectors.toList()));
                    }
                    else {
                        List<Boolean> deleteResult = uncompressFile.stream().map(file -> {
                            if (file.isRight()) {
                                return file.toOptional();
                            }
                            else {
                                return file.left().get();
                            }
                        }).filter(Optional::isPresent).map(Optional::get).map(File::delete).collect(Collectors.toList());
                        if (deleteResult.stream().allMatch(Boolean::booleanValue)) {
                            return new Left<>("uncompressResult not all successful");
                        }
                        else {
                            return new Left<>("uncompressResult not all successful, and delete output failure");
                        }

                    }
                }
            }
            else {
                return new Left<>("compressedFile no exists, or outpath is not directory");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Left<>(e.getMessage());
        }
    }

    public static Try<File> initCompressFileTree(File outputFile, List<FileTreeInterface> fileTree) {
        try(ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputFile))) {
            zos.setComment(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            Map<String, List<FileTreeInterface>> currentFileTreeMap = Collections.singletonMap("", fileTree);
            while (!currentFileTreeMap.isEmpty()) {
                Tuple2<Map<String, List<TreeFile>>, Map<String, List<TreeFolder>>> partitionFileTreeMap = Seq.partition(currentFileTreeMap.entrySet().stream().flatMap(entry -> entry.getValue().stream().map(fTI -> new Tuple2<>(entry.getKey(), fTI))), t2 -> t2.v2.isFile())
                        .map1(seq -> seq.collect(Collectors.groupingBy(t2 -> t2.v1, Collectors.mapping(t2 -> t2.v2.asTreeFile(), Collectors.toList()))))
                        .map2(seq -> seq.collect(Collectors.groupingBy(t2 -> t2.v1, Collectors.mapping(t2 -> t2.v2.asTreeFolder(), Collectors.toList()))));
                Map<String, List<TreeFile>> treeFileMap = partitionFileTreeMap.v1();
                Map<String, List<TreeFolder>> treeFolderMap = partitionFileTreeMap.v2();
                treeFileMap.forEach((key, tFList) -> tFList.forEach(tF -> {
                    String fileName = key + (StringUtils.hasText(key) ? "/" : "") + tF.getName();
                    File file = tF.getSourceFile();
                    Assert.isTrue(file.exists(), String.format("WTF! file %s no exists!", file.getName()));
                    try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                        ZipEntry zipEntry = new ZipEntry(fileName);
                        zipEntry.setSize(Objects.nonNull(tF.getFileSize()) ? tF.getFileSize() : file.length());
                        zos.putNextEntry(zipEntry);
                        IOUtils.copy(bis, zos);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
                currentFileTreeMap = Seq.toMap(treeFolderMap.entrySet().stream().flatMap(entry -> entry.getValue().stream().flatMap(tF -> {
                    String subKey = entry.getKey() + (StringUtils.hasText(entry.getKey()) ? "/" : "") + tF.getName();
                    return Stream.of(new Tuple2<>(subKey, tF.getChildren()));
                })));
            }
            return new Success<>(outputFile);
        } catch (Exception e) {
            e.printStackTrace();
            return new Failure<>(e);
        }
    }
}
