package com.ibm.autoformatedittext.inputmask;

import com.ibm.autoformatedittext.FormattedInputEditText;
import com.ibm.autoformatedittext.model.EditTextState;

public class DynamicMaskFilter implements FormattedInputEditText.MaskingInputFilter {
    private static final char DEFAULT_PLACEHOLDER = '#';
    private InputMask inputMask;
    private boolean shiftModeEnabled;

    public DynamicMaskFilter(String maskString, String placeholderString, boolean shiftModeEnabled) {
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

    @Override
    public EditTextState filter(String textBefore, String textAfter,
                                int selectionStart, int selectionLength, int replacementLength) {
        //Case where no input mask exists, so the text can be entered without restriction
        if (inputMask == null || inputMask.getInputMaskString() == null || inputMask.getInputMaskString().isEmpty()) {
            return new EditTextState(textAfter, textAfter, selectionStart + replacementLength);
        }

        //Case where user is attempting to enter text beyond the length of the input mask
        if (textAfter.length() > inputMask.getInputMaskString().length()
                && selectionLength != replacementLength && selectionStart > 0
                && !inputMask.matches(textAfter)) {
            //TODO: Wouldn't it be helpful if we were passed the unformatted text rather than recalculating it
            String newUnformattedText = inputMask.unformatText(textBefore, 0, textBefore.length());
            return new EditTextState(textBefore, newUnformattedText, selectionStart);
        }

        CharSequence insertedText = textAfter.subSequence(selectionStart, selectionStart + replacementLength);
        String leftUnformatted = inputMask.unformatText(textBefore, 0, selectionStart);
        String rightUnformatted = inputMask.unformatText(textBefore, selectionStart + selectionLength, textBefore.length());

        //Gives a fairly good idea of whether the user removed a single character without watching for keypresses
        boolean userBackspaced = leftUnformatted.length() <= inputMask.getUnformattedLength() &&
                !inputMask.isPlaceholder(selectionStart) &&
                selectionLength == 1 &&
                replacementLength == 0;

        //Special case where user has backspaced in front of a character added by the input mask
        //If in shift mode, remove next character to the left
        if (leftUnformatted.length() > 0 && userBackspaced && shiftModeEnabled) {
            leftUnformatted = leftUnformatted.substring(0, leftUnformatted.length() - 1);
        }

        String newUnformattedText = leftUnformatted + insertedText + rightUnformatted;
        String newFormattedText = inputMask.formatText(newUnformattedText);

        //When user backspaces but is not in shift mode, even if no character ends up removed, the cursor keeps its new position
        int cursorPos = userBackspaced && !shiftModeEnabled ? selectionStart :
                inputMask.formatText(leftUnformatted + insertedText).length();

        return new EditTextState(newFormattedText, newUnformattedText, cursorPos);
    }
}