/**
 * Entry point for executing the {@link com.ccb.daily.file.pipeline.mt.gmps.MTGMPSHandler}.
 * Created on 2025-07-18.
 * <p>
 * This class extends {@link com.ccb.daily.file.pipeline.launcher.Launcher}
 * and provides the specific {@link com.ccb.daily.file.pipeline.core.Handler} implementation
 * for MT GMPS processing.
 * </p>
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.3
 * @since 1.3
 */

package com.ccb.daily.file.pipeline.mt.gmps;

import com.ccb.daily.file.pipeline.launcher.Launcher;
import com.ccb.daily.file.pipeline.core.Handler;

public class MTGMPSMain extends Launcher {
    public static void main(String[] args) throws Exception {
        new MTGMPSMain().launch(args);
    }

    @Override
    protected Handler createHandler(String sourceDir, String targetDir) {
        return new MTGMPSHandler(sourceDir, targetDir);
    }
}
