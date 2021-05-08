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
import com.flayone.oaid.MyOAID;
import com.flayone.oaid.OAIDHelper;
import com.mercury.sdk.core.config.MercuryAD;

public class MainActivity extends AppCompatActivity {

    TextView oaidTextSDK;
    TextView oaidTextCode;
    TextView oaidTextSDKSaved;
    TextView oaidTextCodeSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oaidTextCodeSaved = findViewById(R.id.codeSaved);
        oaidTextCode = findViewById(R.id.code);
        oaidTextSDKSaved = findViewById(R.id.sdkSaved);
        oaidTextSDK = findViewById(R.id.sdk);


        oaidTextCodeSaved.setText("oaid源码存储值：\n " + MyOAID.getOAID(this));

        oaidTextSDKSaved.setText("msa sdk存储值：\n " + MercuryAD.getOAID());

        OAIDHelper.getOAid(this, new AppIdsUpdater() {
            @Override
            public void OnIdsAvalid(@NonNull final String id) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        oaidTextCode.setText("oaid源码实时值：\n " + id);

                    }
                });
            }
        });

        new MsaOaidHelper(new MsaOaidHelper.OaidUpdater() {
            @Override
            public void IdReceived(@NonNull final String id) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        oaidTextSDK.setText("msa sdk实时值：\n " + id);

                    }
                });
            }
        }).getDeviceIds(this);

    }
}