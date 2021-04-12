package com.lyy.oaidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.OAIDHelper;
import com.mercury.sdk.core.config.MercuryAD;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String oaid = MercuryAD.getOAID();
        Log.d("MercuryAD.getOAID()", "oaid = " + oaid);

        OAIDHelper.getOAid(this, new AppIdsUpdater() {
            @Override
            public void OnIdsAvalid(@NonNull String ids) {
                Log.d("OnIdsAvalid", "oaid = " + ids);

                Toast.makeText(MainActivity.this, "oaid = " + ids, Toast.LENGTH_LONG).show();
            }
        });
    }
}