package com.ibm.autoformatedittext;

import androidx.databinding.ObservableField;

public class MaskObservable {
    public ObservableField<String> maskedText = new ObservableField<>();
    public ObservableField<String> rawText = new ObservableField<>();
    public ObservableField<String> mask = new ObservableField<>();
}
