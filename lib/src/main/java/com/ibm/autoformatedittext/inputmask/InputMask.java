package com.ibm.autoformatedittext.inputmask;

public class InputMask {
    private char placeholder;
    private String inputMaskString;
    private Integer unformattedLength;

    public InputMask(String inputMaskString, char placeholder) {
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
    public boolean matches(String formattedText) {
        if (formattedText == null) {
            throw new IllegalArgumentException("Formatted text argument cannot be null.");
        }

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

    public String formatText(String unformattedText) {
        StringBuilder builder = new StringBuilder();

        if (unformattedText == null) {
            throw new IllegalArgumentException("Unformatted text argument cannot be null.");
        }

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

    public String unformatText(CharSequence formattedText, int start, int end) {
        StringBuilder builder = new StringBuilder();

        if (formattedText == null) {
            throw new IllegalArgumentException("Formatted text argument cannot be null.");
        }

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

    public void setInputMaskString(String inputMaskString) {
        if (inputMaskString == null) {
            throw new IllegalArgumentException("Input mask string cannot be null.");
        }

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