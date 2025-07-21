/**
 * Utility class for generating formatted date strings in {@code yyyyMMdd} format.
 * <p>
 * Provides helper methods for obtaining today's date, yesterday's date,
 * and dates offset by a specified number of days.
 * </p>
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.0
 * @since 1.0
 */

package com.ccb.daily.file.pipeline.core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static String today() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static String yesterday() {
        return LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static String daysAgo(int days) {
        return LocalDate.now().minusDays(days).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}

