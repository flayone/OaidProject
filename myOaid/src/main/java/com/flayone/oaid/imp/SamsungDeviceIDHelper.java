package com.flayone.oaid.imp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.IBinder;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.IDGetterAction;
import com.flayone.oaid.interfaces.SamsungIDInterface;

/**
 * 三星手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:28
 */
public class SamsungDeviceIDHelper implements IDGetterAction {

    private final Context mContext;
    AppIdsUpdater _listener;

    public SamsungDeviceIDHelper(Context ctx) {
        mContext = ctx;
    }


    @Override
    public boolean supported() {
        if (mContext == null) {
            return false;
        }
        try {
            PackageInfo pi = mContext.getPackageManager().getPackageInfo("com.samsung.android.deviceidservice", 0);
            return pi != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void getID(final AppIdsUpdater _listener) {
        this._listener = _listener;
        if (mContext == null) {
            if (_listener != null) {
                _listener.OnIdsAvalid("");
            }
            return;
        }
        try {

            Intent intent = new Intent();
            intent.setClassName("com.samsung.android.deviceidservice", "com.samsung.android.deviceidservice.DeviceIdService");
            mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                SamsungIDInterface.Proxy proxy = new SamsungIDInterface.Proxy(service);       // 在这里有区别，需要实际验证
                String oaid = proxy.getID();
                if (_listener != null) {
                    _listener.OnIdsAvalid(oaid);
                }
                mContext.unbindService(serviceConnection);
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
                mContext.unbindService(serviceConnection);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    };
}
