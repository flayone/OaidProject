package com.flayone.oaid.imp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.HWIDInterface;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 华为手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:24
 */
public class HWDeviceIDHelper {

    private Context mContext;
    public final LinkedBlockingQueue<IBinder> linkedBlockingQueue = new LinkedBlockingQueue(1);

    public HWDeviceIDHelper(Context ctx) {
        mContext = ctx;
    }

    public void getHWID(AppIdsUpdater _listener) {
        try {
            try {
                mContext.getPackageManager().getPackageInfo("com.huawei.hwid", 0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent bindIntent = new Intent("com.uodis.opendevice.OPENIDS_SERVICE");
            bindIntent.setPackage("com.huawei.hwid");
            boolean isBin = mContext.bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            if (isBin) {
                try {
                    IBinder iBinder = linkedBlockingQueue.take();
                    HWIDInterface.HWID hwID = new HWIDInterface.HWID(iBinder);
                    String ids = hwID.getIDs();
                    boolean boos = hwID.getBoos();

                    if (_listener != null) {
                        _listener.OnIdsAvalid(ids);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mContext.unbindService(serviceConnection);
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
