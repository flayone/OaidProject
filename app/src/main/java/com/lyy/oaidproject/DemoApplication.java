package com.lyy.oaidproject;

import android.app.Application;

import com.mercury.sdk.core.config.MercuryAD;

public class DemoApplication extends Application {

    public static final String APP_KEY = "e1d0d3aaf95d3f1980367e75bc41141d";
    public static final String APP_ID = "100171";

    @Override
    public void onCreate() {
        super.onCreate();

        //MercurySDK 初始化
        MercuryAD.initSDK(this, APP_ID, APP_KEY);
        //设置debug状态
        MercuryAD.setDebug(true);
        //是否支持素材提前加载，默认false不支持
        MercuryAD.needPreLoadMaterial(true);
        //自定义debug下的tag标志
//        MercuryAD.setDebugTag("MercuryDemo");
    }
}
