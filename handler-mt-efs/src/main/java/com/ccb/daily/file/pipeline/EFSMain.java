/**
 * Entry point for executing the {@link com.ccb.daily.file.pipeline.EFSFileHandler}.
 * Created on 2025-07-07.
 * <p>
 * This program handles the movement of EFS MT Excel files from a source directory to a
 * destination folder based on the specified report date.
 * Introduced in version 1.1 to support modular and permission-isolated execution.
 * </p>
 * <p>
 * v1.2 update: Modified CLI arguments to require source and destination paths;
 * report date remains optional with default to today/yesterday.
 * </p>
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.3
 * @since 1.1
 */

package com.ccb.daily.file.pipeline;

public class EFSMain extends Launcher {
    public static void main(String[] args) throws Exception {
        new EFSMain().launch(args);
    }

    @Override
    protected Handler createHandler(String sourceDir, String targetDir) {
        return new EFSFileHandler(sourceDir, targetDir);
    }
}