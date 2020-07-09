package com.ibm.autoformatedittext.model;

@SuppressWarnings("WeakerAccess")
public class EditTextState {
    private String formattedText, unformattedText;
    private int cursorStart, cursorEnd;

    public EditTextState(String formattedText, String unformattedText, int cursorStart, int cursorEnd) {
        this.formattedText = formattedText;
        this.unformattedText = unformattedText;
        this.cursorStart = cursorStart;
        this.cursorEnd = cursorEnd;
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

    public int getCursorStart() {
        return cursorStart;
    }

    public int getCursorEnd() {
        return cursorEnd;
    }
}
