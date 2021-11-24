package com.flayone.oaid;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

public class MyOAID {

    public static String TAG = "MyOAID";
    public static String SP_OAID = "myoaid";


    @Deprecated
    public static void init(final Context context) {
        init(context, null);
    }

    @Deprecated
    public static void init(final Context context, final ResultCallBack callBack) {
        MyOAIDHelper.getOAid(context, new AppIdsUpdater() {
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

    public static void getOAID(Context context, final ResultCallBack callBack) {
        try {
            String insOaid = OAIDManager.getInstance().getOaId();
            String savedOaid = OAIDManager.getSavedString(context, SP_OAID);
            if (TextUtils.isEmpty(insOaid)) {
                if (TextUtils.isEmpty(savedOaid)) {
                    //获取oaid
                    init(context, new ResultCallBack() {
                        @Override
                        public void onResult(String oaid) {

                            if (callBack != null) {
                                callBack.onResult(oaid);
                            }
                        }
                    });
                } else {
                    if (callBack != null) {
                        callBack.onResult(savedOaid);
                    }
                }
            } else {
                if (callBack != null) {
                    callBack.onResult(insOaid);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public static String getOAID(Context context) {
        String result = "";
        try {
            String insOaid = OAIDManager.getInstance().getOaId();
            String savedOaid = OAIDManager.getSavedString(context, SP_OAID);
            if (TextUtils.isEmpty(insOaid)) {
                result = savedOaid;
            } else {
                result = insOaid;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getVersion() {
        return BuildConfig.VERSION_NAME;
    }
}
