package com.ibm.autoformatedittext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.ibm.autoformatedittext.databinding.ActivityMainBinding;
import com.ibm.autoformatedittext.model.EditTextState;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        final FormatObservable observable = new FormatObservable();
        binding.setObservable(observable);

//        binding.phoneNumberAutoFormatEditText.setMaskingInputFilter(
//                new FormattedEditText.MaskingInputFilter() {
//                    @Override
//                    public EditTextState filter(String textBefore, String textAfter, int selectionStart, int selectionLength, int replacementLength) {
//                        String newText = textAfter.length() > textBefore.length() ? textAfter + "-" : textAfter;
//                        return new EditTextState(newText, newText, newText.length());
//                    }
//                });
    }
}
