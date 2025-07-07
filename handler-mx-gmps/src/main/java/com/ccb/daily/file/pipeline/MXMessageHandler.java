/**
 * Handles the extraction of MX messages.
 * <p>
 * This class performs the extraction of MX message files from a predefined input
 * location and outputs the results to a target directory named using the current date
 * with an "MX" suffix. For example, {@code G:/gmps_share/20250703MX}.
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.0
 * @since 2025-06-27
 */

package com.ccb.daily.file.pipeline;

import com.ccb.daily.file.pipeline.GMPSExtractor;
import com.ccb.daily.file.pipeline.Handler;
import com.ccb.daily.file.pipeline.ReportDateContext;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MXMessageHandler implements Handler {
//    private static final Path TARGET_ROOT = Paths.get("D:\\CCB\\gmps_share"); // /home/ap/bde/data/gmps_share
        private static final Path TARGET_ROOT = Paths.get("\\\\ccbausydfs02\\shared\\Common\\IT_common\\gmps_share");

    @Override
    public void handle(ReportDateContext context) throws Exception {
        String siradt = context.siradt;
        Path targetDir = TARGET_ROOT.resolve(siradt + "MX");

//        GMPSExtractor.extract("mxt_", siradt, targetDir);
//
//        System.out.println("--> Completed Extracting GMPS Messages for date " + siradt);
        if (GMPSExtractor.extract("mxt_", siradt, targetDir)) {
            System.out.println("--> Completed Extracting GMPS MX Messages for date " + siradt);
        }
    }
}
