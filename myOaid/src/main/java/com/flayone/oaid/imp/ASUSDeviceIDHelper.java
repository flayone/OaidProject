package com.flayone.oaid.imp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.ASUSIDInterface;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 华硕手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:24
 */
public class ASUSDeviceIDHelper {

    private Context mContext;
    public final LinkedBlockingQueue<IBinder> linkedBlockingQueue = new LinkedBlockingQueue(1);

    public ASUSDeviceIDHelper(Context ctx) {
        mContext = ctx;
    }

    /**
     * 获取 OAID 并回调
     *
     * @param _listener
     */
    public void getID(AppIdsUpdater _listener) {
        try {
            try {
                mContext.getPackageManager().getPackageInfo("com.asus.msa.SupplementaryDID", 0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent();
            intent.setAction("com.asus.msa.action.ACCESS_DID");
            ComponentName componentName = new ComponentName("com.asus.msa.SupplementaryDID", "com.asus.msa.SupplementaryDID.SupplementaryDIDService");
            intent.setComponent(componentName);

            boolean isBin = mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            if (isBin) {
                try {
                    IBinder iBinder = linkedBlockingQueue.take();
                    ASUSIDInterface.ASUSID asusID = new ASUSIDInterface.ASUSID(iBinder);
                    String asusOAID = asusID.getID();

                    if (_listener != null) {
                        _listener.OnIdsAvalid(asusOAID);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                linkedBlockingQueue.put(service);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
}
