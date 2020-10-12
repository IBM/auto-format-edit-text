package com.ibm.autoformatedittext.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HideFormatter {
    private final static String RANGE_INDICATOR = "-";
    private final static String REGEX = "\\[[0-9]+(%s[0-9]+)?]";
    private final static String PATTERN_STR = String.format(REGEX, RANGE_INDICATOR);

    public static String format(String s, String hideModeFormat) {
        if (s == null || hideModeFormat == null) {
            return s;
        }

        Pattern pattern = Pattern.compile(PATTERN_STR);
        Matcher m;

        do {
            m = pattern.matcher(hideModeFormat);
            if (m.find()) {
                String matchingSection = m.group();
                String replacement = getReplacement(s, matchingSection);
                hideModeFormat = hideModeFormat.replaceFirst(PATTERN_STR, replacement);
            }
        }while(m.find());

        return hideModeFormat;
    }

    private static String getReplacement(String s, String matchingSection) {
        int rangeIndicatorIndex = matchingSection.indexOf(RANGE_INDICATOR);

        if (rangeIndicatorIndex < 0) {
            String digitStr = matchingSection.substring(1, matchingSection.length() - 1);
            int index = Integer.parseInt(digitStr);

            return index < s.length() ? s.substring(index, index + 1) : "";
        }

        //Assuming index > 0 since pattern included RANGE_INDICATOR
        String startStr = matchingSection.substring(1, rangeIndicatorIndex);
        String endStr = matchingSection.substring(rangeIndicatorIndex + 1, matchingSection.length() - 1);

        //Assuming startStr/endStr can be parsed as int, since pattern specified only digits 0-9
        int startIndex = Integer.parseInt(startStr);
        int endIndex = Integer.parseInt(endStr);

        if (startIndex < 0 || endIndex >= s.length() || startIndex > endIndex) {
            return "";
        }

        //TODO: Can't we just use substring directly? Ex: s.substring(startIndex, endIndex + 1)
        StringBuilder replacementBuilder = new StringBuilder();
        for (int i = startIndex; i <= endIndex; i++) {
            replacementBuilder.append(s.substring(i, i + 1));
        }

        return replacementBuilder.toString();
    }
}