package com.ibm.autoformatedittext;

class Formatter {
    private char placeholder;
    private String format;
    private Integer unformattedLength;

    Formatter(String format, char placeholder) {
        this.format = format;
        this.placeholder = placeholder;
    }

    boolean isPlaceholder(int index) {
        return format.charAt(index) == placeholder;
    }

    int getUnformattedLength() {
        if (unformattedLength == null) {
            unformattedLength = 0;

            for (int i = 0; i < format.length(); i++) {
                if (format.charAt(i) == placeholder) {
                    unformattedLength++;
                }
            }
        }

        return unformattedLength;
    }

    String formatText(String unformattedText) {
        StringBuilder builder = new StringBuilder();

        if (unformattedText.length() == 0) {
            return "";
        }

        int unformattedTextPosition = 0;
        for (int i = 0; i < format.length(); i++) {
            if (format.charAt(i) == placeholder) {
                if (unformattedTextPosition == unformattedText.length()) {
                    break;
                }

                builder.append(unformattedText.charAt(unformattedTextPosition));
                unformattedTextPosition++;
            }else {
                builder.append(format.charAt(i));
            }
        }

        return builder.toString();
    }

    String unformatText(CharSequence formattedText, int start, int end) {
        StringBuilder builder = new StringBuilder();

        if (formattedText.length() > 0) {
            for (int i = start; i < end; i++) {
                if (format.charAt(i) == placeholder) {
                    builder.append(formattedText.charAt(i));
                }
            }
        }

        return builder.toString();
    }

    String getFormat() {
        return format;
    }

    void setFormat(String format) {
        this.format = format;
        this.unformattedLength = null;
    }
}