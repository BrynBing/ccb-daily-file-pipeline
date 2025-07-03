package com.ccb.daily.file.pipeline.message.ingestion;

public interface Handler {
    void handle(ReportDateContext context) throws Exception;
}
