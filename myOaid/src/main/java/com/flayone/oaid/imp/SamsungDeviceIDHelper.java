package com.flayone.oaid.imp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.SamsungIDInterface;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 三星手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:28
 */
public class SamsungDeviceIDHelper {

    private Context mContext;
    public final LinkedBlockingQueue<IBinder> linkedBlockingQueue = new LinkedBlockingQueue(1);

    public SamsungDeviceIDHelper(Context ctx) {
        mContext = ctx;
    }

    public void getSumsungID(AppIdsUpdater _listener) {
        try {
            try {
                mContext.getPackageManager().getPackageInfo("com.samsung.android.deviceidservice", 0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent();
            intent.setClassName("com.samsung.android.deviceidservice", "com.samsung.android.deviceidservice.DeviceIdService");
            boolean isBinded = mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            if (isBinded) {
                try {
                    IBinder iBinder = linkedBlockingQueue.take();
                    SamsungIDInterface.Proxy proxy = new SamsungIDInterface.Proxy(iBinder);       // 在这里有区别，需要实际验证
                    String oaid = proxy.getID();
                    if (_listener != null) {
                        _listener.OnIdsAvalid(oaid);
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
