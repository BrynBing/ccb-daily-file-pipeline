package com.ccb.daily.file.pipeline.function.message.ingestion.MXmessage;

import com.ccb.daily.file.pipeline.function.message.ingestion.ReportDateContext;
import com.ccb.daily.file.pipeline.function.message.ingestion.GMPSExtractor;
import com.ccb.daily.file.pipeline.function.message.ingestion.Handler;

public class MXMessageHandler implements Handler {
    @Override
    public void handle(ReportDateContext context) throws Exception {
        String siradt = context.siradt;

        GMPSExtractor.extract("mxt_", siradt);
    }
}
