package com.lyy.oaidproject;

import android.app.Application;

import com.flayone.oaid.MyOAID;
import com.mercury.sdk.core.config.MercuryAD;

public class DemoApplication extends Application {

    public static final String APP_KEY = "e1d0d3aaf95d3f1980367e75bc41141d";
    public static final String APP_ID = "100171";

    @Override
    public void onCreate() {
        super.onCreate();

        //MercurySDK 初始化，MercurySDK会初始化msa SDK，并保存获取到的oaid值
        MercuryAD.initSDK(this, APP_ID, APP_KEY);

        //初始化代码获取的oaid值
        MyOAID.init(this);
    }
}
