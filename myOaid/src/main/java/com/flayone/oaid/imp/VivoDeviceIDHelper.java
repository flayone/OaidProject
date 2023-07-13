package com.flayone.oaid.imp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.OAIDRom;
import com.flayone.oaid.interfaces.IDGetterAction;

/**
 * Vivo手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:29
 */
public class VivoDeviceIDHelper implements IDGetterAction {

    private final Context mConetxt;

    public VivoDeviceIDHelper(Context ctx) {
        mConetxt = ctx;
    }


    @Override
    public boolean supported() {
        boolean result = false;
        try {
            result = OAIDRom.sysProperty("persist.sys.identifierid.supported", "0").equals("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


//    private HandlerThread handlerThread;
//    private Handler handler;
//    private boolean isSupportIds = false;
//    String oaid = null;

    @Override
    public void getID(final AppIdsUpdater _listener) {
        try {
            String res = "";
            try {
                Uri uri = Uri.parse("content://com.vivo.vms.IdProvider/IdentifierId/OAID");
                Cursor cursor = mConetxt.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToNext()) {
                        res = cursor.getString(cursor.getColumnIndex("value"));
                    }
                    cursor.close();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (_listener != null) {
                _listener.OnIdsAvalid(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public String loge() {
//        String result = null;
//
//        try {
//            f();
//            isSupportIds();
//
//            if (!isSupportIds) {
//                return null;
//            }
//            if (oaid != null) {
//                return null;
//            }
//            timeCheck(0, null);
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    private void timeCheck(int i, String str) {
//        try {
//            Message message = handler.obtainMessage();
//            message.what = 11;
//            Bundle bundle = new Bundle();
//            bundle.putInt("type", 0);
//            if (i == 1 || i == 2) {
//                bundle.putString("appid", str);
//            }
//            message.setData(bundle);
//            handler.sendMessage(message);
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }

//    private String sysProperty(String v1, String v2) {
//        String res = null;
//        try {
//            Class clazz = Class.forName("android.os.SystemProperties");
//            Method method = clazz.getMethod("get", new Class[]{String.class, String.class});
//            res = (String) method.invoke(clazz, new Object[]{v1, "unknown"});
//        } catch (Throwable e) {
//            e.printStackTrace();
//            return v2;
//        }
//        return res;
//    }

//    private boolean isSupportIds() {
//        try {
//            String isSupId = sysProperty("persist.sys.identifierid.supported", "0");
//            isSupportIds = isSupId.equals("1");
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return isSupportIds;
//    }
//
//    private void f() {
//        try {
//            handlerThread = new HandlerThread("SqlWorkThread");
//            handlerThread.start();
//            handler = new Handler(handlerThread.getLooper()) {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//                    if (msg.what == 11) {
//                        int tag = msg.getData().getInt("type");
//                        String name = msg.getData().getString("appid");
//                        String id = getContentResolver(tag, name);
//                    } else {
//
//                    }
//                }
//            };
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }

//    private String getContentResolver(int tag, String name) {
//        String result = null;
//        try {
//            Uri uri;
//            switch (tag) {
//                case 0:
//                    uri = Uri.parse("content://com.vivo.vms.IdProvider/IdentifierId/OAID");
//                    break;
//                case 1:
//                    uri = Uri.parse("content://com.vivo.vms.IdProvider/IdentifierId/VAID_" + name);
//                    break;
//                case 2:
//                    uri = Uri.parse("content://com.vivo.vms.IdProvider/IdentifierId/AAID_" + name);
//                    break;
//                default:
//                    uri = null;
//                    break;
//            }
//            Cursor cursor = mConetxt.getContentResolver().query(uri, null, null, null, null);
//            if (cursor != null) {
//                if (cursor.moveToNext()) {
//                    result = cursor.getString(cursor.getColumnIndex("value"));
//                }
//                cursor.close();
//            } else {
//
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
}
