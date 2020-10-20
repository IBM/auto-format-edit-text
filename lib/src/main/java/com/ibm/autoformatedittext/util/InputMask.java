package com.ibm.autoformatedittext.util;

import androidx.annotation.NonNull;

public class InputMask {
    private char placeholder;
    private String inputMaskString;
    private Integer unformattedLength;

    public InputMask(@NonNull String inputMaskString, char placeholder) {
        setInputMaskString(inputMaskString);
        this.placeholder = placeholder;
    }

    //Returns true if the character in the format at the specified index is the placeholder character
    public boolean isPlaceholder(int index) {
        return index >= 0 &&
                index < inputMaskString.length() &&
                inputMaskString.charAt(index) == placeholder;
    }

    //Returns true if the specified string matches the format
    //Returns false if there is no match
    public boolean matches(@NonNull String formattedText) {
        if (inputMaskString.length() != formattedText.length()) {
            return false;
        }

        for (int i = 0; i < inputMaskString.length(); i++) {
            char currentChar = inputMaskString.charAt(i);
            if (currentChar != placeholder && currentChar != formattedText.charAt(i)) {
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

    public String formatText(@NonNull String unformattedText) {
        StringBuilder builder = new StringBuilder();

        if (unformattedText.isEmpty()) {
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

    public String unformatText(@NonNull CharSequence formattedText, int start, int end) {
        StringBuilder builder = new StringBuilder();

        for (int i = start; i < end; i++) {
            if (inputMaskString.charAt(i) == placeholder) {
                builder.append(formattedText.charAt(i));
            }
        }

        return builder.toString();
    }

    public String getInputMaskString() {
        return inputMaskString;
    }

    public void setInputMaskString(@NonNull String inputMaskString) {
        this.inputMaskString = inputMaskString;
        this.unformattedLength = null;
    }

    public char getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(char placeholder) {
        this.placeholder = placeholder;
    }
}