/**
 * Handles the processing of SMIS files for a specified date.
 * Created on 2025-07-23.
 * <p>
 * This handler extracts the SMIS ZIP archive for the specified
 * date into a target folder named after the same date.
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.4
 * @since 1.4
 */

package com.ccb.daily.file.pipeline.smis;

import com.ccb.daily.file.pipeline.core.Handler;
import com.ccb.daily.file.pipeline.core.ReportDateContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SMISHandler implements Handler {
    private final Path sourcePath;
    private final Path targetPath;

    public SMISHandler(String sourceDir, String targetDir) {
        this.sourcePath = Paths.get(sourceDir);
        this.targetPath = Paths.get(targetDir);
    }

    @Override
    public void handle(ReportDateContext context) throws Exception {
        String tmdt = context.tmdt;
        Path sourceDir = sourcePath.resolve(tmdt);

        System.out.println("Extracting " + tmdt + " SMIS Files ......");

        if (!Files.isDirectory(sourceDir)) {
            System.out.println("No SMIS directory found for date: " + tmdt);
            return;
        }

        Path destDir = targetPath.resolve(tmdt);
        if (Files.isDirectory(destDir)) {
            deleteDirectory(destDir);
        }
        Files.createDirectories(destDir);

        String zipFileName = "SMIS_REPORT_ALL_" + tmdt + "_710091000.zip";
        Path zipFile = sourceDir.resolve(zipFileName);

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile.toFile()))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    Path newFile = destDir.resolve(Paths.get(entry.getName()).getFileName());
                    try (OutputStream os = Files.newOutputStream(newFile, StandardOpenOption.CREATE)) {
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            os.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
        }

        System.out.println("--> Completed Extracting SMIS Files");
    }

    private void deleteDirectory(Path dir) throws IOException {
        if (!Files.exists(dir)) return;
        try (Stream<Path> walk = Files.walk(dir)) {
            walk.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            System.err.println("Failed to delete " + path);
                        }
                    });
        }
    }
}
