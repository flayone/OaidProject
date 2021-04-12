package com.flayone.oaid;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.flayone.oaid.imp.ASUSDeviceIDHelper;
import com.flayone.oaid.imp.HWDeviceIDHelper;
import com.flayone.oaid.imp.LenovoDeviceIDHelper;
import com.flayone.oaid.imp.MeizuDeviceIDHelper;
import com.flayone.oaid.imp.NubiaDeviceIDHelper;
import com.flayone.oaid.imp.OnePlusDeviceIDHelper;
import com.flayone.oaid.imp.OppoDeviceIDHelper;
import com.flayone.oaid.imp.SamsungDeviceIDHelper;
import com.flayone.oaid.imp.VivoDeviceIDHelper;
import com.flayone.oaid.imp.XiaomiDeviceIDHelper;
import com.flayone.oaid.imp.ZTEDeviceIDHelper;

import java.lang.reflect.Method;

/**
 * 把各大厂商获取OADI的工具类统一封装成一个类
 *
 * @author AF
 * @time 2020/4/14 17:11
 */
public class OAIDHelper {

    private AppIdsUpdater appIdsUpdater;

    public OAIDHelper(AppIdsUpdater callback) {
        appIdsUpdater = callback;
    }

    public OAIDHelper() {
    }

    /**
     * 获取各大平台的OAID
     *
     * @param context
     */
    public static void getOAid(Context context, AppIdsUpdater mAppIdUpdateListener) {

        try {
            String oaid = null;
            String manufacturer = getManufacturer().toUpperCase();
            Log.d("DevicesIDsHelper", "manufacturer===> " + manufacturer);
            if (mAppIdUpdateListener == null) {
                return;
            }

            if (isFreeMeOS() || isSSUIOS()) {
                getOAIDByNewThread(context,mAppIdUpdateListener);
            }
            DeviceTypeEnum deviceType = DeviceTypeEnum.getInstance(manufacturer);
            switch (deviceType) {
                case HuaShuo:
                case HuaWei:
                case Oppo:
                case OnePlus:
                case ZTE:
                    getOAIDByNewThread(context,mAppIdUpdateListener);
                    break;
                case Lenove:
                case Motolora:
                    new LenovoDeviceIDHelper(context).getIdRun(mAppIdUpdateListener);
                    break;
                case Nubia:
                    oaid = new NubiaDeviceIDHelper(context).getNubiaID();
                    mAppIdUpdateListener.OnIdsAvalid(oaid);
                    break;
                case Meizu:
                    new MeizuDeviceIDHelper(context).getMeizuID(mAppIdUpdateListener);
                    break;
                case Samsung:
                    new SamsungDeviceIDHelper(context).getSumsungID(mAppIdUpdateListener);
                    break;
                case Vivo:
                    oaid = new VivoDeviceIDHelper(context).getOaid();
                    mAppIdUpdateListener.OnIdsAvalid(oaid);
                    break;
                case XiaoMi:
                case BlackShark:
                    oaid = new XiaomiDeviceIDHelper(context).getOAID();
                    mAppIdUpdateListener.OnIdsAvalid(oaid);
                    break;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }


    /**
     * 这些平台获取OAID是耗时操作，需要放在异步线程中
     *
     * @param context
     */
    private static void getOAIDByNewThread(final Context context, final AppIdsUpdater mAppIdUpdateListener) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (isFreeMeOS() || isSSUIOS()) {
                            new ZTEDeviceIDHelper(context).getID(mAppIdUpdateListener);
                            return;
                        }
                        String manufacturer = getManufacturer().toUpperCase();
                        DeviceTypeEnum deviceType = DeviceTypeEnum.getInstance(manufacturer);
                        switch (deviceType) {
                            case HuaShuo:
                                new ASUSDeviceIDHelper(context).getID(mAppIdUpdateListener);
                                break;
                            case HuaWei:
                                new HWDeviceIDHelper(context).getHWID(mAppIdUpdateListener);
                                break;
                            case Oppo:
                                new OppoDeviceIDHelper(context).getID(mAppIdUpdateListener);
                                break;
                            case OnePlus:
                                new OnePlusDeviceIDHelper(context).getID(mAppIdUpdateListener);
                                break;
                            case ZTE:
                                new ZTEDeviceIDHelper(context).getID(mAppIdUpdateListener);
                                break;
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取当前设备硬件制造商（MANUFACTURER）
     *
     * @return
     */
    private static String getManufacturer() {
        return Build.MANUFACTURER.toUpperCase();
    }

    private static String getProperty(String property) {
        String res = null;
        if (property == null) {
            return null;
        }
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getMethod("get", new Class[]{String.class, String.class});
            res = (String) method.invoke(clazz, new Object[]{property, "unknown"});
        } catch (Exception e) {
            // ignore
        }
        return res;
    }


    /**
     * 是否是freeMe操作系统，该系统是卓易科技深度定制的Android操作系统
     *
     * @return
     */
    public static boolean isFreeMeOS() {
        try {
            String pro = getProperty("ro.build.freeme.label");      // "ro.build.freeme.label"
            if ((!TextUtils.isEmpty(pro)) && pro.equalsIgnoreCase("FREEMEOS")) {      // "FreemeOS"  FREEMEOS
                return true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean isSSUIOS() {
        try {
            String pro = getProperty("ro.ssui.product");    // "ro.ssui.product"
            if ((!TextUtils.isEmpty(pro)) && (!pro.equalsIgnoreCase("unknown"))) {
                return true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }


}
