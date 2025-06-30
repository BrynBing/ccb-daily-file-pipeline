package com.ccb.daily.file.pipeline.function.message.ingestion;

public class ReportDateContext {
    public final String siradt;
    public final String tmdt;

    public ReportDateContext(String siradt, String tmdt) {
        this.siradt = siradt;
        this.tmdt = tmdt;
    }
}

