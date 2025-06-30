package com.ccb.daily.file.pipeline.function.message.ingestion;

import com.ccb.daily.file.pipeline.function.message.ingestion.MXmessage.MXMessageHandler;

import java.util.ArrayList;
import java.util.List;

public class ReportProcessingClient {
    private static ReportProcessingClient instance;

    private final ReportDateContext context;
    private final List<Handler> handlers = new ArrayList<>();

    private ReportProcessingClient(ReportDateContext context) {
        this.context = context;
        registerDefaultHandlers();
    }

    public static ReportProcessingClient getInstance(ReportDateContext context) {
        if (instance == null) {
            instance = new ReportProcessingClient(context);
        }
        return instance;
    }

    private void registerDefaultHandlers() {
        addHandler(new MXMessageHandler());
//        addHandler(new TransferHandler());
    }

    public void addHandler(Handler handler) {
        handlers.add(handler);
    }

    public void removeHandler(Handler handler) {
        handlers.remove(handler);
    }

    public void process() {
        for (Handler handler : handlers) {
            try {
                handler.handle(context);
            } catch (Exception e) {
                System.err.println("Report handler failed: " + handler.getClass().getSimpleName());
                e.printStackTrace();
            }
        }
    }
}
