package com.ibm.autoformatedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.databinding.BindingAdapter;

public class AutoFormatEditText extends AbstractAutoEditText {
    private static final char DEFAULT_PLACEHOLDER = '#';
    private Mask mask;

    public AutoFormatEditText(Context context) {
        super(context);
    }

    public AutoFormatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoFormatEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    void init(Context context, AttributeSet attrs, Integer defStyle) {
        super.init(context, attrs, defStyle);

        TypedArray a;
        if (defStyle != null) {
            a = context.obtainStyledAttributes(attrs, R.styleable.AutoFormatEditText, defStyle, 0);
        }else {
            a = context.obtainStyledAttributes(attrs, R.styleable.AutoFormatEditText);
        }

        String maskString = a.getString(R.styleable.AutoFormatEditText_mask);
        String maskPlaceholder = a.getString(R.styleable.AutoFormatEditText_mask_placeholder);

        char placeholder = DEFAULT_PLACEHOLDER;
        if (maskPlaceholder != null && maskPlaceholder.length() > 0) {
            placeholder = maskPlaceholder.charAt(0);
        }

        this.mask = new Mask(maskString, placeholder);

        a.recycle();
    }

    private void updateMaskString(String maskString) {
        mask.setMaskString(maskString);
    }

    @Override
    EditTextState format(String textBefore, String textAfter, int selectionStart, int selectionLength, int replacementLength) {
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

    //This works but is still experimental. Does not work properly if the mask is shorter than the masked text
    @BindingAdapter("mask")
    public static void setMask(AutoFormatEditText editText, String maskString) {
        editText.updateMaskString(maskString);
        editText.setText(editText.getRawText()); //Will cause re-masking
    }
}