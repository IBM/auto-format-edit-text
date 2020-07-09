package com.ibm.autoformatedittext.inputmask;

@SuppressWarnings("WeakerAccess")
public class InputMask {
    private static final char DEFAULT_PLACEHOLDER = '#';

    private char placeholder;
    private String inputMaskString;
    private Integer unformattedLength;

    public InputMask(String inputMaskString, String placeholder) {
        this.inputMaskString = inputMaskString;
        setPlaceholder(placeholder);
    }

    //Returns true if the character in the format at the specified index is the placeholder character
    public boolean isPlaceholder(int index) {
        return index < inputMaskString.length() && inputMaskString.charAt(index) == placeholder;
    }

    //Returns true if the specified string matches the format
    public boolean matches(String formattedString) {
        if (inputMaskString.length() != formattedString.length()) {
            return false;
        }

        for (int i = 0; i < inputMaskString.length(); i++) {
            char currentChar = inputMaskString.charAt(i);
            if (currentChar != placeholder && currentChar != formattedString.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    public int getUnformattedLength() {
        if (unformattedLength == null) {
            unformattedLength = 0;

            for (int i = 0; i < inputMaskString.length(); i++) {
                if (inputMaskString.charAt(i) == placeholder) {
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
        for (int i = 0; i < inputMaskString.length(); i++) {
            if (inputMaskString.charAt(i) == placeholder) {
                if (unformattedTextPosition == unformattedText.length()) {
                    break;
                }

                builder.append(unformattedText.charAt(unformattedTextPosition));
                unformattedTextPosition++;
            }else {
                builder.append(inputMaskString.charAt(i));
            }
        }

        return builder.toString();
    }

    public String unformatText(CharSequence formattedText, int start, int end) {
        StringBuilder builder = new StringBuilder();

        if (formattedText.length() > 0) {
            for (int i = start; i < end; i++) {
                if (inputMaskString.charAt(i) == placeholder) {
                    builder.append(formattedText.charAt(i));
                }
            }
        }

        return builder.toString();
    }

    public String getInputMaskString() {
        return inputMaskString;
    }

    public void setInputMaskString(String inputMaskString) {
        this.inputMaskString = inputMaskString;
        this.unformattedLength = null;
    }

    public char getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholderString) {
        boolean placeholderValid = placeholderString != null && placeholderString.length() == 1;
        this.placeholder = placeholderValid ? placeholderString.charAt(0) : DEFAULT_PLACEHOLDER;
    }
}