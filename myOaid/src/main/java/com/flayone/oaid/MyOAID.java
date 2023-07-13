package com.flayone.oaid;

import static com.flayone.oaid.MyOAIDHelper.createOaidGetter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.flayone.oaid.imp.GMSADIDHelper;
import com.flayone.oaid.imp.MSAOAIDHelper;
import com.flayone.oaid.interfaces.IDGetterAction;

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
                    //无论获取值是否为空，都应该回调出去，防止无响应结果导致业务处理异常
//                    if (callBack != null) {
//                        callBack.onResult(oaid);
//                    }
                    if (TextUtils.isEmpty(oaid)) {
                        Log.e(TAG, "oaid为空,尝试msa方式获取OAID");
                        final boolean[] hasResult = {false};

                        try {
                            //尝试msa方式获取OAID
                            new MSAOAIDHelper(context).getID(new AppIdsUpdater() {
                                @Override
                                public void OnIdsAvalid(@NonNull String ids) {
                                    Log.d(TAG, "[MSA-OnIdsAvalid] oaid= " + oaid);
                                    hasResult[0] = true;

                                    //无论获取值是否为空，都应该回调出去，防止无响应结果导致业务处理异常
                                    if (callBack != null) {
                                        callBack.onResult(ids);
                                    }
                                    //如果不为空，那么进行本地存储
                                    if (TextUtils.isEmpty(ids)) {
                                        OAIDManager.getInstance().setOaId(ids);
                                        OAIDManager.saveString(context, SP_OAID, ids);
                                    }
                                }
                            });
                        } catch (Throwable e) {
                            //已经有结果的话，不在回调
                            if (!hasResult[0]) {
                                if (callBack != null) {
                                    callBack.onResult(oaid);
                                }
                            }
                            e.printStackTrace();
                        }
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

    //获取google广告id标识
    public static void getGoogleADID(Context context, final ResultCallBack callBack) {
        try {
            final long start = System.currentTimeMillis();
            new GMSADIDHelper(context).getID(new AppIdsUpdater() {
                @Override
                public void OnIdsAvalid(@NonNull String ids) {
                    long cost = System.currentTimeMillis() - start;
                    Log.d(TAG, "【getGoogleADID】OnIdsAvalid: "+ids +"，cost = "+ cost );
                    if (callBack != null) {
                        callBack.onResult(ids);
                    }
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void getOAID(Context context, final ResultCallBack callBack) {
        try {
            String insOaid = OAIDManager.getInstance().getOaId();
            String savedOaid = OAIDManager.getSavedString(context, SP_OAID);
//            String insOaid = "";
//            String savedOaid = "";
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

    //是否支持OAID获取
    public static boolean isSupportOAID(Context context) {
        boolean result = false;
        IDGetterAction idGetterAction = null;
        try {
            idGetterAction = createOaidGetter(context);
            //是否支持oaid获取
            result = idGetterAction.supported();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
