package com.ibm.autoformatedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.databinding.BindingAdapter;

import com.carljmont.lib.R;
import com.ibm.autoformatedittext.inputmask.DynamicMaskFilter;
import com.ibm.autoformatedittext.util.HideFormatter;

@SuppressWarnings("unused")
public class AutoFormatEditText extends FormattedInputEditText {
    private String hideModeFormat;
    private DynamicMaskFilter dynamicMaskFilter;

    public AutoFormatEditText(Context context) {
        super(context);
    }

    public AutoFormatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoFormatEditText);
            String inputMaskString = a.getString(R.styleable.AutoFormatEditText_inputMask);
            String placeholderString = a.getString(R.styleable.AutoFormatEditText_inputMaskPlaceholder);
            boolean shiftModeEnabled = a.getBoolean(R.styleable.AutoFormatEditText_shiftModeEnabled, false);
            a.recycle();

            dynamicMaskFilter = new DynamicMaskFilter(inputMaskString, placeholderString, shiftModeEnabled);
            setMaskingInputFilter(dynamicMaskFilter);
        }
    }

    public void setInputMask(String inputMaskString) {
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