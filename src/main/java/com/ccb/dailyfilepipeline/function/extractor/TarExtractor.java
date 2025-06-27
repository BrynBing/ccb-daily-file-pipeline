package com.ccb.dailyfilepipeline.function.extractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TarExtractor {
    public static void extractTarFile(Path tarFilePath, Path targetDir) throws IOException {
        if (!Files.exists(tarFilePath)) return;
        // 调用命令行 tar，或用 Apache Commons Compress 实现
        ProcessBuilder pb = new ProcessBuilder("tar", "xf", tarFilePath.toString(), "-C", targetDir.toString());
        pb.inheritIO(); // 可选，用于显示输出
        Process p = pb.start();
        try {
            if (p.waitFor() != 0) {
                throw new IOException("Failed to extract " + tarFilePath);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

