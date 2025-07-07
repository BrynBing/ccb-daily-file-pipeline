/**
 * Entry point for executing the {@link com.ccb.daily.file.pipeline.MXMessageHandler}.
 * Created on 2025-07-07.
 * <p>
 * This handler performs the extraction of GMPS MX message tar files into a date-based
 * destination directory. Introduced in version 1.1 as part of the modular refactor.
 * </p>
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.1
 * @since 1.1
 */

package com.ccb.daily.file.pipeline;

public class MXGMPSMain {
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

        MXMessageHandler handler = new MXMessageHandler();
        ReportDateContext context = new ReportDateContext(siradt, tmdt);

        handler.handle(context);
    }
}
