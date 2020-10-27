package com.ibm.autoformatedittext;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableField;

import java.util.Objects;

public class FormatObservable {
    public ObservableField<String> formattedText = new ObservableField<>("");
    public ObservableField<String> unformattedText = new ObservableField<>("");
    public ObservableField<String> inputMask = new ObservableField<>("(###) ###-####");
    public ObservableField<String> hideModeFormat = new ObservableField<>("([0]**) ***-[6-9]");
    public ObservableField<Boolean> inputEnabled = new ObservableField<>(true);
    public ObservableField<Boolean> shiftModeEnabled = new ObservableField<>(false);
    public ObservableField<Boolean> hideModeEnabled = new ObservableField<>(false);

    public void onValueChanged(String unformattedText, String formattedText) {
        this.unformattedText.set(unformattedText);
        this.formattedText.set(formattedText);
    }

    public void onToggleInputEnabledClick(View v) {
        inputEnabled.set(!inputEnabled.get());
    }

    public void onShiftModeToggleClick(View v) {
        shiftModeEnabled.set(!shiftModeEnabled.get());
    }

    public void onHideModeToggleClick(View v) {
        hideModeEnabled.set(!hideModeEnabled.get());
    }
}