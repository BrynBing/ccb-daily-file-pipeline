/**
 * Holds the report processing date parameters to be used by handlers.
 * <p>
 * This context is passed to each handler during the processing pipeline to ensure
 * consistent handling of date-specific logic.
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.0
 * @since 2025-06-27
 */

package com.ccb.daily.file.pipeline;

public class ReportDateContext {
    public final String siradt;
    public final String tmdt;

    public ReportDateContext(String siradt, String tmdt) {
        this.siradt = siradt;
        this.tmdt = tmdt;
    }
}

