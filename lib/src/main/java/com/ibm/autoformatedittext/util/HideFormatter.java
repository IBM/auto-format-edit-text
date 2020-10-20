package com.ibm.autoformatedittext.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HideFormatter {
    private static final String RANGE_INDICATOR = "-";
    private static final String REGEX = "\\[[0-9]+(%s[0-9]+)?\\]";
    private static final String PATTERN_STR = String.format(REGEX, RANGE_INDICATOR);
    private static final Pattern INDEX_PLACEHOLDER_PATTERN = Pattern.compile(PATTERN_STR);

    public static String format(String s, String hideModeFormat) {
        if (hideModeFormat == null) {
            return null;
        }

        if (s == null) {
            return hideModeFormat;
        }

        String formattedText = hideModeFormat;
        Matcher m;

        do {
            m = INDEX_PLACEHOLDER_PATTERN.matcher(formattedText);
            if (m.find()) {
                String matchingSection = m.group();
                String replacement = getReplacement(s, matchingSection);
                formattedText = formattedText.replaceFirst(PATTERN_STR, replacement);
            }
        }while(m.find());

        return formattedText;
    }

    private static String getReplacement(String s, String matchingSection) {
        int rangeIndicatorIndex = matchingSection.indexOf(RANGE_INDICATOR);

        if (rangeIndicatorIndex < 0) {
            String digitStr = matchingSection.substring(1, matchingSection.length() - 1);
            int index = Integer.parseInt(digitStr);
            return index < s.length() ? s.substring(index, index + 1) : "";
        }

        String startStr = matchingSection.substring(1, rangeIndicatorIndex);
        String endStr = matchingSection.substring(rangeIndicatorIndex + 1, matchingSection.length() - 1);

        int startIndex = Integer.parseInt(startStr);
        int endIndex = Integer.parseInt(endStr);

        if (startIndex < 0 || endIndex >= s.length() || startIndex > endIndex) {
            return "";
        }

        return s.substring(startIndex, endIndex + 1);
    }
}