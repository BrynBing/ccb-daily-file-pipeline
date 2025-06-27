package com.ccb.daily.file.pipeline.function.message.ingestion;

public interface Handler {
    void handle(DateContext context) throws Exception;
}
