/**
 * Entry point for executing the {@link com.ccb.daily.file.pipeline.efs.EFSFileHandler}.
 * Created on 2025-07-07.
 * <p>
 * This program handles the movement of EFS reports from a source directory to a
 * destination folder based on the specified report date.
 * Introduced in version 1.1 to support modular and permission-isolated execution.
 * </p>
 * <p>
 * v1.2 update: Modified CLI arguments to require source and destination paths;
 * report date remains optional with default to today/yesterday.
 * </p>
 * <p>
 * v1.3 update: This class extends {@link com.ccb.daily.file.pipeline.launcher.Launcher}
 * and provides the specific {@link com.ccb.daily.file.pipeline.core.Handler} implementation
 * for EFS reports processing.
 * </p>
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.3
 * @since 1.1
 */

package com.ccb.daily.file.pipeline.efs;

import com.ccb.daily.file.pipeline.core.Handler;
import com.ccb.daily.file.pipeline.launcher.Launcher;

public class EFSMain extends Launcher {
    public static void main(String[] args) throws Exception {
        new EFSMain().launch(args);
    }

    @Override
    protected Handler createHandler(String sourceDir, String targetDir) {
        return new EFSFileHandler(sourceDir, targetDir);
    }
}