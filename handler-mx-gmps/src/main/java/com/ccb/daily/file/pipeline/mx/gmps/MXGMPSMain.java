/**
 * Entry point for executing the {@link com.ccb.daily.file.pipeline.mx.gmps.MXGMPSHandler}.
 * Created on 2025-07-07.
 * <p>
 * This handler performs the extraction of GMPS MX message tar files into a date-based
 * destination directory. Introduced in version 1.1 as part of the modular refactor.
 * </p>
 * <p>
 * v1.2 update: Modified CLI arguments to require source and destination paths;
 * report date remains optional with default to today/yesterday.
 * </p>
 * <p>
 * v1.3 update: This class extends {@link com.ccb.daily.file.pipeline.launcher.Launcher}
 * and provides the specific {@link com.ccb.daily.file.pipeline.core.Handler} implementation
 * for MX GMPS processing.
 * </p>
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.3
 * @since 1.1
 */

package com.ccb.daily.file.pipeline.mx.gmps;

import com.ccb.daily.file.pipeline.launcher.Launcher;
import com.ccb.daily.file.pipeline.core.Handler;

public class MXGMPSMain extends Launcher {
    public static void main(String[] args) throws Exception {
        new MXGMPSMain().launch(args);
    }

    @Override
    protected Handler createHandler(String sourceDir, String targetDir) {
        return new MXGMPSHandler(sourceDir, targetDir);
    }
}
