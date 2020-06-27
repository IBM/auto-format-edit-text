package com.ibm.autoformatedittext;

import android.util.Log;

import androidx.databinding.ObservableField;

public class FormatObservable {
    public ObservableField<String> formattedText = new ObservableField<>();
    public ObservableField<String> unformattedText = new ObservableField<>();
    public ObservableField<String> inputMask = new ObservableField<>();

    public void onUnformattedValueChanged(String value) {
        unformattedText.set(value);
        Log.i("Unformatted", value + ":");
    }

    public void onTextChanged(String text) {
        Log.i("Formatted", text + ":");
    }
}
