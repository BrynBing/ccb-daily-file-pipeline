/**
 * GMPSExtractor is a utility class for extracting GMPS report files from tar archives.
 * Created on 2025-06-27.
 * <p>
 * This class is specifically designed to handle extraction of two types of tar files:
 * - Income tar: Files will be extracted directly into the root of the target directory.
 * - Outgo tar: Files will be extracted into a subdirectory named after the report date.
 * </p>
 * <p>
 * The extracted content is placed in a shared destination directory, organized by date.
 * If the target directory already exists, it will be deleted and recreated.
 * </p>
 * <p>
 * This class delegates target path construction to the caller, allowing flexibility in naming
 * depending on the report type (MX, MT, etc.).
 * </p>
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.1
 * @since 1.0
 */

package com.ccb.daily.file.pipeline;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

public class GMPSExtractor {

//    private static final Path SOURCE_DIR = Paths.get("D:\\CCB\\gmps"); // /home/ap/bde/data/gmps
    private static final Path SOURCE_DIR = Paths.get("\\\\ccbausydnas02\\data\\gmps");

    public static boolean extract(String prefix, String date, Path targetDir) throws IOException {
        String incomeName = prefix + "income_prd_" + date + ".tar";
        String outgoName  = prefix + "outgo_prd_"  + date + ".tar";

        Path incomeFile = SOURCE_DIR.resolve(incomeName);
        Path outgoFile  = SOURCE_DIR.resolve(outgoName);

        System.out.println("Extracting GMPS " + date +"Files...");

        if (!Files.exists(SOURCE_DIR)) {
            System.out.println("source dir does not exist: " + SOURCE_DIR);
            return false;
        } else {
            System.out.println("source dir: " + SOURCE_DIR);
        }

        if (!Files.exists(incomeFile) && !Files.exists(outgoFile)) {
            System.out.println("No GMPS tar files found for date " + date);
            return false;
        }

        if (Files.exists(targetDir)) {
            deleteDirectory(targetDir);
        }
        Files.createDirectories(targetDir);

        if (Files.exists(incomeFile)) {
            extractTarFile(incomeFile, targetDir);
        }
        if (Files.exists(outgoFile)) {
            extractTarFile(outgoFile, targetDir);
        }

        System.out.println("Finished extracting GMPS Files for date " + date);
        return true;
    }

    private static void extractTarFile(Path tarFilePath, Path targetDir) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("tar", "xf", tarFilePath.toString(), "-C", targetDir.toString());
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Failed to extract: " + tarFilePath);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Extraction interrupted: " + tarFilePath, e);
        }
    }

    private static void deleteDirectory(Path dir) throws IOException {
        if (!Files.exists(dir)) return;

        try (Stream<Path> stream = Files.walk(dir)) {
            stream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(file -> {
                        if (!file.delete()) {
                            System.err.println("Failed to delete: " + file.getAbsolutePath());
                        }
                    });
        }
    }

}

