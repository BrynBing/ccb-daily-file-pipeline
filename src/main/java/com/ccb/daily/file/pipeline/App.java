package com.ccb.daily.file.pipeline;

import com.ccb.daily.file.pipeline.message.ingestion.ReportDateContext;
import com.ccb.daily.file.pipeline.message.ingestion.ReportProcessingClient;
import com.ccb.daily.file.pipeline.utils.DateUtil;

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
         * To add or remove report handlers, uncomment and modify the following lines:
         * - To add a handler:      client.addHandler(...)
         * - To remove a handler:   client.removeHandler(...)
         * These handlers will be invoked during client.process().
         *
         * By default, the following handlers are registered:
         * - MXMessageHandler
         * - EFSFileHandler
         */
//        client.addHandler();
//        client.removeHandler();

        client.process();


    }
}
