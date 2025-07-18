/**
 * Launcher provides a template for launching handler-based processes.
 * Created on 2025-07-18.
 * <p>
 * This class encapsulates common logic for:
 * <ul>
 *   <li>Parsing command-line arguments</li>
 *   <li>Initializing report date context</li>
 *   <li>Delegating execution to a concrete {@link com.ccb.daily.file.pipeline.core.Handler}
 *   implementation</li>
 * </ul>
 * Subclasses should implement {@link #createHandler(String, String)} to specify which
 * handler to execute for a given module.
 * </p>
 * <p>
 * Supported argument formats:
 * <ul>
 *   <li>{@code <Source_Dir> <Target_Dir>} (uses today's and yesterday's date by default)</li>
 *   <li>{@code <Source_Dir> <Target_Dir> <SIRA_DATE: yyyymmdd> <TM_DATE: yyyymmdd>}</li>
 * </ul>
 * </p>
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.3
 * @since 1.3
 */

package com.ccb.daily.file.pipeline.launcher;

import com.ccb.daily.file.pipeline.core.DateUtil;
import com.ccb.daily.file.pipeline.core.Handler;
import com.ccb.daily.file.pipeline.core.ReportDateContext;

public abstract class Launcher {
    public void launch(String[] args) throws Exception {
        String siradt;
        String tmdt;

        String sourceDir;
        String targetDir;

        if (args.length == 2) {
            siradt = DateUtil.today();
            tmdt = DateUtil.yesterday();
            sourceDir = args[0];
            targetDir = args[1];
        } else if (args.length == 4) {
            siradt = args[2];
            tmdt = args[3];
            sourceDir = args[0];
            targetDir = args[1];
        } else {
            System.out.println("Usage:");
            System.out.println("  java -jar program.jar <Source_Dir> <Target_Dir>");
            System.out.println("  java -jar program.jar <SIRA_DATE: yyyymmdd> <TM_DATE: yyyymmdd> <sourceDir> <destDir>");
            System.out.println();
            System.out.println("  If no dates are provided, today's and yesterday's date will be used by default.");
            System.out.println("  Source_Dir and Target_Dir are required.");

            return;
        }

        Handler handler = createHandler(sourceDir, targetDir);
        ReportDateContext context = new ReportDateContext(siradt, tmdt);

        handler.handle(context);
    }

    protected abstract Handler createHandler(String sourceDir, String targetDir);
}
