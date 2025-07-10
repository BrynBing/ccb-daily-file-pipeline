/**
 * Handles the extraction of GMPS MX messages.
 * Created on 2025-06-27.
 * <p>
 * This class performs the extraction of GMPS MX message files from a predefined input
 * location and outputs the results to a target directory named using the current date
 * with an "MX" suffix. For example, {@code G:/gmps_share/20250703MX}.
 * </p>
 * <p>
 * v1.2 update: Constructor modified to accept source and destination directories as arguments,
 * removing hardcoded paths for better flexibility.
 * </p>
 *
 * @author Bryn Zhou (Bing Zhou)
 * @version 1.2
 * @since 1.0
 */

package com.ccb.daily.file.pipeline;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MXMessageHandler implements Handler {
    private final Path sourcePath;
    private final Path targetRoot;

    public MXMessageHandler(String sourceDir, String targetDir) {
        this.sourcePath = Paths.get(sourceDir);
        this.targetRoot = Paths.get(targetDir);
    }

    @Override
    public void handle(ReportDateContext context) throws Exception {
        String siradt = context.siradt;

        Path targetPath = targetRoot.resolve(siradt + "MX");


//        GMPSExtractor.extract("mxt_", siradt, targetDir);
//
//        System.out.println("--> Completed Extracting GMPS Messages for date " + siradt);
        if (GMPSExtractor.extract("mxt_", siradt, sourcePath, targetPath)) {
            System.out.println("--> Completed Extracting GMPS MX Messages for date " + siradt);
        }
    }
}
