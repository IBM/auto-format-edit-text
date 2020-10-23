package com.ibm.autoformatedittext.util;

import com.ibm.autoformatedittext.model.EditTextState;

public class SimpleMaskFilter {
    private static final char DEFAULT_PLACEHOLDER = '#';
    private InputMask inputMask;
    private boolean shiftModeEnabled;

    public SimpleMaskFilter(String maskString, String placeholderString, boolean shiftModeEnabled) {
        if (maskString == null) {
            maskString = "";
        }

        this.inputMask = new InputMask(maskString, getPlaceholder(placeholderString));
        this.shiftModeEnabled = shiftModeEnabled;
    }

    public void setMaskString(String maskString) {
        inputMask.setInputMaskString(maskString);
    }

    public void setPlaceholder(String placeholderString) {
        inputMask.setPlaceholder(getPlaceholder(placeholderString));
    }

    public void setShiftModeEnabled(boolean shiftModeEnabled) {
        this.shiftModeEnabled = shiftModeEnabled;
    }

    private char getPlaceholder(String placeholderString) {
        boolean placeholderValid = placeholderString != null && placeholderString.length() == 1;
        return placeholderValid ? placeholderString.charAt(0) : DEFAULT_PLACEHOLDER;
    }

    public EditTextState filter(TextChangeEvent event) {
        //Filter should prevent user from inserting text
        if (shouldAllowFilter(event)) {
            return null;
        }

        String leftUnformatted = inputMask.unformatText(event.getTextBefore(), 0, event.getSelectionStart());
        String rightUnformatted = inputMask.unformatText(event.getTextBefore(), event.getSelectionEnd(), event.getTextBefore().length());

        boolean shouldRemoveLeftCharacter = removeLeftCharacterOnBackspace(event.isBackspace(), leftUnformatted, event.getSelectionStart());
        if (shouldRemoveLeftCharacter) {
            leftUnformatted = leftUnformatted.substring(0, leftUnformatted.length() - 1);
        }

        String newUnformattedText = leftUnformatted + event.getInsertedText() + rightUnformatted;
        String newFormattedText = inputMask.formatText(newUnformattedText);

        boolean shouldRetainCursorPosition = retainCursorPositionOnBackspace(event.isBackspace(), leftUnformatted);
        if (shouldRetainCursorPosition) {
            return new EditTextState(newFormattedText, newUnformattedText, event.getSelectionStart());
        }

        int newCursorPos = inputMask.formatText(leftUnformatted + event.getInsertedText()).length();
        return new EditTextState(newFormattedText, newUnformattedText, newCursorPos);
    }

    public boolean maskEmpty() {
        return inputMask == null ||
                inputMask.getInputMaskString() == null ||
                inputMask.getInputMaskString().isEmpty();
    }

    private boolean shouldAllowFilter(TextChangeEvent event) {
        String textAfter = event.getTextAfter();
        int selectionLength = event.getSelectionEnd() - event.getSelectionStart();

        return  textAfter.length() > inputMask.getInputMaskString().length()
                && selectionLength != event.getReplacementLength()
                && event.getSelectionStart() > 0
                && !inputMask.matches(textAfter);
    }

    //Special case where user has backspaced in front of a character added by the input mask
    private boolean removeLeftCharacterOnBackspace(boolean backspaced, String leftText, int selectionStart) {
        return backspaced && shiftModeEnabled &&
                leftText.length() > 0 &&
                !inputMask.isPlaceholder(selectionStart);
    }

    //When user backspaces but is not in shift mode, even if no character ends up removed, the cursor keeps its new position
    private boolean retainCursorPositionOnBackspace(boolean backspaced, String leftUnformatted) {
        return backspaced &&
                !shiftModeEnabled &&
                leftUnformatted.length() > 0;
    }
}