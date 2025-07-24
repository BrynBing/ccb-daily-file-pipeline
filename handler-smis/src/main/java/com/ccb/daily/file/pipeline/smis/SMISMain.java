/**
 * Entry point for executing the {@link com.ccb.daily.file.pipeline.smis.SMISHandler}.
 * Created on 2025-07-23.
 * <p>
 * This class extends {@link com.ccb.daily.file.pipeline.launcher.Launcher}
 * and provides the specific {@link com.ccb.daily.file.pipeline.core.Handler} implementation
 * for SMIS processing.
 * </p>
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.4
 * @since 1.4
 */

package com.ccb.daily.file.pipeline.smis;

import com.ccb.daily.file.pipeline.core.Handler;
import com.ccb.daily.file.pipeline.launcher.Launcher;

public class SMISMain extends Launcher {
    public static void main(String[] args) throws Exception {
        new SMISMain().launch(args);
    }

    @Override
    protected Handler createHandler(String sourceDir, String targetDir) {
        return new SMISHandler(sourceDir, targetDir);
    }
}
