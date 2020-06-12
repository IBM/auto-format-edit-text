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

public abstract class BaseAutoFormatEditText extends AppCompatEditText {
    private AutoFormatTextChangeListener changeListener;
    private TextWatcher textWatcher;
    private boolean textChangeActive;
    private String rawText = "";

    private String textBefore, textAfter;
    private int selectionStart, selectionLength, replacementLength;

    public BaseAutoFormatEditText(Context context) {
        super(context);
    }

    public BaseAutoFormatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, null);
    }

    public BaseAutoFormatEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, Integer defStyle) {
        if (attrs == null) {
            return;
        }

        setUpTextWatcher();

        TypedArray a;
        if (defStyle != null) {
            a = context.obtainStyledAttributes(attrs, R.styleable.BaseAutoFormatEditText, defStyle, 0);
        }else {
            a = context.obtainStyledAttributes(attrs, R.styleable.BaseAutoFormatEditText);
        }

        String maskString = a.getString(R.styleable.BaseAutoFormatEditText_mask);
        String maskPlaceholder = a.getString(R.styleable.BaseAutoFormatEditText_mask_placeholder);
        setMask(maskString, maskPlaceholder);

        CharSequence text = a.getText(R.styleable.BaseAutoFormatEditText_android_text);
        if (text != null && text.length() > 0) {
            setNewText(text);
        }

        a.recycle();

        //Prevents edge case where multiple callbacks are occurring for input from edit texts with suggestions
        if (getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE)) {
            setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    abstract void setMask(String maskString, String maskPlaceholder);

    abstract EditTextState format(String textBefore, String textAfter, int selectionStart, int selectionLength, int replacementLength);
    abstract void updateMaskString(String mask);

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
        setSelection(newEditTextState.getCursorPos()); //Setting text programmatically resets the cursor, so this will reposition it

        if (changeListener != null) {
            changeListener.onTextChanged(rawText,
                    newEditTextState.getMaskedText(),
                    newEditTextState.getCursorPos());
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

    public void reapplyMask() {
        setText(rawText); //Re-masking after changing mask string value
    }

    String getRawText() {
        return rawText;
    }

    public void setOnChangeListener(AutoFormatTextChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    @BindingAdapter("android:text")
    public static void setText(BaseAutoFormatEditText editText, String text) {
        editText.setNewText(text);
    }

    @BindingAdapter("rawText")
    public static void setRawText(BaseAutoFormatEditText editText, String rawText) {
        editText.setNewText(rawText);
    }

    //This works but is still experimental. Does not work properly if the mask is shorter than the masked text
    @BindingAdapter("mask")
    public static void setMask(BaseAutoFormatEditText editText, String maskString) {
        editText.updateMaskString(maskString);
        editText.reapplyMask();
    }

    @InverseBindingAdapter(attribute = "rawText", event = "android:textAttrChanged")
    public static String getText(BaseAutoFormatEditText editText) {
        return editText.getRawText();
    }

    public interface AutoFormatTextChangeListener {
        void onTextChanged(String rawValue, String maskedValue, int position);
    }

    static class EditTextState {
        private String unmaskedText, maskedText;
        private int cursorPos;

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

        int getCursorPos() {
            return cursorPos;
        }

        void setCursorPos(int cursorPos) {
            this.cursorPos = cursorPos;
        }
    }
}