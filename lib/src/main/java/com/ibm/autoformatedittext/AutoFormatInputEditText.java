package com.ibm.autoformatedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.databinding.BindingAdapter;

import com.carljmont.lib.R;
import com.ibm.autoformatedittext.inputmask.DynamicMaskFilter;
import com.ibm.autoformatedittext.util.HideFormatter;

public class AutoFormatInputEditText extends FormattedInputEditText {
    private String hideModeFormat;
    private DynamicMaskFilter dynamicMaskFilter;

    public AutoFormatInputEditText(Context context) {
        super(context);
    }

    public AutoFormatInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoFormatInputEditText);
            String inputMaskString = a.getString(R.styleable.AutoFormatInputEditText_inputMask);
            String placeholderString = a.getString(R.styleable.AutoFormatInputEditText_inputMaskPlaceholder);
            boolean shiftModeEnabled = a.getBoolean(R.styleable.AutoFormatInputEditText_shiftModeEnabled, false);
            a.recycle();

            dynamicMaskFilter = new DynamicMaskFilter(inputMaskString, placeholderString, shiftModeEnabled);
            setMaskingInputFilter(dynamicMaskFilter);
        }
    }

    private void setInputMask(String inputMaskString) {
        dynamicMaskFilter.setMaskString(inputMaskString);
        setText("");
    }

    private void setInputMaskPlaceholder(String placeholderString) {
        dynamicMaskFilter.setPlaceholder(placeholderString);
        setText("");
    }

    private void setShiftModeEnabled(boolean enabled) {
        dynamicMaskFilter.setShiftModeEnabled(enabled);
    }

    private void setHideModeFormat(String staticFormat) {
        this.hideModeFormat = staticFormat;
        refreshHideModeReplacementText();
    }

    @Override
    public String getHideModeReplacementText(String unformattedText) {
        return HideFormatter.format(unformattedText, hideModeFormat);
    }

    @BindingAdapter("inputMask")
    public static void setInputMask(AutoFormatInputEditText editText, String mask) {
        editText.setInputMask(mask);
    }

    @BindingAdapter("inputMaskPlaceholder")
    public static void setInputMaskPlaceholder(AutoFormatInputEditText editText, String placeholder) {
        editText.setInputMaskPlaceholder(placeholder);
    }

    @BindingAdapter("shiftModeEnabled")
    public static void setShiftModeEnabled(AutoFormatInputEditText editText, boolean enabled) {
        editText.setShiftModeEnabled(enabled);
    }

    @BindingAdapter("hideModeFormat")
    public static void setHideModeFormat(AutoFormatInputEditText editText, String format) {
        editText.setHideModeFormat(format);
    }
}