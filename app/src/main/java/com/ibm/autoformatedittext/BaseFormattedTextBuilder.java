package com.ibm.autoformatedittext;

abstract class BaseFormattedTextBuilder {
    String textBefore, textAfter;
    int selectionStart, selectionLength, replacementLength;

    BaseFormattedTextBuilder setTextBeforeChange(String textBefore) {
        this.textBefore = textBefore;
        return this;
    }

    BaseFormattedTextBuilder setTextAfterChange(String textAfter) {
        this.textAfter = textAfter;
        return this;
    }

    BaseFormattedTextBuilder setSelectionStart(int selectionStart) {
        this.selectionStart = selectionStart;
        return this;
    }

    BaseFormattedTextBuilder setSelectionLength(int selectionLength) {
        this.selectionLength = selectionLength;
        return this;
    }

    BaseFormattedTextBuilder setReplacementLength(int replacementLength) {
        this.replacementLength = replacementLength;
        return this;
    }

    abstract void setMask(String mask);

    abstract EditTextState build();

    static class EditTextState {
        private String unmaskedText, maskedText;
        private int cursorPos;

        String getUnmaskedText() {
            return unmaskedText;
        }

        void setUnmaskedText(String unmaskedText) {
            this.unmaskedText = unmaskedText;
        }

        String getMaskedText() {
            return maskedText;
        }

        void setMaskedText(String maskedText) {
            this.maskedText = maskedText;
        }

        int getCursorPos() {
            return cursorPos;
        }

        void setCursorPos(int cursorPos) {
            this.cursorPos = cursorPos;
        }
    }
}
