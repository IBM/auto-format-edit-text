package com.ibm.autoformatedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;

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
    private boolean textChangeActive;

    private String textBefore, textAfter, formattedText, unformattedText;
    private int selectionStart, selectionLength, replacementLength;

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

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbstractAutoEditText);
            CharSequence text = a.getText(R.styleable.AbstractAutoEditText_android_text);
            a.recycle();

            if (text != null && text.length() > 0) {
                setText(text);
            }
        }

        //Prevents edge case where multiple callbacks are occurring for input type 'text'
        if (getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE) ||
                getInputType() == InputType.TYPE_CLASS_TEXT || getInputType() == InputType.TYPE_TEXT_FLAG_MULTI_LINE) {
            setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    abstract EditTextState format(String textBefore, String textAfter, int selectionStart, int selectionLength, int replacementLength);

    private void setUpTextWatcher() {
        removeTextChangedListener(textWatcher);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                handleBeforeTextChanged(s);
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

    public void handleBeforeTextChanged(CharSequence s) {
        textChangeActive = true;
        textBefore = s.toString();
    }

    public void handleOnTextChanged(CharSequence s, int start, int before, int count) {
        if (textChangeActive) {
            textAfter = s.toString();
            selectionStart = start;
            selectionLength = before;
            replacementLength = count;
        }
    }

    public void handleAfterTextChanged() {
        removeTextChangedListener(textWatcher); //Removing/re-adding listener will prevent never ending loop

        EditTextState newEditTextState = format(textBefore, textAfter, selectionStart, selectionLength, replacementLength);

        if (unformattedText == null || !unformattedText.equals(newEditTextState.getUnformattedText())) {
            unformattedText = newEditTextState.getUnformattedText(); //New unformatted text
            if (onUnformattedValueListener != null) {
                onUnformattedValueListener.onUnformattedValueChanged(unformattedText);
            }
        }

        if (formattedText == null || !formattedText.equals(newEditTextState.getFormattedText())) {
            formattedText = newEditTextState.getFormattedText();
            if (onTextChangedListener != null) {
                onTextChangedListener.onTextChanged(formattedText);
            }
        }

        setNewText(formattedText);

        //Setting text programmatically resets the cursor, so this will reposition it
        setSelection(newEditTextState.getCursorStart(), newEditTextState.getCursorEnd());

        addTextChangedListener(textWatcher);
        textChangeActive = false;
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

    @BindingAdapter("text")
    public static void setText(AbstractAutoEditText editText, String newText) {
        editText.setNewText(newText);
    }

    public interface UnformattedValueListener {
        void onUnformattedValueChanged(String value);
    }


    public interface TextChangedListener {
        void onTextChanged(String text);
    }

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