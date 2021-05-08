package com.lyy.oaidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.OAIDHelper;
import com.mercury.sdk.core.config.MercuryAD;

public class MainActivity extends AppCompatActivity {

    TextView oaidTextSDK;
    TextView oaidTextCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oaidTextSDK = findViewById(R.id.sdk);
        oaidTextCode = findViewById(R.id.code);

        String oaid = MercuryAD.getOAID();
        Log.d("MercuryAD.getOAID()", "oaid = " + oaid);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OAIDHelper.getOAid(MainActivity.this, new AppIdsUpdater() {
//                    @Override
//                    public void OnIdsAvalid(@NonNull String ids) {
//                        Log.d("OnIdsAvalid", "oaid = " + ids);
//
//                        Toast.makeText(MainActivity.this, "oaid = " + ids, Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        }).start();
        oaidTextSDK.setText("MercuryAD.getOAID() = "+ MercuryAD.getOAID());

        OAIDHelper.getOAid(this, new AppIdsUpdater() {
            @Override
            public void OnIdsAvalid(@NonNull final String ids) {
                Log.d("OnIdsAvalid", "oaid = " + ids);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(MainActivity.this, "oaid = " + ids, Toast.LENGTH_LONG).show();

                        oaidTextCode.setText("Code getOAid= "+ids);
                    }
                });
            }
        });
    }
}