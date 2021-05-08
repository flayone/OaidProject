package com.lyy.oaidproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.MyOAID;
import com.flayone.oaid.OAIDHelper;

public class MainActivity extends AppCompatActivity {

    TextView oaidTextCode;
    TextView oaidTextCodeSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oaidTextCodeSaved = findViewById(R.id.codeSaved);
        oaidTextCode = findViewById(R.id.code);


        //在init时已经获取了oaid值，如果有返回，此时会直接拿到本地存储的oaid值，不需等待。当APP第一次启动时，部分机型可能无法获取到，之后启动如果有值均能快速拿到。
        oaidTextCodeSaved.setText("oaid 存储值：\n " + MyOAID.getOAID(this));

        //实时值部分机型上由于是异步的，获取的比较慢，不建议直接使用此方式获取
        OAIDHelper.getOAid(this, new AppIdsUpdater() {
            @Override
            public void OnIdsAvalid(@NonNull final String id) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        oaidTextCode.setText("oaid 实时值：\n " + id);

                    }
                });
            }
        });


    }
}