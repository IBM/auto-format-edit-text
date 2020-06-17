package com.ibm.autoformatedittext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;

import android.os.Bundle;
import android.util.Log;

import com.ibm.autoformatedittext.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        final FormatObservable observable = new FormatObservable();
        observable.format.set("+1(###) ###-####");
        binding.setObservable(observable);

//        binding.phoneNumberAutoFormatEditText.setOnChangeListener(
//                new AbstractAutoEditText.AutoFormatTextChangeListener() {
//                    @Override
//                    public void onUnformattedTextChanged(String text) {
//                        Log.i("XXX", text);
//                    }
//        });

//        observable.formattedText.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable sender, int propertyId) {
//                Log.i("XXX", observable.formattedText.get());
//            }
//        });


    }
}
