package com.ccb.daily.file.pipeline.function.message.ingestion;

public interface Handler {
    void handle(ReportDateContext context) throws Exception;
}
