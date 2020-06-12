package com.ibm.autoformatedittext;

class MaskedTextBuilder extends BaseFormattedTextBuilder {
    private static final char DEFAULT_PLACEHOLDER = '#';
    private Mask mask;

    private MaskedTextBuilder(String maskString, String maskPlaceholder) {
        char placeholder = DEFAULT_PLACEHOLDER;
        if (maskPlaceholder != null && maskPlaceholder.length() > 0) {
            placeholder = maskPlaceholder.charAt(0);
        }

        this.mask = new Mask(maskString, placeholder);
    }

    static MaskedTextBuilder newInstance(String maskString, String maskPlaceholder) {
        return new MaskedTextBuilder(maskString, maskPlaceholder);
    }

    @Override
    void setMask(String maskString) {
        this.mask.setMaskString(maskString);
    }

    @Override
    EditTextState build() {
        EditTextState editTextState = new EditTextState();

        //Case where no mask exists, so the text can be entered without restriction
        if (mask.getMaskString() == null || mask.getMaskString().isEmpty()) {
            editTextState.setMaskedText(textAfter);
            editTextState.setUnmaskedText(textAfter);
            editTextState.setCursorPos(selectionStart + replacementLength);
            return editTextState;
        }

        //Case where user is attempting to enter text beyond the length of the mask
        if (textAfter.length() > mask.getMaskString().length()) {
            editTextState.setMaskedText(textBefore);
            editTextState.setUnmaskedText(mask.unmaskText(textBefore, 0, textBefore.length()));
            editTextState.setCursorPos(selectionStart);
            return editTextState;
        }

        int selectionEnd = selectionStart + selectionLength;
        String leftUnmasked = mask.unmaskText(textBefore, 0, selectionStart);
        String rightUnmasked = mask.unmaskText(textBefore, selectionEnd, textBefore.length());

        //Special case where user has backspaced in front of a non-masked character
        //Remove next masked character
        if (leftUnmasked.length() > 0 && leftUnmasked.length() <= mask.getUnmaskedLength() &&
                !mask.isPlaceholder(selectionStart) && selectionLength == 1 && replacementLength == 0) {
            leftUnmasked = leftUnmasked.substring(0, leftUnmasked.length() - 1);
        }

        int replacementEnd = selectionStart + replacementLength;
        String leftMidUnmaskedText = leftUnmasked + textAfter.subSequence(selectionStart, replacementEnd);
        String newRawText = leftMidUnmaskedText + rightUnmasked;

        editTextState.setUnmaskedText(newRawText);
        editTextState.setMaskedText(mask.maskText(newRawText));

        int cursorPos = mask.maskText(leftMidUnmaskedText).length();
        editTextState.setCursorPos(cursorPos);

        return editTextState;
    }
}
