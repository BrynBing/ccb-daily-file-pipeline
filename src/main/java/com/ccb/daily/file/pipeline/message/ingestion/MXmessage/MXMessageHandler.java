package com.ccb.daily.file.pipeline.message.ingestion.MXmessage;

import com.ccb.daily.file.pipeline.message.ingestion.ReportDateContext;
import com.ccb.daily.file.pipeline.message.ingestion.GMPSExtractor;
import com.ccb.daily.file.pipeline.message.ingestion.Handler;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MXMessageHandler implements Handler {
    private static final Path TARGET_ROOT = Paths.get("D:\\CCB\\gmps_share"); // /home/ap/bde/data/gmps_share
    //    private static final Path TARGET_ROOT = Paths.get("C:\\Test\\gmps_share");

    @Override
    public void handle(ReportDateContext context) throws Exception {
        String siradt = context.siradt;
        Path targetDir = TARGET_ROOT.resolve(siradt + "MX");

        GMPSExtractor.extract("mxt_", siradt, targetDir);

        System.out.println("--> Completed Extracting GMPS Messages for date " + siradt);
    }
}
