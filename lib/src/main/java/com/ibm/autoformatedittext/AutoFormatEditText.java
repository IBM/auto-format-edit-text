package com.ibm.autoformatedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.databinding.BindingAdapter;

import com.carljmont.lib.R;
import com.ibm.autoformatedittext.model.EditTextState;
import com.ibm.autoformatedittext.util.HideFormatter;
import com.ibm.autoformatedittext.util.SimpleMaskFilter;

@SuppressWarnings("unused")
public class AutoFormatEditText extends FormattedInputEditText {
    private String hideModeFormat;
    private SimpleMaskFilter simpleMaskFilter;

    public AutoFormatEditText(Context context) {
        super(context);
    }

    public AutoFormatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoFormatEditText);
            String inputMaskString = a.getString(R.styleable.AutoFormatEditText_inputMask);
            String placeholderString = a.getString(R.styleable.AutoFormatEditText_inputMaskPlaceholder);
            boolean shiftModeEnabled = a.getBoolean(R.styleable.AutoFormatEditText_shiftModeEnabled, false);
            a.recycle();

            simpleMaskFilter = new SimpleMaskFilter(inputMaskString, placeholderString, shiftModeEnabled);
        }
    }

    public void setInputMask(String inputMaskString) {
        simpleMaskFilter.setMaskString(inputMaskString);
        setText("");
    }

    public void setInputMaskPlaceholder(String placeholderString) {
        simpleMaskFilter.setPlaceholder(placeholderString);
        setText("");
    }

    public void setShiftModeEnabled(boolean enabled) {
        simpleMaskFilter.setShiftModeEnabled(enabled);
    }

    public void setHideModeFormat(String staticFormat) {
        this.hideModeFormat = staticFormat;

        if (hideModeEnabled) {
            updateHideModeText();
        }
    }

    @Override
    public String getHideModeText(String unformattedText, String formattedText) {
        return HideFormatter.format(unformattedText, hideModeFormat);
    }

    @Override
    public EditTextState filter(String textBefore, String textAfter, int selectionStart, int selectionLength, int replacementLength) {
        if (simpleMaskFilter == null) {
            return null; //Null state will be ignored
        }

        return simpleMaskFilter.filter(textBefore, textAfter, selectionStart, selectionLength, replacementLength);
    }

    public SimpleMaskFilter getSimpleMaskFilter() {
        return simpleMaskFilter;
    }

    @BindingAdapter("inputMask")
    public static void setInputMask(AutoFormatEditText editText, String maskString) {
        editText.setInputMask(maskString);
    }

    @BindingAdapter("inputMaskPlaceholder")
    public static void setInputMaskPlaceholder(AutoFormatEditText editText, String placeholder) {
        editText.setInputMaskPlaceholder(placeholder);
    }

    @BindingAdapter("shiftModeEnabled")
    public static void setShiftModeEnabled(AutoFormatEditText editText, boolean enabled) {
        editText.setShiftModeEnabled(enabled);
    }

    @BindingAdapter("hideModeFormat")
    public static void setHideModeFormat(AutoFormatEditText editText, String format) {
        editText.setHideModeFormat(format);
    }
}