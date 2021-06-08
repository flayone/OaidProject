package com.flayone.oaid.imp;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.OnePlusIDInterface;

import java.security.MessageDigest;

/**
 * 一加手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:27
 */
public class OnePlusDeviceIDHelper {

    private Context mContext;
    public String oaid = "OUID";
    private String sign;
    OnePlusIDInterface onePlusIDInterface;

    public OnePlusDeviceIDHelper(Context ctx) {
        mContext = ctx;
    }


    public String getID(AppIdsUpdater _listener) {

        String res = null;

        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                throw new IllegalStateException("Cannot run on MainThread");
            }

            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.heytap.openid", "com.heytap.openid.IdentifyService"));
            intent.setAction("action.com.heytap.openid.OPEN_ID_SERVICE");

            if (mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)) {

                try {
                    SystemClock.sleep(3000);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                if (onePlusIDInterface != null) {
                    String oaid = realoGetIds("OUID");
                    String vaid = realoGetIds("DUID");
                    String aaid = realoGetIds("AUID");
                    res = oaid;

                    if (_listener != null) {
                        _listener.OnIdsAvalid(oaid);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return res;
    }

    @SuppressLint("WrongConstant")
    private String realoGetIds(String str) {
        String res = null;

        String str2 = null;
        String pkgName = mContext.getPackageName();
        if (sign == null) {
            Signature[] signatures;
            try {
                signatures = mContext.getPackageManager().getPackageInfo(pkgName, 64).signatures;
            } catch (Throwable e) {
                e.printStackTrace();
                signatures = null;
            }

            if (signatures != null && signatures.length > 0) {
                byte[] byteArray = signatures[0].toByteArray();
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
                    if (messageDigest != null) {
                        byte[] digest = messageDigest.digest(byteArray);
                        StringBuilder sb = new StringBuilder();
                        for (byte b : digest) {
                            sb.append(Integer.toHexString((b & 255) | 256).substring(1, 3));
                        }
                        str2 = sb.toString();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            sign = str2;
        }

        res = ((OnePlusIDInterface.Proxy) onePlusIDInterface).getSerID(pkgName, sign, str);
        return res;
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            onePlusIDInterface = OnePlusIDInterface.Stub.genInstance(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            onePlusIDInterface = null;
        }
    };

    private boolean isSupportOppo() {
        boolean res = true;

        try {
            PackageManager pm = mContext.getPackageManager();
            String pNname = "com.heytap.openid";

            PackageInfo pi = pm.getPackageInfo(pNname, 0);
            if (pi == null) {
                return false;
            }
            long ver = 0;
            if (Build.VERSION.SDK_INT >= 28) {
                ver = pi.getLongVersionCode();
            } else {
                ver = pi.versionCode;
            }
            if (ver < 1) {
                return false;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return res;
    }
}
