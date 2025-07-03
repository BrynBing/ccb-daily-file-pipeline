/**
 * Handler responsible for fetching EFS Excel files for a given processing date.
 * <p>
 * This class reads a list of expected EFS filenames from a predefined text file,
 * locates each corresponding file under a date-based source directory, and copies
 * them into a destination folder named after the same date.
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.0
 * @since 2025-06-27
 */

package com.ccb.daily.file.pipeline.message.ingestion.MTfile;

import com.ccb.daily.file.pipeline.message.ingestion.Handler;
import com.ccb.daily.file.pipeline.message.ingestion.ReportDateContext;

import java.io.BufferedReader;
import java.nio.file.*;

public class EFSFileHandler implements Handler {
//    private static final Path SOURCE_DIR_ROOT = Paths.get("D:\\CCB\\P10\\000000000"); // /home/ap/bde/data/P10/000000000
    private static final Path SOURCE_DIR_ROOT = Paths.get("C:\\Test\\P10\\000000000");
//    private static final Path DEST_DIR_ROOT = Paths.get("D:\\CCB\\EFS"); // /home/ap/bde/data/EFS
    private static final Path DEST_DIR_ROOT = Paths.get("C:\\Test\\EFS");
//    private static final Path FILE_LIST = Paths.get("D:\\CCB\\sbin\\EFSFileList.txt"); // /home/ap/bde/sbin/EFSFileList.txt
    private static final Path FILE_LIST = Paths.get("C:\\Test\\EFSFileList.txt");
    @Override
    public void handle(ReportDateContext context) throws Exception {
        String tmdt = context.tmdt;
        Path sourceDir = SOURCE_DIR_ROOT.resolve(tmdt);
        if (!Files.isDirectory(sourceDir)) {
            System.out.println("No directory found for date: " + tmdt);
            return;
        }

        System.out.println("Fetching EFS " + tmdt + " Files ......");

        Path destDir = DEST_DIR_ROOT.resolve(tmdt);
        Files.createDirectories(destDir);

        try (BufferedReader reader = Files.newBufferedReader(FILE_LIST)) {
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

        System.out.println("--> Completed Fetching EFS Files");
//        System.out.println(LocalDateTime.now());
    }
}
