package com.ibm.autoformatedittext;

class Mask {
    private char maskPlaceholder;
    private String maskString;
    private Integer unmaskedLength;

    Mask(String maskString, char maskPlaceholder) {
        this.maskString = maskString;
        this.maskPlaceholder = maskPlaceholder;
    }

    boolean isPlaceholder(int index) {
        return maskString.charAt(index) == maskPlaceholder;
    }

    int getUnmaskedLength() {
        if (unmaskedLength == null) {
            unmaskedLength = 0;

            for (int i = 0; i < maskString.length(); i++) {
                if (maskString.charAt(i) == maskPlaceholder) {
                    unmaskedLength++;
                }
            }
        }

        return unmaskedLength;
    }

    String maskText(String unmaskedText) {
        StringBuilder builder = new StringBuilder();

        if (unmaskedText.length() == 0) {
            return "";
        }

        int unmaskedTextPosition = 0;
        for (int i = 0; i < maskString.length(); i++) {
            if (maskString.charAt(i) == maskPlaceholder) {
                if (unmaskedTextPosition == unmaskedText.length()) {
                    break;
                }

                builder.append(unmaskedText.charAt(unmaskedTextPosition));
                unmaskedTextPosition++;
            }else {
                builder.append(maskString.charAt(i));
            }
        }

        return builder.toString();
    }

    String unmaskText(CharSequence maskedText, int start, int end) {
        StringBuilder builder = new StringBuilder();

        if (maskedText.length() > 0) {
            for (int i = start; i < end; i++) {
                if (maskString.charAt(i) == maskPlaceholder) {
                    builder.append(maskedText.charAt(i));
                }
            }
        }

        return builder.toString();
    }

    String getMaskString() {
        return maskString;
    }

    void setMaskString(String maskString) {
        this.maskString = maskString;
        this.unmaskedLength = null;
    }
}