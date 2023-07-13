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

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.IDGetterAction;
import com.flayone.oaid.interfaces.OppoIDInterface;

import java.security.MessageDigest;

/**
 * OPPO手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:27
 */
public class OppoDeviceIDHelper implements IDGetterAction {

    private Context mContext;
    public String oaid = "OUID";
    private String sign;

    public OppoDeviceIDHelper(Context ctx) {
        mContext = ctx;
    }


    @Override
    public void getID(final AppIdsUpdater _listener) {
        try {
            if (mContext == null) {
                if (_listener != null) {
                    _listener.OnIdsAvalid("");
                }
                return;
            }

            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.heytap.openid", "com.heytap.openid.IdentifyService"));
            intent.setAction("action.com.heytap.openid.OPEN_ID_SERVICE");

            mContext.bindService(intent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    try {
                        OppoIDInterface oppoIDInterface = OppoIDInterface.Stub.genInterface(service);
                        if (oppoIDInterface == null) {
                            if (_listener != null) {
                                _listener.OnIdsAvalid("");
                            }
                            mContext.unbindService(this);
                            return;
                        }
                        try {
                            String oaid = realoGetIds(oppoIDInterface, "OUID");
                            if (_listener != null) {
                                _listener.OnIdsAvalid(oaid);
                            }
                        } catch (Exception e) {
                            //获取异常情况下，也按照空处理
                            if (_listener != null) {
                                _listener.OnIdsAvalid("");
                            }
                            e.printStackTrace();
                        }

                        mContext.unbindService(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    try {
                        if (_listener != null) {
                            _listener.OnIdsAvalid("");
                        }
                        mContext.unbindService(this);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }, Context.BIND_AUTO_CREATE);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    private String realoGetIds(OppoIDInterface oppoIDInterface, String str) {
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

        res = ((OppoIDInterface.Proxy) oppoIDInterface).getSerID(pkgName, sign, str);

        return res;
    }


    @Override
    public boolean supported() {
        if (mContext == null) {
            return false;
        }
        try {
            PackageInfo pi = mContext.getPackageManager().getPackageInfo("com.heytap.openid", 0);
            return pi != null;
        } catch (Exception e) {
            return false;
        }
    }

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
