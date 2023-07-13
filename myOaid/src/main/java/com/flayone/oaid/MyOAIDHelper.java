package com.flayone.oaid;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.flayone.oaid.imp.ASUSDeviceIDHelper;
import com.flayone.oaid.imp.CoolpadHelper;
import com.flayone.oaid.imp.CooseaHelper;
import com.flayone.oaid.imp.DefaultHelper;
import com.flayone.oaid.imp.FreemeHelper;
import com.flayone.oaid.imp.HWDeviceIDHelper;
import com.flayone.oaid.imp.LenovoDeviceIDHelper;
import com.flayone.oaid.imp.MeizuDeviceIDHelper;
import com.flayone.oaid.imp.NubiaDeviceIDHelper;
import com.flayone.oaid.imp.OppoDeviceIDHelper;
import com.flayone.oaid.imp.SamsungDeviceIDHelper;
import com.flayone.oaid.imp.VivoDeviceIDHelper;
import com.flayone.oaid.imp.XiaomiDeviceIDHelper;
import com.flayone.oaid.interfaces.IDGetterAction;

/**
 * 把各大厂商获取OADI的工具类统一封装成一个类
 *
 * @author AF
 * @time 2020/4/14 17:11
 */
public class MyOAIDHelper {


    /**
     * 获取各大平台的OAID
     *
     * @param context
     */
    public static void getOAid(Context context, AppIdsUpdater mAppIdUpdateListener) {
        try {
            String manufacturer = getManufacturer().toUpperCase();
            Log.d("MyOAIDHelper", "manufacturer===> " + manufacturer);

            IDGetterAction idGetterAction = createOaidGetter(context);
            //获取oaid
            idGetterAction.getID(mAppIdUpdateListener);
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


    //获取接口实现文件
    public static IDGetterAction createOaidGetter(Context context) {
        try {
            if (OAIDRom.isHuawei() || OAIDRom.isEmui()) {
                return new HWDeviceIDHelper(context);
            }

            if (OAIDRom.isVivo()) {
                return new VivoDeviceIDHelper(context);
            }

            if (OAIDRom.isOppo() || OAIDRom.isOnePlus()) {
                return new OppoDeviceIDHelper(context);
            }

            if (OAIDRom.isXiaomi() || OAIDRom.isMiui() || OAIDRom.isBlackShark()) {
                return new XiaomiDeviceIDHelper(context);
            }
            if (OAIDRom.isSamsung()) {
                return new SamsungDeviceIDHelper(context);
            }
            if (OAIDRom.isMeizu()) {
                return new MeizuDeviceIDHelper(context);
            }
            if (OAIDRom.isNubia()) {
                return new NubiaDeviceIDHelper(context);
            }
            if (OAIDRom.isASUS()) {
                return new ASUSDeviceIDHelper(context);
            }
            if (OAIDRom.isLenovo() || OAIDRom.isMotolora()) {
                return new LenovoDeviceIDHelper(context);
            }
            if (OAIDRom.isCoolpad(context)) {
                return new CoolpadHelper(context);
            }
            if (OAIDRom.isCoosea()) {
                return new CooseaHelper(context);
            }
            if (OAIDRom.isFreeme()) {
                return new FreemeHelper(context);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new DefaultHelper();
    }

}
