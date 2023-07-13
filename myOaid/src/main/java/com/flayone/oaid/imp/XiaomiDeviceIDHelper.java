package com.flayone.oaid.imp;

import android.content.Context;
import android.util.Log;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.IDGetterAction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 小米手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:29
 */
public class XiaomiDeviceIDHelper implements IDGetterAction {
    private static final String TAG = "[XiaomiDeviceIDHelper]";
    private Context mContext;


    private Class<?> idProviderClass;
    private Object idProviderImpl;
//
//    private Class idProvider;
//    private Object idImpl;
//    private Method udid;
//    private Method oaid;
//    private Method vaid;
//    private Method aaid;


    public XiaomiDeviceIDHelper(Context ctx) {
        mContext = ctx;
        try {
            idProviderClass = Class.forName("com.android.id.impl.IdProviderImpl");
            idProviderImpl = idProviderClass.newInstance();
        } catch (Exception e) {
        }
    }


    @Override
    public boolean supported() {
        return idProviderImpl != null;
    }


    @Override
    public void getID(final AppIdsUpdater _listener) {

        try {
            String oaid = "";

            try {
                oaid = getOAID();
                Log.d(TAG, "getID: " + "OAID query success: " + oaid);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            if (_listener != null) {
                _listener.OnIdsAvalid(oaid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getOAID() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = idProviderClass.getMethod("getOAID", Context.class);
        return (String) method.invoke(idProviderImpl, mContext);
    }

//    private String invokeMethod(Context ctx, Method method) {
//        String result = null;
//        if (idImpl != null && method != null) {
//            try {
//                result = (String) method.invoke(idImpl, ctx);
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }
//
//    public String getUDID() {
//        return invokeMethod(mContext, udid);
//    }

//    public String getOAID() {
//        return invokeMethod(mContext, oaid);
//    }
//
//    public String getAAID() {
//        return invokeMethod(mContext, aaid);
//    }
//
//    public String getVAID() {
//        return invokeMethod(mContext, vaid);
//    }
}
