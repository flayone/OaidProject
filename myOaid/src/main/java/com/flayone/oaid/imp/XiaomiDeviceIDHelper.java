package com.flayone.oaid.imp;

import android.content.Context;

import java.lang.reflect.Method;

/**
 * 小米手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:29
 */
public class XiaomiDeviceIDHelper {
    private Context mContext;

    private Class idProvider;
    private Object idImpl;
    private Method udid;
    private Method oaid;
    private Method vaid;
    private Method aaid;


    public XiaomiDeviceIDHelper(Context ctx) {
        mContext = ctx;

        try {
            idProvider = Class.forName("com.android.id.impl.IdProviderImpl");
            idImpl = idProvider.newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            udid = idProvider.getMethod("getDefaultUDID", new Class[]{Context.class});
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            oaid = idProvider.getMethod("getOAID", new Class[]{Context.class});
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            vaid = idProvider.getMethod("getVAID", new Class[]{Context.class});
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            aaid = idProvider.getMethod("getAAID", new Class[]{Context.class});
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private String invokeMethod(Context ctx, Method method) {
        String result = null;
        if (idImpl != null && method != null) {
            try {
                result = (String) method.invoke(idImpl, ctx);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public String getUDID() {
        return invokeMethod(mContext, udid);
    }

    public String getOAID() {
        return invokeMethod(mContext, oaid);
    }

    public String getAAID() {
        return invokeMethod(mContext, aaid);
    }

    public String getVAID() {
        return invokeMethod(mContext, vaid);
    }
}
