package com.ibm.autoformatedittext;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

import com.ibm.autoformatedittext.model.EditTextState;
import com.ibm.autoformatedittext.util.TextChangeEvent;

@SuppressWarnings("unused")
@BindingMethods({
        @BindingMethod(type = FormattedInputEditText.class, attribute = "onValueChanged", method = "setChangeListener"),
})
public abstract class FormattedInputEditText extends AppCompatEditText {
    private ChangeListener changeListener;
    private TextWatcher textWatcher;

    private String textBefore;
    public String formattedText, unformattedText;

    public FormattedInputEditText(Context context) {
        super(context);
        init(context, null);
    }

    public FormattedInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public abstract EditTextState format(TextChangeEvent textChangeEvent);
    public abstract boolean formattingEnabled();

    public void init(Context context, AttributeSet attrs) {
        //Prevents edge case where multiple callbacks are occurring for input type 'text'
        if (getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE) ||
                getInputType() == InputType.TYPE_CLASS_TEXT || getInputType() == InputType.TYPE_TEXT_FLAG_MULTI_LINE) {
            setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }

        startWatching();
    }

    private void startWatching() {
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

    private void handleOnTextChanged(CharSequence s, int start, int before, int count) {
        if (!formattingEnabled()) {
            return;
        }

        TextChangeEvent textChangeEvent = new TextChangeEvent(textBefore, s.toString(), start, before, count);
        EditTextState editTextState = format(textChangeEvent);

        if (editTextState == null) {
            setTextNoFormat(textBefore);
            setSelection(start + before);
            return;
        }

        boolean formattedTextChanged = formattedText == null || !formattedText.equals(editTextState.getFormattedText());
        if (formattedTextChanged) {
            formattedText = editTextState.getFormattedText();
        }

        boolean unformattedTextChanged = unformattedText == null || !unformattedText.equals(editTextState.getUnformattedText());
        if (unformattedTextChanged) {
            unformattedText = editTextState.getUnformattedText();
        }

        setTextNoFormat(formattedText); //Unformatted text must be set beforehand

        //When we set the text programmatically, the cursor returns to position 0. This repositions the cursor/selection
        setSelection(editTextState.getSelectionStart(), editTextState.getSelectionEnd());

        if (changeListener != null &&
                (formattedTextChanged || unformattedTextChanged)) {
            changeListener.onValueChanged(unformattedText, formattedText);
        }
    }

    public void setTextNoFormat(String s) {
        removeTextChangedListener(textWatcher); //Removing/re-adding listener will prevent never ending loop
        if (s != null && !s.equals(getText().toString())) {
            setText(s);
        }
        addTextChangedListener(textWatcher);
    }

    public ChangeListener getChangeListener() {
        return changeListener;
    }

    public TextWatcher getTextWatcher() {
        return textWatcher;
    }

    public String getUnformattedText() {
        return unformattedText;
    }

    public String getFormattedText() {
        return formattedText;
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    @BindingAdapter("android:text")
    public static void setTextAndroid(FormattedInputEditText editText, String s) {
        if (s != null && !s.equals(editText.getText().toString())) {
            editText.setText(s);
        }
    }

    public interface ChangeListener {
        void onValueChanged(String unformattedValue, String formattedValue);
    }
}