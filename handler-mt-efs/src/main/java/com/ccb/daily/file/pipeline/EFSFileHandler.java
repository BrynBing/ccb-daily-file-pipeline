/**
 * Handler responsible for fetching EFS Excel files for a given processing date.
 * Created on 2025-06-27.
 * <p>
 * This class reads a list of expected EFS filenames from a predefined text file,
 * locates each corresponding file under a date-based source directory, and copies
 * them into a destination folder named after the same date.
 * </p>
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.1
 * @since 1.0
 */

package com.ccb.daily.file.pipeline;

import com.ccb.daily.file.pipeline.Handler;
import com.ccb.daily.file.pipeline.ReportDateContext;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class EFSFileHandler implements Handler {
//    private static final Path SOURCE_DIR_ROOT = Paths.get("D:\\CCB\\P10\\000000000"); // /home/ap/bde/data/P10/000000000
    private static final Path SOURCE_DIR_ROOT = Paths.get("\\\\ccbausydnas02\\data\\P10\\000000000");
//    private static final Path DEST_DIR_ROOT = Paths.get("D:\\CCB\\EFS"); // /home/ap/bde/data/EFS
    private static final Path DEST_DIR_ROOT = Paths.get("\\\\ccbausydfs02\\shared\\Common\\IT_common\\EFS");

//    private static final Path FILE_LIST = Paths.get("D:\\CCB\\sbin\\EFSFileList.txt"); // /home/ap/bde/sbin/EFSFileList.txt
//    private static final Path FILE_LIST = Paths.get("C:\\Test\\EFSFileList.txt");

    @Override
    public void handle(ReportDateContext context) throws Exception {
        String tmdt = context.tmdt;
        Path sourceDir = SOURCE_DIR_ROOT.resolve(tmdt);

        System.out.println("Fetching EFS " + tmdt + " Files ......");

        if (!Files.isDirectory(sourceDir)) {
            System.out.println("No EFS directory found for date: " + tmdt);
            return;
        }

        Path destDir = DEST_DIR_ROOT.resolve(tmdt);
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
