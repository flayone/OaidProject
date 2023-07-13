package com.flayone.oaid.imp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.IBinder;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.IDGetterAction;

import mymsa.com.google.android.gms.ads.identifier.internal.IAdvertisingIdService;

//google 广告id获取
public class GMSADIDHelper implements IDGetterAction {
    private final Context context;

    public GMSADIDHelper(Context context) {
        this.context = context;
    }

    @Override
    public boolean supported() {
        if (context == null) {
            return false;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo("com.android.vending", 0);
            return pi != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void getID(final AppIdsUpdater _listener) {
        try {
            if (context == null) {
                if (_listener != null) {
                    _listener.OnIdsAvalid("");
                }
                return;
            }

            Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
            intent.setPackage("com.google.android.gms");
            context.bindService(intent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    try {
                        IAdvertisingIdService anInterface = IAdvertisingIdService.Stub.asInterface(service);
                        if (anInterface.isLimitAdTrackingEnabled(true)) {
                            // 实测在系统设置中停用了广告化功能也是能获取到广告标识符的
                            //                        OAIDLog.print("User has disabled advertising identifier");
                        }
                        String adid = anInterface.getId();

                        if (_listener != null) {
                            _listener.OnIdsAvalid(adid);
                        }
                        context.unbindService(this);

                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    try {
                        if (_listener != null) {
                            _listener.OnIdsAvalid("");
                        }
                        context.unbindService(this);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }, Context.BIND_AUTO_CREATE);
        } catch (Throwable e) {
            e.printStackTrace();
        }


    }
}
