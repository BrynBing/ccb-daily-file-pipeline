package com.ccb.daily.file.pipeline.mt.gmps;

import com.ccb.daily.file.pipeline.core.GMPSExtractor;
import com.ccb.daily.file.pipeline.core.Handler;
import com.ccb.daily.file.pipeline.core.ReportDateContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

public class MTGMPSHandler implements Handler {
    private final Path sourcePath;
    private final Path targetRoot;
    private final Path tempDir;

    public MTGMPSHandler(String sourceDir, String targetDir) {
        this.sourcePath = Paths.get(sourceDir);
        this.targetRoot = Paths.get(targetDir);

        this.tempDir = Paths.get(System.getProperty("java.io.tmpdir")).resolve("gmps-temp");
    }
    @Override
    public void handle(ReportDateContext context) throws Exception {
        // Extract tar
        String siradt = context.siradt;
        Path targetPath = targetRoot.resolve(siradt);
//        if (GMPSExtractor.extract("", siradt, sourcePath, targetPath)) {
        if (GMPSExtractor.extract("", siradt, sourcePath, tempDir)) {
            System.out.println("--> Completed Extracting tar files for date " + siradt);

            // Decode
            try (Stream<Path> files = Files.walk(tempDir)) {
                files.filter(Files::isRegularFile).forEach(file -> {
                    try {
//                        Path relativePath = tempDir.relativize(file);
//                        boolean isOutgo = relativePath.getNameCount() > 1;
//
//                        String fileName = file.getFileName().toString();
//                        if (isOutgo) {
//                            fileName = fileName + ".OUT";
//                        }
//
//                        Path outputFile = targetPath.resolve(fileName);


                        String fileName = file.getFileName().toString();
                        fileName = "new" + fileName;
                        Path relativePath = tempDir.relativize(file);
                        Path outputFile = targetPath.resolve(fileName);

                        if (Files.exists(outputFile)) {
                            deleteDirectory(outputFile);
                        }
                        Files.createDirectories(outputFile.getParent());

                        String decoded = DecodeUtil.decode(getFileContent(file));
                        String replaced = PlaceholderReplacer.replaceSNAndISN(decoded);
                        Files.write(outputFile, replaced.getBytes("ISO-8859-2"));

                        System.out.println("Decoded: " + relativePath);
                    } catch (Exception e) {
                        System.err.println("Failed to decode: " + file);
                        e.printStackTrace();
                    }
                });
            }

            deleteDirectory(tempDir);
            System.out.println("Temp directory cleaned up.");
        }
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

    private String getFileContent(Path file) throws IOException {
        return new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
    }
}
