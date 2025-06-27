package com.ccb.dailyfilepipeline.function.extractor;

public interface Handler {
    void handle(DateContext context) throws Exception;
}
