/**
 * Entry point for executing the {@link com.ccb.daily.file.pipeline.MXMessageHandler}.
 * Created on 2025-07-07.
 * <p>
 * This handler performs the extraction of GMPS MX message tar files into a date-based
 * destination directory. Introduced in version 1.1 as part of the modular refactor.
 * </p>
 * <p>
 * v1.2 update: Modified CLI arguments to require source and destination paths;
 * report date remains optional with default to today/yesterday.
 * </p>
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.2
 * @since 1.1
 */

package com.ccb.daily.file.pipeline;

public class MXGMPSMain {
    public static void main(String[] args) throws Exception {
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

        MXMessageHandler handler = new MXMessageHandler(sourceDir, targetDir);
        ReportDateContext context = new ReportDateContext(siradt, tmdt);

        handler.handle(context);
    }
}
