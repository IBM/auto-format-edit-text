package com.ibm.autoformatedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;

public abstract class AbstractAutoEditText extends AppCompatEditText {
    private AutoFormatTextChangeListener changeListener;
    private TextWatcher textWatcher;
    private boolean textChangeActive;
    private String rawText = "";

    private String textBefore, textAfter;
    private int selectionStart, selectionLength, replacementLength;

    public AbstractAutoEditText(Context context) {
        super(context);
    }

    public AbstractAutoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, null);
    }

    public AbstractAutoEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    void init(Context context, AttributeSet attrs, Integer defStyle) {
        if (attrs == null) {
            return;
        }

        setUpTextWatcher();

        TypedArray a;
        if (defStyle != null) {
            a = context.obtainStyledAttributes(attrs, R.styleable.AbstractAutoEditText, defStyle, 0);
        }else {
            a = context.obtainStyledAttributes(attrs, R.styleable.AbstractAutoEditText);
        }

        CharSequence text = a.getText(R.styleable.AbstractAutoEditText_android_text);
        if (text != null && text.length() > 0) {
            setNewText(text);
        }

        a.recycle();

        //Prevents edge case where multiple callbacks are occurring for input from edit texts with suggestions
        if (getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE)) {
            setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    abstract EditTextState format(String textBefore, String textAfter, int selectionStart, int selectionLength, int replacementLength);

    private void setUpTextWatcher() {
        removeTextChangedListener(textWatcher);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                textChangeActive = true;
                textBefore = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleOnTextChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                handleAfterTextChanged();
            }
        };

        addTextChangedListener(textWatcher);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeTextChangedListener(textWatcher);
        textWatcher = null;
    }

    private void handleOnTextChanged(CharSequence s, int start, int before, int count) {
        if (textChangeActive) {
            textAfter = s.toString();
            selectionStart = start;
            selectionLength = before;
            replacementLength = count;
        }
    }

    private void handleAfterTextChanged() {
        removeTextChangedListener(textWatcher); //Removing/re-adding listener will prevent never ending loop

        EditTextState newEditTextState = format(textBefore, textAfter, selectionStart, selectionLength, replacementLength);
        rawText = newEditTextState.getUnmaskedText(); //New raw text
        setText(newEditTextState.getMaskedText()); //Set new edit text string

        //Setting text programmatically resets the cursor, so this will reposition it
        setSelection(newEditTextState.getCursorStart(), newEditTextState.getCursorEnd());

        if (changeListener != null) {
            changeListener.onTextChanged(rawText,
                    newEditTextState.getMaskedText(),
                    newEditTextState.getCursorStart());
        }

        addTextChangedListener(textWatcher);
        textChangeActive = false;
    }

    public void setNewText(CharSequence newText) {
        if (newText == null) {
            newText = "";
        }

        if (!rawText.equals(newText.toString())) {
            setText(newText);
        }
    }

    String getRawText() {
        return rawText;
    }

    public void setOnChangeListener(AutoFormatTextChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    @BindingAdapter("android:text")
    public static void setText(AbstractAutoEditText editText, String text) {
        editText.setNewText(text);
    }

    @BindingAdapter("rawText")
    public static void setRawText(AbstractAutoEditText editText, String rawText) {
        editText.setNewText(rawText);
    }

    @InverseBindingAdapter(attribute = "rawText", event = "android:textAttrChanged")
    public static String getText(AbstractAutoEditText editText) {
        return editText.getRawText();
    }

    public interface AutoFormatTextChangeListener {
        void onTextChanged(String rawValue, String maskedValue, int position);
    }

    static class EditTextState {
        private String unmaskedText, maskedText;
        private int cursorStart, cursorEnd;

        String getUnmaskedText() {
            return unmaskedText;
        }

        void setUnmaskedText(String unmaskedText) {
            this.unmaskedText = unmaskedText;
        }

        String getMaskedText() {
            return maskedText;
        }

        void setMaskedText(String maskedText) {
            this.maskedText = maskedText;
        }

        int getCursorStart() {
            return cursorStart;
        }

        int getCursorEnd() {
            return cursorEnd;
        }

        void setCursor(int cursorStart, int cursorEnd) {
            this.cursorStart = cursorStart;
            this.cursorEnd = cursorEnd;
        }

        void setCursor(int cursorPos) {
            this.cursorStart = cursorPos;
            this.cursorEnd = cursorPos;
        }
    }
}