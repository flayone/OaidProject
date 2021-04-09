package com.lyy.oaidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.mercury.sdk.core.config.MercuryAD;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String oaid = MercuryAD.getOAID();
        Toast.makeText(this, "oaid = " + oaid, Toast.LENGTH_LONG).show();
    }
}