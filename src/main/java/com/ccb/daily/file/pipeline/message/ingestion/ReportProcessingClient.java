package com.ccb.daily.file.pipeline.message.ingestion;

import com.ccb.daily.file.pipeline.message.ingestion.MTfile.EFSFileHandler;
import com.ccb.daily.file.pipeline.message.ingestion.MXmessage.MXMessageHandler;

import java.util.ArrayList;
import java.util.List;

public class ReportProcessingClient {
    private final ReportDateContext context;
    private final List<Handler> handlers = new ArrayList<>();

    public ReportProcessingClient(ReportDateContext context) {
        this.context = context;
        registerDefaultHandlers();
    }

    private void registerDefaultHandlers() {
        addHandler(new MXMessageHandler());
        addHandler(new EFSFileHandler());
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
//                e.printStackTrace();
            }
        }
    }
}
