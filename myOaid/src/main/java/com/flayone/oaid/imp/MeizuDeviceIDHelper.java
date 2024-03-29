package com.flayone.oaid.imp;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.IDGetterAction;

/**
 * 魅族手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:25
 */
public class MeizuDeviceIDHelper implements IDGetterAction {

    private final Context mContext;

    public MeizuDeviceIDHelper(Context ctx) {
        mContext = ctx;
    }


    @Override
    public boolean supported() {
        try {
            PackageManager pm = mContext.getPackageManager();
            if (pm != null) {
                ProviderInfo pi = pm.resolveContentProvider("com.meizu.flyme.openidsdk", 0);        // "com.meizu.flyme.openidsdk"
                if (pi != null) {
                    return true;
                }
            }
        } catch (Throwable e) {
            //ignore
        }
        return false;
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
            try {
                mContext.getPackageManager().getPackageInfo("com.meizu.flyme.openidsdk", 0);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            Uri uri = Uri.parse("content://com.meizu.flyme.openidsdk/");

            Cursor cursor;
            ContentResolver contentResolver = mContext.getContentResolver();
            try {
                cursor = contentResolver.query(uri, null, null, new String[]{"oaid"}, null);
                String oaid = getOaid(cursor);

                if (_listener != null) {
                    _listener.OnIdsAvalid(oaid);
                }
                cursor.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 OAID
     *
     * @param cursor
     * @return
     */
    private String getOaid(Cursor cursor) {
        String oaid = null;
        try {
            if (cursor == null) {
                return null;
            }
            if (cursor.isClosed()) {
                return null;
            }
            cursor.moveToFirst();
            int valueIdx = cursor.getColumnIndex("value");
            Log.d("MeizuDeviceIDHelper", "[getOaid] valueIdx = " + valueIdx);
            if (valueIdx >= 0) {
                oaid = cursor.getString(valueIdx);
            }
            valueIdx = cursor.getColumnIndex("code");
            if (valueIdx > 0) {
                int codeID = cursor.getInt(valueIdx);
            }
            valueIdx = cursor.getColumnIndex("expired");
            if (valueIdx > 0) {
                long timeC = cursor.getLong(valueIdx);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return oaid;
    }
}
