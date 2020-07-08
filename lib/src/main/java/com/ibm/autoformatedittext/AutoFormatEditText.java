package com.ibm.autoformatedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.databinding.BindingAdapter;

import com.carljmont.lib.R;
import com.ibm.autoformatedittext.inputmask.MaskingInputMaskFilter;
import com.ibm.autoformatedittext.util.StaticFormatter;

public class AutoFormatEditText extends FormattedEditText {
    private String staticFormat;
    private MaskingInputMaskFilter inputMaskFilter;

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

            inputMaskFilter = new MaskingInputMaskFilter(inputMaskString, placeholderString, shiftModeEnabled);
            setMaskingInputFilter(inputMaskFilter);
        }
    }

    private void setInputMask(String inputMaskString) {
        inputMaskFilter.setMaskString(inputMaskString);
        setText("");
    }

    private void setInputMaskPlaceholder(String placeholderString) {
        inputMaskFilter.setPlaceholder(placeholderString);
    }

    private void setShiftModeEnabled(boolean enabled) {
        inputMaskFilter.setShiftModeEnabled(enabled);
    }

    private void setStaticFormat(String staticFormat) {
        this.staticFormat = staticFormat;
        refreshStaticReplacement();
    }

    @Override
    public String getStaticReplacement(String unformattedText) {
        return StaticFormatter.format(unformattedText, staticFormat);
    }

    @BindingAdapter("inputMask")
    public static void setInputMask(AutoFormatEditText editText, String inputMaskString) {
        editText.setInputMask(inputMaskString);
    }

    @BindingAdapter("inputMaskPlaceholder")
    public static void setInputMaskPlaceholder(AutoFormatEditText editText, String placeholderString) {
        editText.setInputMaskPlaceholder(placeholderString);
    }

    @BindingAdapter("shiftModeEnabled")
    public static void setShiftModeEnabled(AutoFormatEditText editText, boolean enabled) {
        editText.setShiftModeEnabled(enabled);
    }

    @BindingAdapter("staticFormat")
    public static void setStaticFormat(AutoFormatEditText editText, String staticFormat) {
        editText.setStaticFormat(staticFormat);
    }
}