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

public class AutoFormatEditText extends AppCompatEditText {
    private AutoFormatTextChangeListener changeListener;
    private BaseFormattedTextBuilder formattedTextBuilder;
    private TextWatcher textWatcher;
    private boolean textChangeActive;
    private String rawText = "";

    public AutoFormatEditText(Context context) {
        super(context);
    }

    public AutoFormatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, null);
    }

    public AutoFormatEditText(Context context, AttributeSet attrs, int defStyle) {
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
            a = context.obtainStyledAttributes(attrs, R.styleable.AutoFormatEditText, defStyle, 0);
        }else {
            a = context.obtainStyledAttributes(attrs, R.styleable.AutoFormatEditText);
        }

        String maskString = a.getString(R.styleable.AutoFormatEditText_mask);
        String maskPlaceholder = a.getString(R.styleable.AutoFormatEditText_mask_placeholder);
        formattedTextBuilder = getMaskedTextBuilder(maskString, maskPlaceholder);

        CharSequence text = a.getText(R.styleable.AutoFormatEditText_android_text);
        if (text != null && text.length() > 0) {
            setNewText(text);
        }

        a.recycle();

        //Prevents edge case where multiple callbacks are occurring for input from edit texts with suggestions
        if (getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE)) {
            setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    private void setUpTextWatcher() {
        removeTextChangedListener(textWatcher);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                textChangeActive = true;
                formattedTextBuilder.setTextBeforeChange(s.toString());
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

    protected BaseFormattedTextBuilder getMaskedTextBuilder(String maskString, String maskPlaceholder) {
        return MaskedTextBuilder.newInstance(maskString, maskPlaceholder);
    }

    private void handleOnTextChanged(CharSequence s, int start, int before, int count) {
        if (textChangeActive) {
            formattedTextBuilder.setTextAfterChange(s.toString())
                    .setSelectionStart(start)
                    .setSelectionLength(before)
                    .setReplacementLength(count);
        }
    }

    private void handleAfterTextChanged() {
        removeTextChangedListener(textWatcher); //Removing/re-adding listener will prevent never ending loop

        BaseFormattedTextBuilder.EditTextState editTextState = formattedTextBuilder.build();
        rawText = editTextState.getUnmaskedText(); //New raw text
        setText(editTextState.getMaskedText()); //Set new edit text string
        setSelection(editTextState.getCursorPos()); //Setting text programmatically resets the cursor, so this will reposition it

        if (changeListener != null) {
            changeListener.onTextChanged(rawText,
                    editTextState.getMaskedText(),
                    editTextState.getCursorPos());
        }

        addTextChangedListener(textWatcher);
        textChangeActive = false;
    }

    public void setMask(String maskString) {
        formattedTextBuilder.setMask(maskString);
        setText(rawText); //Re-masking after changing mask string value
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
    public static void setText(AutoFormatEditText editText, String text) {
        editText.setNewText(text);
    }

    @BindingAdapter("rawText")
    public static void setRawText(AutoFormatEditText editText, String rawText) {
        editText.setNewText(rawText);
    }

    //This works but is still experimental. Does not work properly if the mask is shorter than the masked text
    @BindingAdapter("mask")
    public static void setMask(AutoFormatEditText editText, String maskString) {
        editText.setMask(maskString);
    }

    @InverseBindingAdapter(attribute = "rawText", event = "android:textAttrChanged")
    public static String getText(AutoFormatEditText editText) {
        return editText.getRawText();
    }

    public interface AutoFormatTextChangeListener {
        void onTextChanged(String rawValue, String maskedValue, int position);
    }
}