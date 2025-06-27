package com.ccb.daily.file.pipeline.function.message.ingestion;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;

public class GMPSExtractor {

    private static final Path SOURCE_DIR = Paths.get("D:\\CCB\\gmps");
    private static final Path TARGET_ROOT = Paths.get("D:\\CCB\\gmps_share");

    public static void extract(String prefix, String date) throws IOException {
        Path targetDir = TARGET_ROOT.resolve(date + "MX");

        String incomeName = prefix + "income_prd_" + date + ".tar";
        String outgoName  = prefix + "outgo_prd_"  + date + ".tar";

        Path incomeFile = SOURCE_DIR.resolve(incomeName);
        Path outgoFile  = SOURCE_DIR.resolve(outgoName);

        System.out.println("Extracting " + incomeName + " to " + outgoName);

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

        System.out.println("Finished extracting GMPS files for date " + date);
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

