import com.ccb.daily.file.pipeline.GMPSExtractor;
import com.ccb.daily.file.pipeline.Handler;
import com.ccb.daily.file.pipeline.ReportDateContext;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MTGMPSHandler implements Handler {
    private final Path sourcePath;
    private final Path targetRoot;

    public MTGMPSHandler(String sourceDir, String targetDir) {
        this.sourcePath = Paths.get(sourceDir);
        this.targetRoot = Paths.get(targetDir);
    }
    @Override
    public void handle(ReportDateContext context) throws Exception {
        String siradt = context.siradt;

        Path targetPath = targetRoot.resolve(siradt);

        if (GMPSExtractor.extract("", siradt, sourcePath, targetPath)) {
            System.out.println("--> Completed Extracting GMPS MX Messages for date " + siradt);
        }
    }
}
