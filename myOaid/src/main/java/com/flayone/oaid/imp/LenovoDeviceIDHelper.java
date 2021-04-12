package com.flayone.oaid.imp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.LenovoIDInterface;

/**
 * 联想手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:25
 */
public class LenovoDeviceIDHelper {

    private Context mContext;
    LenovoIDInterface lenovoIDInterface;

    public LenovoDeviceIDHelper(Context ctx) {
        mContext = ctx;
    }

    public void getIdRun(AppIdsUpdater _listener) {
        try {
            String result = null;
            String pkgName = mContext.getPackageName();
            Intent intent = new Intent();
            intent.setClassName("com.zui.deviceidservice", "com.zui.deviceidservice.DeviceidService");
            boolean seu = mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            if (seu) {
                if (lenovoIDInterface != null) {
                    String oaid = lenovoIDInterface.a();
                    String udid = lenovoIDInterface.b();
                    String aaid = lenovoIDInterface.b(pkgName);
                    String vaid = lenovoIDInterface.b(pkgName);

                    if (_listener != null) {
                        _listener.OnIdsAvalid(oaid);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            lenovoIDInterface = new LenovoIDInterface.Proxy(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
