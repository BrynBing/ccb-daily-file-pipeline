package com.ccb.daily.file.pipeline;

public class MTGMPSMain extends Launcher {
    public static void main(String[] args) throws Exception {
        new MTGMPSMain().launch(args);
    }

    @Override
    protected Handler createHandler(String sourceDir, String targetDir) {
        return new MTGMPSHandler(sourceDir, targetDir);
    }
}
