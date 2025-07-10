/**
 * Main entry point for the daily report file processing pipeline.
 * Created on 2025-06-27.
 * <p>
 * This class initializes the {@link com.ccb.daily.file.pipeline.ReportDateContext}
 * based on provided command-line arguments and triggers the execution of all registered
 * {@link com.ccb.daily.file.pipeline.Handler} components via the
 * {@link com.ccb.daily.file.pipeline.ReportProcessingClient}.
 * </p>
 *
 * <p><strong>Usage:</strong>
 * <pre>
 *     java -jar ccb-daily-file-pipeline-1.0-SNAPSHOT.jar SIRA_DATE TM_DATE
 * </pre>
 * </p>
 * <p>
 * If no arguments are provided, the current date is used as {@code SIRA_DATE}
 * and the previous day as {@code TM_DATE}.
 * </p>
 * <p>
 * Additional handlers can be added or removed via {@code client.addHandler()} or
 * {@code client.removeHandler()} before calling {@code client.process()}.
 * </p>
 * <p><b>Change History:</b></p>
 * <ul>
 *   <li><b>v1.0</b> - Initial implementation as part of unified report processing pipeline</li>
 *   <li><b>v1.1</b> - Refactored into a standalone handler module with independent execution entry</li>
 * </ul>
 * <p>
 * In version 1.1, this is retained for local testing only and is no longer used
 * in production where handlers are executed independently.
 * </p>
 * <p>
 * v1.2 update: Modified CLI arguments to require source and destination paths;
 * report date remains optional with default to today/yesterday.
 * </p>
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.2
 * @since 1.0
 */

package com.ccb.daily.file.pipeline;

public class App {
    public static void main(String[] args) {
//        Reports
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
        ReportDateContext context = new ReportDateContext(siradt, tmdt);
        ReportProcessingClient client = new ReportProcessingClient(context);
        /*
         * --- Handler Registration Guide ---
         * To include or exclude report handlers, simply comment or uncomment the following lines:
         * - To include a handler:   client.addHandler(new YourHandler());
         * - To exclude a handler:   comment out the corresponding addHandler(...) line
         *
         * These handlers will be invoked during client.process().
         *
         * By default, the following handlers are registered:
         * - MXMessageHandler
         * - EFSFileHandler
         *
         * If you don't need a handler, you can disable it by commenting out its registration line.
         */

        client.addHandler(new MXMessageHandler(sourceDir, targetDir));
        client.addHandler(new EFSFileHandler(sourceDir, targetDir));

        client.process();


    }
}
