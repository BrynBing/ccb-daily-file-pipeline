package com.ccb.daily.file.pipeline;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderReplacer {

    private static final Pattern PATTERN = Pattern.compile(".SN..ISN..");

    public static String replaceSNAndISN(String input) {
        Matcher matcher = PATTERN.matcher(input);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, generateRandom10Digits());
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private static String generateRandom10Digits() {
        long hash = Math.abs(UUID.randomUUID().toString().hashCode());
        String numStr = String.valueOf(hash);
        if (numStr.length() > 10) {
            return numStr.substring(0, 10);
        } else {
            return String.format("%010d", hash);
        }
    }
}
