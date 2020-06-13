package com.ibm.autoformatedittext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import com.ibm.autoformatedittext.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        FormatObservable observable = new FormatObservable();
        observable.format.set("+1(###) ###-####");
        binding.setObservable(observable);
    }
}
