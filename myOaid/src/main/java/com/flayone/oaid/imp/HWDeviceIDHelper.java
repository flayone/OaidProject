package com.flayone.oaid.imp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.HWIDInterface;

/**
 * 华为手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:24
 */
public class HWDeviceIDHelper {

    private Context mContext;
    AppIdsUpdater _listener;

    public HWDeviceIDHelper(Context ctx) {
        mContext = ctx;
    }

    public void getHWID(AppIdsUpdater _listener) {
        this._listener = _listener;
        try {
            try {
                mContext.getPackageManager().getPackageInfo("com.huawei.hwid", 0);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            Intent bindIntent = new Intent("com.uodis.opendevice.OPENIDS_SERVICE");
            bindIntent.setPackage("com.huawei.hwid");
            boolean isBin = mContext.bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
//            if (isBin) {
//                try {
//                    IBinder iBinder = linkedBlockingQueue.take();
//                    HWIDInterface.HWID hwID = new HWIDInterface.HWID(iBinder);
//                    String ids = hwID.getIDs();
//                    boolean boos = hwID.getBoos();
//
//                    if (_listener != null) {
//                        _listener.OnIdsAvalid(ids);
//                    }
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                } finally {
//                    mContext.unbindService(serviceConnection);
//                }
//            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                HWIDInterface.HWID hwID = new HWIDInterface.HWID(service);
                String ids = hwID.getIDs();
                if (_listener != null) {
                    _listener.OnIdsAvalid(ids);
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
