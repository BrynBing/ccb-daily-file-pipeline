/**
 * Defines the contract for individual report processing components.
 * <p>
 * Each implementation of this interface performs a specific type of file processing task.
 * <p>
 * All handlers are intended to be invoked by {@link ReportProcessingClient}.
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.0
 * @since 2025-06-27
 */

package com.ccb.daily.file.pipeline.message.ingestion;

public interface Handler {

    /**
     * Executes the specific report handling logic for the given target date context.
     *
     * @param context the report date context, containing {@code siradt} and {@code tmdt}
     * @throws Exception if the handler fails during execution
     */
    void handle(ReportDateContext context) throws Exception;
}
