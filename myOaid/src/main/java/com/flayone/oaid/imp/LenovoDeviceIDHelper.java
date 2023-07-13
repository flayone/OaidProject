package com.flayone.oaid.imp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.IBinder;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.IDGetterAction;
import com.flayone.oaid.interfaces.LenovoIDInterface;

import mymsa.com.zui.deviceidservice.IDeviceidInterface;

/**
 * 联想手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:25
 */
public class LenovoDeviceIDHelper implements IDGetterAction {

    private final Context mContext;
    AppIdsUpdater _listener;

    public LenovoDeviceIDHelper(Context ctx) {
        mContext = ctx;
    }

    @Override
    public boolean supported() {
        if (mContext == null) {
            return false;
        }
        try {
            PackageInfo pi = mContext.getPackageManager().getPackageInfo("com.zui.deviceidservice", 0);
            return pi != null;
        } catch (Exception e) {
//            OAIDLog.print(e);
            return false;
        }
    }

    @Override
    public void getID(final AppIdsUpdater _listener) {
        try {
            this._listener = _listener;
            if (mContext == null) {
                if (_listener != null) {
                    _listener.OnIdsAvalid("");
                }
                return;
            }
            Intent intent = new Intent();
            intent.setClassName("com.zui.deviceidservice", "com.zui.deviceidservice.DeviceidService");
            boolean seu = mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                LenovoIDInterface lenovoIDInterface = new LenovoIDInterface.Proxy(service);
                //其他获取方式
                IDeviceidInterface anInterface = IDeviceidInterface.Stub.asInterface(service);

                if (lenovoIDInterface == null) {
                    if (anInterface == null) {
                        if (_listener != null) {
                            _listener.OnIdsAvalid("");
                        }
                        mContext.unbindService(serviceConnection);
                        return;
                    }
                    try {
                        String oaid = anInterface.getOAID();
                        if (_listener != null) {
                            _listener.OnIdsAvalid(oaid);
                        }
                    } catch (Throwable e) {
                        if (_listener != null) {
                            _listener.OnIdsAvalid("");
                        }
                        e.printStackTrace();
                    }
                    mContext.unbindService(serviceConnection);
                    return;
                }
                String oaid = lenovoIDInterface.a();
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
