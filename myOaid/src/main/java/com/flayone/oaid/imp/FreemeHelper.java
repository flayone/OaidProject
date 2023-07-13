package com.flayone.oaid.imp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.IBinder;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.IDGetterAction;

import mymsa.com.android.creator.IdsSupplier;

public class FreemeHelper implements IDGetterAction {
    private final Context context;

    public FreemeHelper(Context context) {
        this.context = context;
    }

    @Override
    public boolean supported() {
        if (context == null) {
            return false;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo("com.android.creator", 0);
            return pi != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void getID(final AppIdsUpdater _listener) {
        if (context == null) {
            if (_listener != null) {
                _listener.OnIdsAvalid("");
            }
            return;
        }
        Intent intent = new Intent("android.service.action.msa");
        intent.setPackage("com.android.creator");
        context.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    IdsSupplier anInterface = IdsSupplier.Stub.asInterface(service);

                    if (_listener != null) {
                        String result = "";
                        if (anInterface != null) {
                            result = anInterface.getOAID();
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
    }

}
