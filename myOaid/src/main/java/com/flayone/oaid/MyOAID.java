package com.flayone.oaid;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

public class MyOAID {

    public static String TAG = "MyOAID";
    public static String SP_OAID = "myoaid";


    public static void init(final Context context) {
        init(context, null);
    }

    public static void init(final Context context, final ResultCallBack callBack) {
        OAIDHelper.getOAid(context, new AppIdsUpdater() {
            @Override
            public void OnIdsAvalid(@NonNull final String oaid) {
                try {
                    Log.d(TAG, "[OnIdsAvalid] oaid= " + oaid);

                    if (TextUtils.isEmpty(oaid)) {
                        Log.e(TAG, "oaid为空,未获取到oaid");
                        return;
                    }
                    if (callBack != null) {
                        callBack.onResult(oaid);
                    }
                    OAIDManager.getInstance().setOaId(oaid);
                    OAIDManager.saveString(context, SP_OAID, oaid);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static String getOAID(Context context) {
        try {
            String insOaid = OAIDManager.getInstance().getOaId();
            String savedOaid = OAIDManager.getSavedString(context, SP_OAID);
            String oaid;
            if (TextUtils.isEmpty(insOaid)) {
                oaid = savedOaid;
            } else {
                oaid = insOaid;
            }
            return oaid;
        } catch (Throwable e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getVersion(){
        return BuildConfig.VERSION_NAME;
    }
}
