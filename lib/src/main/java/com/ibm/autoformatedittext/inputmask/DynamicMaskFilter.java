package com.ibm.autoformatedittext.inputmask;

import com.ibm.autoformatedittext.FormattedEditText;
import com.ibm.autoformatedittext.model.EditTextState;

public class DynamicMaskFilter implements FormattedEditText.MaskingInputFilter {
    private InputMask inputMask;
    private boolean shiftModeEnabled;

    public DynamicMaskFilter(String maskString, String placeholder, boolean shiftModeEnabled) {
        this.inputMask = new InputMask(maskString, placeholder);
        this.shiftModeEnabled = shiftModeEnabled;
    }

    public void setMaskString(String maskString) {
        inputMask.setInputMaskString(maskString);
    }

    public void setPlaceholder(String placeholder) {
        inputMask.setPlaceholder(placeholder);
    }

    public void setShiftModeEnabled(boolean shiftModeEnabled) {
        this.shiftModeEnabled = shiftModeEnabled;
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
            String newUnformattedText = inputMask.unformatText(textBefore, 0, textBefore.length());
            return new EditTextState(textBefore, newUnformattedText, selectionStart);
        }

        CharSequence insertedText = textAfter.subSequence(selectionStart, selectionStart + replacementLength);
        String leftUnformatted = inputMask.unformatText(textBefore, 0, selectionStart);
        String rightUnformatted = inputMask.unformatText(textBefore, selectionStart + selectionLength, textBefore.length());

        boolean userBackspaced = leftUnformatted.length() > 0 &&
                leftUnformatted.length() <= inputMask.getUnformattedLength() &&
                !inputMask.isPlaceholder(selectionStart) &&
                selectionLength == 1 &&
                replacementLength == 0;

        //Special case where user has backspaced in front of a character added by the input mask
        //If in shift mode, remove next character to the left
        if (userBackspaced && shiftModeEnabled) {
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