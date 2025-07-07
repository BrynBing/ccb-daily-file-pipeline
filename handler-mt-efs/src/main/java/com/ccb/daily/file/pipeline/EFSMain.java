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