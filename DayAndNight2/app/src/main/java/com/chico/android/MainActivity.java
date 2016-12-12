package com.chico.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    private SwitchCompat nightCoverCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nightCoverCompat = (SwitchCompat) findViewById(R.id.nightCoverCompat);

        nightCoverCompat.setChecked(PreferencesUtils.getBoolean(this,Constant.ISNIGHT,false));
        nightCoverCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    PreferencesUtils.putBoolean(MainActivity.this,Constant.ISNIGHT, true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    PreferencesUtils.putBoolean(MainActivity.this,Constant.ISNIGHT, false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                recreate();
            }
        });
    }
}
