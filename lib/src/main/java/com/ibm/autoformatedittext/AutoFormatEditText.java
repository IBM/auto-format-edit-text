package com.ibm.autoformatedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.ibm.autoformatedittext.lib.R;
import com.ibm.autoformatedittext.model.EditTextState;
import com.ibm.autoformatedittext.util.TextChangeEvent;
import com.ibm.autoformatedittext.util.HideFormatter;
import com.ibm.autoformatedittext.util.SimpleMaskFilter;

@SuppressWarnings("unused")
public class AutoFormatEditText extends FormattedInputEditText {
    private String hideModeFormat;
    private SimpleMaskFilter simpleMaskFilter;
    private boolean hideModeEnabled;

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

    public void setHideModeFormat(String hideModeFormat) {
        this.hideModeFormat = hideModeFormat;

        if (hideModeEnabled) {
            updateHideModeText();
        }
    }

    public void setHideModeEnabled(boolean enabled) {
        this.hideModeEnabled = enabled;

        if (enabled) {
            updateHideModeText();
        }else {
            setText(unformattedText);
        }
    }

    private void updateHideModeText() {
        String s = HideFormatter.format(unformattedText, hideModeFormat);
        setTextNoFormat(s);
    }

    @Override
    public boolean formattingEnabled() {
        return simpleMaskFilter != null &&
                simpleMaskFilter.getMaskString() != null &&
                simpleMaskFilter.getMaskString().length() > 0;
    }

    @Override
    public EditTextState format(TextChangeEvent textChangeEvent) {
        if (hideModeEnabled) {
            return null;
        }

        return simpleMaskFilter.filter(textChangeEvent);
    }

    public boolean isHideModeEnabled() {
        return hideModeEnabled;
    }

    public SimpleMaskFilter getSimpleMaskFilter() {
        return simpleMaskFilter;
    }
}