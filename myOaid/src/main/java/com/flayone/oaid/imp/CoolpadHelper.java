package com.flayone.oaid.imp;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.IBinder;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.IDGetterAction;

import mymsa.com.coolpad.deviceidsupport.IDeviceIdManager;

// 酷派手机
public class CoolpadHelper implements IDGetterAction {
    private final Context context;

    public CoolpadHelper(Context context) {
        if (context instanceof Application) {
            this.context = context;
        } else {
            this.context = context.getApplicationContext();
        }
    }

    @Override
    public boolean supported() {
        if (context == null) {
            return false;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo("com.coolpad.deviceidsupport", 0);
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
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.coolpad.deviceidsupport", "com.coolpad.deviceidsupport.DeviceIdService"));

            context.bindService(intent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    try {
                        IDeviceIdManager anInterface = IDeviceIdManager.Stub.asInterface(service);
                        if (_listener != null) {
                            String result = "";
                            if (anInterface != null) {
                                result = anInterface.getOAID(context.getPackageName());
                            }
                            _listener.OnIdsAvalid(result);
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
