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

    public boolean hideModeEnabled;
    private String textBefore, formattedText, unformattedText;

    public FormattedInputEditText(Context context) {
        super(context);
        init(context, null);
    }

    public FormattedInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public abstract String getHideModeText(String unformattedText, String formattedText);
    public abstract EditTextState format(TextChangeEvent textChangeEvent);

    public void init(Context context, AttributeSet attrs) {
        //Prevents edge case where multiple callbacks are occurring for input type 'text'
        if (getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE) ||
                getInputType() == InputType.TYPE_CLASS_TEXT || getInputType() == InputType.TYPE_TEXT_FLAG_MULTI_LINE) {
            setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }

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
    }

    public void setHideModeEnabled(boolean enabled) {
        this.hideModeEnabled = enabled;

        if (enabled) {
            updateHideModeText();
        }else {
            setNewText(unformattedText);
        }
    }

    public void updateHideModeText() {
        String s = getHideModeText(unformattedText, formattedText);
        setTextNoFormat(s);
    }

    public void setFormattingEnabled(boolean shouldListen) {
        removeTextChangedListener(textWatcher);

        if (shouldListen) {
            addTextChangedListener(textWatcher);
        }
    }

    //Call is required by a subclass before this class will call format method
    public void startFormatting() {
        setFormattingEnabled(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeTextChangedListener(textWatcher);
        textWatcher = null;
    }

    private void handleOnTextChanged(CharSequence s, int start, int before, int count) {
        if (hideModeEnabled) {
            setTextNoFormat(textBefore);
            setSelection(start + before);
            return;
        }

        TextChangeEvent textChangeEvent = new TextChangeEvent(textBefore, s.toString(), start, before, count);
        EditTextState newEditTextState = format(textChangeEvent);

        if (newEditTextState == null) {
            setTextNoFormat(textBefore);
            setSelection(start + before);
            return;
        }

        if (newEditTextState != null) {
            boolean formattedTextChanged = formattedText == null || !formattedText.equals(newEditTextState.getFormattedText());
            if (formattedTextChanged) {
                formattedText = newEditTextState.getFormattedText();
            }

            boolean unformattedTextChanged= unformattedText == null || !unformattedText.equals(newEditTextState.getUnformattedText());
            if (unformattedTextChanged) {
                unformattedText = newEditTextState.getUnformattedText();
            }

            setTextNoFormat(formattedText); //Unformatted text must be set beforehand

            //When we set the text programmatically, the cursor returns to position 0. This repositions the cursor/selection
            setSelection(newEditTextState.getSelectionStart(), newEditTextState.getSelectionEnd());

            if (changeListener != null &&
                    (formattedTextChanged || unformattedTextChanged)) {
                changeListener.onValueChanged(unformattedText, formattedText);
            }
        }
    }

    public void setTextNoFormat(String s) {
        removeTextChangedListener(textWatcher); //Removing/re-adding listener will prevent never ending loop
        setNewText(s);
        addTextChangedListener(textWatcher);
    }

    public void setNewText(CharSequence s) {
        if (s != null && getText() != null &&
                !getText().toString().equals(s.toString())) {
            setText(s);
        }
    }

    public ChangeListener getChangeListener() {
        return changeListener;
    }

    public TextWatcher getTextWatcher() {
        return textWatcher;
    }

    public boolean getHideModeEnabled() {
        return hideModeEnabled;
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
    public static void setTextAndroid(FormattedInputEditText editText, String newText) {
        editText.setNewText(newText);
    }

    @BindingAdapter("hideModeEnabled")
    public static void setHideModeEnabled(AutoFormatEditText editText, boolean enabled) {
        editText.setHideModeEnabled(enabled);
    }

    public interface ChangeListener {
        void onValueChanged(String unformattedValue, String formattedValue);
    }
}