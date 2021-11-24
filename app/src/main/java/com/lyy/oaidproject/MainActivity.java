package com.lyy.oaidproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flayone.oaid.MyOAID;
import com.flayone.oaid.ResultCallBack;

public class MainActivity extends AppCompatActivity {

    TextView MsaOaid;
    TextView MyOaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyOaid = findViewById(R.id.myoaid);
        MsaOaid = findViewById(R.id.msaoaid);

        getOAID();
    }

    private void getOAID() {

        //MyOaid SDK方式(推荐)
        MyOAID.getOAID(this, new ResultCallBack() {
            @Override
            public void onResult(final String oaid) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        MyOaid.setText("MyOaid SDK获取的Oaid值：\n " + oaid);
                    }
                });
            }
        });

        //msa SDK方式
        new MsaOaidHelper(new MsaOaidHelper.OaidUpdater() {
            @Override
            public void IdReceived(@NonNull final String id) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        MsaOaid.setText("Msa SDK获取的Oaid值：\n " + id);
                    }
                });
            }
        }).getDeviceIds(this);
    }

    public void refresh(View view) {
        getOAID();
    }
}