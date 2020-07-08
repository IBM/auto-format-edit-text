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

@BindingMethods({
        @BindingMethod(type = FormattedEditText.class, attribute = "onFormattedValueChanged", method = "setFormattedValueChangedListener"),
        @BindingMethod(type = FormattedEditText.class, attribute = "onUnformattedValueChanged", method = "setUnformattedValueChangedListener")
})
public class FormattedEditText extends AppCompatEditText {
    private UnformattedValueListener onUnformattedValueListener;
    private FormattedValueListener onFormattedValueListener;
    private MaskingInputFilter maskingInputFilter;

    private TextWatcher textWatcher;

    public boolean staticModeEnabled;
    private String textBefore;
    private String formattedText, unformattedText;

    public FormattedEditText(Context context) {
        super(context);
        init(context, null);
    }

    public FormattedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {
        setUpTextWatcher();

        //Prevents edge case where multiple callbacks are occurring for input type 'text'
        if (getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE) ||
                getInputType() == InputType.TYPE_CLASS_TEXT || getInputType() == InputType.TYPE_TEXT_FLAG_MULTI_LINE) {
            setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    //public EditTextState format(String textBefore, String textAfter, int selectionStart, int selectionLength, int replacementLength);
    public String getStaticReplacement(String unformattedText) {
        return unformattedText;
    }

    public void setStaticModeEnabled(boolean enabled) {
        this.staticModeEnabled = enabled;

        if (enabled) {
            setTextNoWatch(getStaticReplacement(unformattedText));
        }else {
            setNewText(unformattedText);
        }
    }

    public void refreshStaticReplacement() {
        if (staticModeEnabled) {
            setTextNoWatch(getStaticReplacement(unformattedText));
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
        if (staticModeEnabled) {
            setTextNoWatch(textBefore);
            setSelection(selectionStart + selectionLength);
            return;
        }

        if (maskingInputFilter != null) {
            EditTextState newEditTextState = maskingInputFilter.filter(textBefore, s.toString(), selectionStart, selectionLength, replacementLength);

            if (formattedText == null || !formattedText.equals(newEditTextState.getFormattedText())) {
                formattedText = newEditTextState.getFormattedText();
                if (onFormattedValueListener != null) {
                    onFormattedValueListener.onFormattedValueChanged(formattedText);
                }
            }

            if (unformattedText == null || !unformattedText.equals(newEditTextState.getUnformattedText())) {
                unformattedText = newEditTextState.getUnformattedText();
                if (onUnformattedValueListener != null) {
                    onUnformattedValueListener.onUnformattedValueChanged(unformattedText);
                }
            }

            setTextNoWatch(formattedText);

            //Setting text programmatically resets the cursor, so this will reposition it
            setSelection(newEditTextState.getCursorStart(), newEditTextState.getCursorEnd());
        }
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

    public void setFormattedValueChangedListener(FormattedValueListener listener) {
        onFormattedValueListener = listener;
    }

    public void setMaskingInputFilter(MaskingInputFilter maskingInputFilter) {
        this.maskingInputFilter = maskingInputFilter;
    }

    @BindingAdapter("android:text")
    public static void setTextAndroid(FormattedEditText editText, String newText) {
        editText.setNewText(newText);
    }

    @BindingAdapter("staticModeEnabled")
    public static void setStaticModeEnabled(AutoFormatEditText editText, boolean enabled) {
        editText.setStaticModeEnabled(enabled);
    }

    public interface UnformattedValueListener {
        void onUnformattedValueChanged(String value);
    }

    public interface FormattedValueListener {
        void onFormattedValueChanged(String text);
    }

    public interface MaskingInputFilter {
        EditTextState filter(String textBefore, String textAfter,
                             int selectionStart, int selectionLength, int replacementLength);
    }
}