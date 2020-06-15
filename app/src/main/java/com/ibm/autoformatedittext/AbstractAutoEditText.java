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

    private String textBefore, textAfter, unformattedText;
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
        a.recycle();

        if (text != null && text.length() > 0) {
            setText(text);
        }

        //Prevents edge case where multiple callbacks are occurring for text input type
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
        unformattedText = newEditTextState.getUnformattedText(); //New unformatted text
        setText(newEditTextState.getFormattedText()); //Set new edit text string

        //Setting text programmatically resets the cursor, so this will reposition it
        setSelection(newEditTextState.getCursorStart(), newEditTextState.getCursorEnd());

        if (changeListener != null) {
            changeListener.onTextChanged(unformattedText,
                    newEditTextState.getFormattedText(),
                    newEditTextState.getCursorStart());
        }

        addTextChangedListener(textWatcher);
        textChangeActive = false;
    }

    private void setNewText(CharSequence s) {
        if (s == null) {
            s = "";
        }

        if (getText() != null && !getText().equals(s)) {
            setText(s);
        }
    }

    private void setUnformattedText(CharSequence s) {
        if (s == null) {
            s = "";
        }

        if (!s.toString().equals(unformattedText)) {
            setText(s);
        }
    }

    String getUnformattedText() {
        return unformattedText;
    }

    public void setOnChangeListener(AutoFormatTextChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    @BindingAdapter("android:text")
    public static void setTextAndroid(AbstractAutoEditText editText, String newText) {
        editText.setNewText(newText);
    }

    @BindingAdapter("text")
    public static void setText(AbstractAutoEditText editText, String newText) {
        editText.setNewText(newText);
    }

    @BindingAdapter("unformattedText")
    public static void setUnformattedText(AbstractAutoEditText editText, String unformattedText) {
        editText.setUnformattedText(unformattedText);
    }

    @InverseBindingAdapter(attribute = "unformattedText", event = "android:textAttrChanged")
    public static String getText(AbstractAutoEditText editText) {
        return editText.getUnformattedText();
    }

    public interface AutoFormatTextChangeListener {
        void onTextChanged(String unformattedText, String formattedText, int position);
    }

    static class EditTextState {
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