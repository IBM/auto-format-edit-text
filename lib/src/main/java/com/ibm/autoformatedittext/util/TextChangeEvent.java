package com.ibm.autoformatedittext.util;

@SuppressWarnings("unused")
public class TextChangeEvent {
    private String textBefore, textAfter, removedText, insertedText;
    private int selectionStart, selectionLength, replacementLength;

    public TextChangeEvent(String textBefore, String textAfter, int start, int before, int count) {
        this.textBefore = textBefore;
        this.textAfter = textAfter;
        this.selectionStart = start;
        this.selectionLength = before;
        this.replacementLength = count;
    }

    public String getTextBefore() {
        return textBefore;
    }

    public String getTextAfter() {
        return textAfter;
    }

    public int getSelectionStart() {
        return selectionStart;
    }

    public int getSelectionLength() {
        return selectionLength;
    }

    public int getInsertedLength() {
        return replacementLength;
    }

    public int getSelectionEnd() {
        return selectionStart + selectionLength;
    }

    public String getRemovedText() {
        if (removedText == null) {
            removedText = textBefore.substring(selectionStart, getSelectionEnd());
        }
        return removedText;
    }

    public String getInsertedText() {
        if (insertedText == null) {
            insertedText = textAfter.substring(selectionStart, selectionStart + replacementLength);
        }
        return insertedText;
    }

    public boolean isBackspace() {
        return selectionLength == 1 && replacementLength == 0;
    }
}
