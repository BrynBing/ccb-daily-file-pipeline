/**
 * Entry point for executing the {@link com.ccb.daily.file.pipeline.EFSFileHandler}.
 * Created on 2025-07-07.
 * <p>
 * This program handles the movement of EFS MT Excel files from a source directory to a
 * destination folder based on the specified report date.
 * Introduced in version 1.1 to support modular and permission-isolated execution.
 * </p>
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.1
 * @since 1.1
 */

package com.ccb.daily.file.pipeline;

public class EFSMain {
    public static void main(String[] args) throws Exception {
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

        EFSFileHandler handler = new EFSFileHandler();
        ReportDateContext context = new ReportDateContext(siradt, tmdt);

        handler.handle(context);

    }
}