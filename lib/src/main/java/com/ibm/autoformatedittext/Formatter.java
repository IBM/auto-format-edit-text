package com.ibm.autoformatedittext;

@SuppressWarnings("WeakerAccess")
public class Formatter {
    private char placeholder;
    private String format;
    private Integer unformattedLength;

    public Formatter(String format, char placeholder) {
        this.format = format;
        this.placeholder = placeholder;
    }

    //Returns true if the character in the format at the specified index is the placeholder character
    public boolean isPlaceholder(int index) {
        return index < format.length() && format.charAt(index) == placeholder;
    }

    //Returns true if the specified string matches the format
    public boolean matches(String formattedString) {
        if (format.length() != formattedString.length()) {
            return false;
        }

        for (int i = 0; i < format.length(); i++) {
            char currentChar = format.charAt(i);
            if (currentChar != placeholder && currentChar != formattedString.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    public int getUnformattedLength() {
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

    public String formatText(String unformattedText) {
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

    public String unformatText(CharSequence formattedText, int start, int end) {
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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
        this.unformattedLength = null;
    }
}