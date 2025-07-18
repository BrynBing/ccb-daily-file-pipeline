package com.ccb.daily.file.pipeline;

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
