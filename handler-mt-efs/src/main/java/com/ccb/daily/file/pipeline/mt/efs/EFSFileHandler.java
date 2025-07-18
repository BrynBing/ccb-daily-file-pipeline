/**
 * Handler responsible for fetching EFS Excel files for a given processing date.
 * Created on 2025-06-27.
 * <p>
 * This class reads a list of expected EFS filenames from a predefined text file,
 * locates each corresponding file under a date-based source directory, and copies
 * them into a destination folder named after the same date.
 * </p>
 * <p>
 * v1.2 update: Constructor modified to accept source and destination directories as arguments,
 * removing hardcoded paths for better flexibility.
 * </p>
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.2
 * @since 1.0
 */

package com.ccb.daily.file.pipeline.mt.efs;

import com.ccb.daily.file.pipeline.core.Handler;
import com.ccb.daily.file.pipeline.core.ReportDateContext;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class EFSFileHandler implements Handler {
    private final Path sourcePath;
    private final Path targetPath;

    public EFSFileHandler(String sourceDir, String targetDir) {
        this.sourcePath = Paths.get(sourceDir);
        this.targetPath = Paths.get(targetDir);
    }

    @Override
    public void handle(ReportDateContext context) throws Exception {
        String tmdt = context.tmdt;
        Path sourceDir = sourcePath.resolve(tmdt);

        System.out.println("Fetching EFS " + tmdt + " Files ......");

        if (!Files.isDirectory(sourceDir)) {
            System.out.println("No EFS directory found for date: " + tmdt);
            return;
        }

        Path destDir = targetPath.resolve(tmdt);
        Files.createDirectories(destDir);

        try (BufferedReader reader = getFileListReader()) {
            String x;
            while ((x = reader.readLine()) != null) {
                String filename = x + "_" + tmdt + ".xlsx";
                Path sourceFile = sourceDir.resolve(filename);
                Path destFile = destDir.resolve(x + ".xlsx");

                if (Files.exists(sourceFile)) {
                    Files.copy(sourceFile, destFile, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }

        System.out.println("--> Completed Fetching EFS MT Files");
    }

    private static BufferedReader getFileListReader() throws IOException {
        InputStream is = EFSFileHandler.class.getClassLoader().getResourceAsStream("EFSFileList.txt");
        if (is == null) {
            throw new FileNotFoundException("EFSFileList.txt not found in resources");
        }
        return new BufferedReader(new InputStreamReader(is));
    }

}
