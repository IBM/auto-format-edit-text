package com.ibm.autoformatedittext;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

@BindingMethods({
        @BindingMethod(type = AbstractAutoEditText.class, attribute = "onTextChanged", method = "setTextChangedListener"),
        @BindingMethod(type = AbstractAutoEditText.class, attribute = "onUnformattedValueChanged", method = "setUnformattedValueChangedListener")
})
public abstract class AbstractAutoEditText extends AppCompatEditText {
    private UnformattedValueListener onUnformattedValueListener;
    private TextChangedListener onTextChangedListener;

    private TextWatcher textWatcher;

    private boolean isStaticReplacementVisible;
    private String textBefore;
    private String formattedText, unformattedText;

    public AbstractAutoEditText(Context context) {
        super(context);
        init(context, null);
    }

    public AbstractAutoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {
        setUpTextWatcher();
        formattedText = "";

        //Prevents edge case where multiple callbacks are occurring for input type 'text'
        if (getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE) ||
                getInputType() == InputType.TYPE_CLASS_TEXT || getInputType() == InputType.TYPE_TEXT_FLAG_MULTI_LINE) {
            setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    abstract EditTextState format(String textBefore, String textAfter, int selectionStart, int selectionLength, int replacementLength);
    abstract String getStaticReplacement(String unformattedText);

    public void setStaticReplacementVisible(boolean visible) {
        this.isStaticReplacementVisible = visible;

        if (visible) {
            setTextNoWatch(getStaticReplacement(unformattedText));
        }else {
            setNewText(unformattedText);
        }
    }

    private void setUpTextWatcher() {
        removeTextChangedListener(textWatcher);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                textBefore = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleOnTextChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        addTextChangedListener(textWatcher);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeTextChangedListener(textWatcher);
        textWatcher = null;
    }

    public void handleOnTextChanged(CharSequence s, int selectionStart, int selectionLength, int replacementLength) {
        if (isStaticReplacementVisible) {
            setTextNoWatch(textBefore);
            setSelection(selectionStart + selectionLength);
            return;
        }

        //textBefore =  formattedText.trim().substring(0,formattedText.length());

        EditTextState newEditTextState = format(textBefore, s.toString(), selectionStart, selectionLength, replacementLength);

        if (formattedText == null || !formattedText.equals(newEditTextState.getFormattedText())) {
            formattedText = newEditTextState.getFormattedText();
            if (onTextChangedListener != null) {
                onTextChangedListener.onTextChanged(formattedText);
            }
        }

        if (unformattedText == null || !unformattedText.equals(newEditTextState.getUnformattedText())) {
            unformattedText = newEditTextState.getUnformattedText(); //New unformatted text
            if (onUnformattedValueListener != null) {
                onUnformattedValueListener.onUnformattedValueChanged(unformattedText);
            }
        }

        setTextNoWatch(formattedText);


        //Setting text programmatically resets the cursor, so this will reposition it
        setSelection(newEditTextState.getCursorStart(), newEditTextState.getCursorEnd());
    }

    private void setTextNoWatch(String s) {
        removeTextChangedListener(textWatcher); //Removing/re-adding listener will prevent never ending loop
        setNewText(s);
        addTextChangedListener(textWatcher);
    }

    private void setNewText(CharSequence s) {
        if (s != null && getText() != null &&
                !getText().toString().equals(s.toString())) {
            setText(s);
        }
    }

    public String getUnformattedText() {
        return unformattedText;
    }

    public String getFormattedText() {
        return formattedText;
    }

    public void setUnformattedValueChangedListener(UnformattedValueListener listener) {
        onUnformattedValueListener = listener;
    }

    public void setTextChangedListener(TextChangedListener listener) {
        onTextChangedListener = listener;
    }

    @BindingAdapter("android:text")
    public static void setTextAndroid(AbstractAutoEditText editText, String newText) {
        editText.setNewText(newText);
    }

    @BindingAdapter("staticReplacementVisible")
    public static void setStaticReplacementVisible(AutoFormatEditText editText, boolean visible) {
        editText.setStaticReplacementVisible(visible);
    }

    public interface UnformattedValueListener {
        void onUnformattedValueChanged(String value);
    }

    public interface TextChangedListener {
        void onTextChanged(String text);
    }

    @SuppressWarnings("WeakerAccess")
    public static class EditTextState {
        private String formattedText, unformattedText;
        private int cursorStart, cursorEnd;

        EditTextState(String formattedText, String unformattedText, int cursorStart, int cursorEnd) {
            this.formattedText = formattedText;
            this.unformattedText = unformattedText;
            this.cursorStart = cursorStart;
            this.cursorEnd = cursorEnd;
        }

        EditTextState(String formattedText, String unformattedText, int cursorPos) {
            this(formattedText, unformattedText, cursorPos, cursorPos);
        }

        String getFormattedText() {
            return formattedText;
        }

        String getUnformattedText() {
            return unformattedText;
        }

        int getCursorStart() {
            return cursorStart;
        }

        int getCursorEnd() {
            return cursorEnd;
        }
    }
}