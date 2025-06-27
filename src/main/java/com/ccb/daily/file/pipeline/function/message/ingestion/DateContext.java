package com.ccb.daily.file.pipeline.function.message.ingestion;

public class DateContext {
    public final String siradt;
    public final String tmdt;

    public DateContext(String siradt, String tmdt) {
        this.siradt = siradt;
        this.tmdt = tmdt;
    }
}

