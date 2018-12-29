package cc.domovoi.ej.spring.utils;

import cc.domovoi.ej.collection.util.Either;
import cc.domovoi.ej.collection.util.Left;
import cc.domovoi.ej.collection.util.Right;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
                                return file.toOption();
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
}
