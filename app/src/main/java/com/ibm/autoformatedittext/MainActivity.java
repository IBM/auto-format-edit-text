package com.ibm.autoformatedittext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.ibm.autoformatedittext.databinding.ActivityMainBinding;
import com.ibm.autoformatedittext.model.EditTextState;

public class MainActivity extends AppCompatActivity {
    public FormatObservable observable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        observable = new FormatObservable();
        binding.setObservable(observable);
    }
}
