package com.flayone.oaid.imp;

import android.content.ContentProviderClient;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.IDGetterAction;

/**
 * 努比亚手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:26
 */
public class NubiaDeviceIDHelper implements IDGetterAction {

    private Context mContent;

    public NubiaDeviceIDHelper(Context ctx) {
        mContent = ctx;
    }

    @Override
    public boolean supported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }


    @Override
    public void getID(final AppIdsUpdater _listener) {
        String oaid = "";
        Bundle bundle;

        Uri uri = Uri.parse("content://cn.nubia.identity/identity");
        try {
            if (Build.VERSION.SDK_INT > 17) {
                ContentProviderClient contentProviderClient = mContent.getContentResolver().acquireContentProviderClient(uri);
                bundle = contentProviderClient.call("getOAID", null, null);
                if (contentProviderClient != null) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        contentProviderClient.close();
                    } else {
                        contentProviderClient.release();
                    }
                }
            } else {
                bundle = mContent.getContentResolver().call(uri, "getOAID", null, null);
            }
            int code = bundle.getInt("code", -1);
            if (code == 0) {
                oaid = bundle.getString("id");

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (_listener != null) {
            _listener.OnIdsAvalid(oaid);
        }

    }
}
