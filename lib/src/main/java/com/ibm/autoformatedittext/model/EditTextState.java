package com.ibm.autoformatedittext.model;

import androidx.annotation.NonNull;

public class EditTextState {
    private String formattedText, unformattedText;
    private int selectionStart, selectionEnd;

    public EditTextState(@NonNull String formattedText, @NonNull String unformattedText, int selectionStart, int selectionEnd) {
        this.formattedText = formattedText;
        this.unformattedText = unformattedText;
        this.selectionStart = selectionStart;
        this.selectionEnd = selectionEnd;
    }

    public EditTextState(String formattedText, String unformattedText, int cursorPos) {
        this(formattedText, unformattedText, cursorPos, cursorPos);
    }

    public String getFormattedText() {
        return formattedText;
    }

    public String getUnformattedText() {
        return unformattedText;
    }

    public int getSelectionStart() {
        return selectionStart;
    }

    public int getSelectionEnd() {
        return selectionEnd;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + formattedText.hashCode();
        result = 31 * result + unformattedText.hashCode();
        result = 31 * result + selectionStart;
        result = 31 * result + selectionEnd;
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof EditTextState)) {
            return false;
        }

        EditTextState editTextState = (EditTextState) object;
        return formattedText.equals(editTextState.formattedText) &&
                    unformattedText.equals(editTextState.unformattedText) &&
                    selectionStart == editTextState.selectionStart &&
                    selectionEnd == editTextState.selectionEnd;
    }
}