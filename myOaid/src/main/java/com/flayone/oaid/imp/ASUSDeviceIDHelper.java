package com.flayone.oaid.imp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.IBinder;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.ASUSIDInterface;
import com.flayone.oaid.interfaces.IDGetterAction;

/**
 * 华硕手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:24
 */
public class ASUSDeviceIDHelper implements IDGetterAction {

    private final Context mContext;

    public ASUSDeviceIDHelper(Context ctx) {
        mContext = ctx;
    }

    AppIdsUpdater _listener;

    @Override
    public boolean supported() {
        if (mContext == null) {
            return false;
        }
        try {
            PackageInfo pi = mContext.getPackageManager().getPackageInfo("com.android.creator", 0);
            return pi != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取 OAID 并回调
     *
     * @param _listener
     */
    @Override
    public void getID(AppIdsUpdater _listener) {
        this._listener = _listener;
        try {
            if (mContext == null) {
                if (_listener != null) {
                    _listener.OnIdsAvalid("");
                }
                return;
            }
            Intent intent = new Intent();
            intent.setAction("com.asus.msa.action.ACCESS_DID");
            ComponentName componentName = new ComponentName("com.asus.msa.SupplementaryDID", "com.asus.msa.SupplementaryDID.SupplementaryDIDService");
            intent.setComponent(componentName);

            boolean isBin = mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {

                ASUSIDInterface.ASUSID asusID = new ASUSIDInterface.ASUSID(service);
                String asusOAID = asusID.getID();
                if (_listener != null) {
                    _listener.OnIdsAvalid(asusOAID);
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