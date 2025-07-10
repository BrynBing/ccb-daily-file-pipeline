/**
 * Central coordinator for executing report processing handlers.
 * Created on 2025-06-27.
 * <p>
 * This client manages a list of {@link com.ccb.daily.file.pipeline.Handler} implementations,
 * each responsible for a specific task such as extracting or transforming daily report files.
 * It dispatches them sequentially using a shared {@link com.ccb.daily.file.pipeline.ReportDateContext},
 * which provides consistent date input to all handlers.
 * </p>
 * <p>
 * Additional handlers can be added dynamically using {@link #addHandler(Handler)}.
 * </p>
 * <p>
 * This class is typically instantiated once per daily processing task, with a context representing
 * the target date. It then invokes {@link #process()} to execute all registered handlers in order.
 * </p>
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.0
 * @since 1.0
 * @deprecated since 1.1 - replaced by handler-specific apps
 */

package com.ccb.daily.file.pipeline;

import java.util.ArrayList;
import java.util.List;

public class ReportProcessingClient {
    private final ReportDateContext context;
    private final List<Handler> handlers = new ArrayList<>();

    public ReportProcessingClient(ReportDateContext context) {
        this.context = context;
//        registerDefaultHandlers();
    }

//    private void registerDefaultHandlers() {
//        addHandler(new MXMessageHandler());
//        addHandler(new EFSFileHandler());
//    }

    public void addHandler(Handler handler) {
        handlers.add(handler);
    }

//    public void removeHandler(Handler handler) {
//        handlers.remove(handler);
//    }

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
