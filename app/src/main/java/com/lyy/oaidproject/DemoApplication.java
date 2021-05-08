package com.lyy.oaidproject;

import android.app.Application;

import com.flayone.oaid.MyOAID;

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化代码获取的oaid值
        MyOAID.init(this);
    }
}
