package com.ibm.autoformatedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.databinding.BindingAdapter;

public class AutoFormatEditText extends AbstractAutoEditText {
    private static final char DEFAULT_PLACEHOLDER = '#';
    private Formatter formatter;

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

        String format = a.getString(R.styleable.AutoFormatEditText_format);
        String formatPlaceholder = a.getString(R.styleable.AutoFormatEditText_placeholder);
        a.recycle();

        char placeholder = DEFAULT_PLACEHOLDER;
        if (formatPlaceholder != null && formatPlaceholder.length() > 0) {
            placeholder = formatPlaceholder.charAt(0);
        }

        formatter = new Formatter(format, placeholder);
    }

    private void setFormat(String format) {
        formatter.setFormat(format);
        setText(getUnformattedText()); //Will cause re-formatting
    }

    @Override
    EditTextState format(String textBefore, String textAfter, int selectionStart, int selectionLength, int replacementLength) {
        //Case where no format exists, so the text can be entered without restriction
        if (formatter == null || formatter.getFormat() == null || formatter.getFormat().isEmpty()) {
            return new EditTextState(textAfter, textAfter, selectionStart + replacementLength);
        }

        //Case where user is attempting to enter text beyond the length of the format
        if (textAfter.length() > formatter.getFormat().length()
            && selectionLength != replacementLength && selectionStart > 0
            && !formatter.matches(textAfter)) {
                String newUnformattedText = formatter.unformatText(textBefore, 0, textBefore.length());
                return new EditTextState(textBefore, newUnformattedText, selectionStart);
        }

        CharSequence insertedText = textAfter.subSequence(selectionStart, selectionStart + replacementLength);
        String leftUnformatted = formatter.unformatText(textBefore, 0, selectionStart);
        String rightUnformatted = formatter.unformatText(textBefore, selectionStart + selectionLength, textBefore.length());

        //Special case where user has backspaced in front of a character dictated by the format
        //Remove next character not dictated by the format
        if (leftUnformatted.length() > 0 &&
                leftUnformatted.length() <= formatter.getUnformattedLength() &&
                !formatter.isPlaceholder(selectionStart) &&
                selectionLength == 1 && replacementLength == 0) {
            leftUnformatted = leftUnformatted.substring(0, leftUnformatted.length() - 1);
        }

        String newUnformattedText = leftUnformatted + insertedText + rightUnformatted;
        String newFormattedText = formatter.formatText(newUnformattedText);
        int cursorPos = formatter.formatText(leftUnformatted + insertedText).length();

        return new EditTextState(newFormattedText, newUnformattedText, cursorPos);
    }

    @BindingAdapter("format")
    public static void setFormat(AutoFormatEditText editText, String format) {
        editText.setFormat(format);
    }
}