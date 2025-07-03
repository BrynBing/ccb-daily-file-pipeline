/**
 * GMPSExtractor is a utility class for extracting GMPS report files from tar archives.
 *
 * This class is specifically designed to handle extraction of two types of tar files:
 * - Income tar: Files will be extracted directly into the root of the target directory.
 * - Outgo tar: Files will be extracted into a subdirectory named after the report date.
 *
 * The extracted content is placed in a shared destination directory (e.g., gmps_share),
 * organized by date. If the target directory already exists, it will be deleted and recreated.
 *
 * This class delegates target path construction to the caller, allowing flexibility in naming
 * (e.g., "20250630MX" or just "20250630"), depending on the report type (MX or MT).
 *
 * Note: This class uses the system tar command and requires it to be available in the environment path.
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.0
 * @since 2025-06-27
 */


package com.ccb.daily.file.pipeline.message.ingestion;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;

public class GMPSExtractor {

    private static final Path SOURCE_DIR = Paths.get("D:\\CCB\\gmps"); // /home/ap/bde/data/gmps
//    private static final Path SOURCE_DIR = Paths.get("C:\\Test\\gmps");

    public static void extract(String prefix, String date, Path targetDir) throws IOException {
//        Path targetDir = TARGET_ROOT.resolve(date + "MX");

        String incomeName = prefix + "income_prd_" + date + ".tar";
        String outgoName  = prefix + "outgo_prd_"  + date + ".tar";

        Path incomeFile = SOURCE_DIR.resolve(incomeName);
        Path outgoFile  = SOURCE_DIR.resolve(outgoName);

        System.out.println("Extracting GMPS " + date +"Files...");

        if (!Files.exists(incomeFile) && !Files.exists(outgoFile)) {
            System.out.println("No GMPS tar files found for date " + date);
            return;
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

