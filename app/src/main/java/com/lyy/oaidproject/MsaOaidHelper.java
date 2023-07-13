package com.lyy.oaidproject;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;

public class MsaOaidHelper implements IIdentifierListener {
    private String TAG = "[MsaOaidHelper] ";
    private OaidUpdater oaidUpdater;

    public MsaOaidHelper(OaidUpdater oaidUpdater) {
        this.oaidUpdater = oaidUpdater;
    }


    public void getDeviceIds(Context cxt) {
        try {
            long timeb = System.currentTimeMillis();
            int nres = CallFromReflect(cxt);
//        int nres=DirectCall(cxt);
            long timee = System.currentTimeMillis();
            long offset = timee - timeb;
            String resultMeaning = "";
            if (nres == ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT) {//不支持的设备
                resultMeaning = "不支持的设备";
            } else if (nres == ErrorCode.INIT_ERROR_LOAD_CONFIGFILE) {//加载配置文件出错
                resultMeaning = "加载配置文件出错";
                Log.e(TAG,"请检查 assets文件夹下的 supplierconfig.json是否未配置或配置参数异常");
            } else if (nres == ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT) {//不支持的设备厂商
                resultMeaning = "不支持的设备厂商";
            } else if (nres == ErrorCode.INIT_ERROR_RESULT_DELAY) {//获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程
                resultMeaning = "获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程";
            } else if (nres == ErrorCode.INIT_HELPER_CALL_ERROR) {//反射调用出错
                resultMeaning = "反射调用出错";
            }
            Log.d(TAG , "return value: " + nres + " meaning：" + resultMeaning);
        } catch (Throwable e) {
            e.printStackTrace();
            Log.w(TAG , "getDeviceIds error");
        }

    }


    /**
     * 通过反射调用，解决android 9以后的类加载升级，导至找不到so中的方法
     */
    private int CallFromReflect(Context cxt) {
        return MdidSdkHelper.InitSdk(cxt, true, this);
    }

    @Override
    public void OnSupport(boolean isSupport, IdSupplier _supplier) {
        try {
            if (_supplier == null) {
                return;
            }
            String oaid = _supplier.getOAID();
            String vaid = _supplier.getVAID();
            String aaid = _supplier.getAAID();
//        String udid=_supplier.getUDID();
            StringBuilder builder = new StringBuilder();
            builder.append("support: ").append(isSupport ? "true" : "false").append("\n");
//        builder.append("UDID: ").append(udid).append("\n");
            builder.append("OAID: ").append(oaid).append("\n");
            builder.append("VAID: ").append(vaid).append("\n");
            builder.append("AAID: ").append(aaid).append("\n");
            String idstext = builder.toString();
            Log.d(TAG , "ids_text == " + idstext);
            //将oaid 赋值给mercury sdk
            if (oaid != null && !"".equals(oaid)) {
                Log.d(TAG , "oaid == " + oaid);
                if (oaidUpdater != null) {
                    oaidUpdater.IdReceived(oaid);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Log.w(TAG , "OnSupport error");

        }
    }

    public interface OaidUpdater {
        void IdReceived(@NonNull String id);
    }

}
