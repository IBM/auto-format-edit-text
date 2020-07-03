package com.ibm.autoformatedittext;

import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableField;

import java.util.Objects;

public class FormatObservable {
    public ObservableField<String> formattedText = new ObservableField<>();
    public ObservableField<String> unformattedText = new ObservableField<>();
    public ObservableField<String> inputMask = new ObservableField<>("(###) ###-####");
    public ObservableField<String> staticFormat = new ObservableField<>("([0]**) ***-[6-9]");
    public ObservableField<Boolean> enabled = new ObservableField<>(true);

    public void onUnformattedValueChanged(String value) {
        unformattedText.set(value);
        Log.i("Unformatted", value + "...");
    }

    public void onTextChanged(String text) {
        Log.i("Formatted", text + "...");
    }

    public void onToggleEnableClick(View v) {
        enabled.set(!Objects.requireNonNull(enabled.get()));
    }
}
