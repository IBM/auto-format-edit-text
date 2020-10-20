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

    public InputMask getMask() {
        return inputMask;
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

    public EditTextState filter(String textBefore, String textAfter, int selectionStart, int selectionLength, int replacementLength) {
        boolean noMask = inputMask == null ||
                inputMask.getInputMaskString() == null ||
                inputMask.getInputMaskString().isEmpty();

        if (noMask) {
            return new EditTextState(textAfter, textAfter, selectionStart + replacementLength);
        }

        boolean outsideMaskBounds = textAfter.length() > inputMask.getInputMaskString().length()
                && selectionLength != replacementLength && selectionStart > 0
                && !inputMask.matches(textAfter);

        //Undoes what the user entered by returning to previous state
        if (outsideMaskBounds) {
            String newUnformattedText = inputMask.unformatText(textBefore, 0, textBefore.length());
            return new EditTextState(textBefore, newUnformattedText, selectionStart);
        }

        return calculateUpdatedState(textBefore, textAfter, selectionStart, selectionLength, replacementLength);
    }

    private EditTextState calculateUpdatedState(String textBefore, String textAfter, int selectionStart, int selectionLength, int replacementLength) {
        CharSequence insertedText = textAfter.subSequence(selectionStart, selectionStart + replacementLength);
        String leftUnformatted = inputMask.unformatText(textBefore, 0, selectionStart);
        String rightUnformatted = inputMask.unformatText(textBefore, selectionStart + selectionLength, textBefore.length());

        boolean backspaced = replacementLength == 0 && selectionLength == 1;

        //Special case where user has backspaced in front of a character added by the input mask
        //If in shift mode, remove next character to the left
        if (leftUnformatted.length() > 0 && backspaced &&
                !inputMask.isPlaceholder(selectionStart) &&
                shiftModeEnabled) {
            leftUnformatted = leftUnformatted.substring(0, leftUnformatted.length() - 1);
        }

        String newUnformattedText = leftUnformatted + insertedText + rightUnformatted;
        String newFormattedText = inputMask.formatText(newUnformattedText);

        //When user backspaces but is not in shift mode, even if no character ends up removed, the cursor keeps its new position
        boolean keepCurrentCursorPosition = backspaced && !shiftModeEnabled && leftUnformatted.length() > 0;

        int cursorPos = keepCurrentCursorPosition ? selectionStart :
                inputMask.formatText(leftUnformatted + insertedText).length();

        return new EditTextState(newFormattedText, newUnformattedText, cursorPos);
    }
}