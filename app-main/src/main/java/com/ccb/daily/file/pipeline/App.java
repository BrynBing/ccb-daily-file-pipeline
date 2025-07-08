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
 *   <li><b>v1.0</b> – Initial implementation as part of unified report processing pipeline</li>
 *   <li><b>v1.1</b> – Refactored into a standalone handler module with independent execution entry</li>
 * </ul>
 * <p>
 * In version 1.1, this is retained for local testing only and is no longer used
 * in production where handlers are executed independently.
 * </p>
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.1
 * @since 1.0
 */

package com.ccb.daily.file.pipeline;

public class App {
    public static void main(String[] args) {
//        Reports
        String siradt;
        String tmdt;

        if (args.length == 0) {
            siradt = DateUtil.today();
            tmdt = DateUtil.yesterday();
        } else if (args.length == 2) {
            siradt = args[0];
            tmdt = args[1];
        } else {
            System.out.println("Usage: java -jar program.jar SIRA_DATE<yyyymmdd> TM_DATE<yyyymmdd>");
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

        client.addHandler(new MXMessageHandler());
        client.addHandler(new EFSFileHandler());

        client.process();


    }
}
