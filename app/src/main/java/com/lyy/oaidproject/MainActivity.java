package com.lyy.oaidproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.MyOAID;
import com.flayone.oaid.MyOAIDHelper;
import com.flayone.oaid.OAIDManager;
import com.flayone.oaid.ResultCallBack;
import com.flayone.oaid.imp.MSAOAIDHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "[MainActivity] ";
    TextView MsaOaid;
    TextView MyOaid;
    TextView nowOaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyOaid = findViewById(R.id.myoaid);
        MsaOaid = findViewById(R.id.msaoaid);
        nowOaid = findViewById(R.id.now_oaid);
        try {
            final TextView gid = findViewById(R.id.tv_gaid);

            MyOAID.getGoogleADID(this, new ResultCallBack() {
                @Override
                public void onResult(String oaid) {
                    gid.setText("MyOaid ,获取到的gaid = "+ oaid);
                }
            });
            TextView support = findViewById(R.id.tv_support);
            support.setText("MyOaid , 是否支持OAID获取：" + MyOAID.isSupportOAID(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        getOAID();
    }

    private void getOAID() {

        //MyOaid SDK方式(推荐)
        MyOAID.getOAID(this, new ResultCallBack() {
            @Override
            public void onResult(final String oaid) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        MyOaid.setText("MyOaid SDK获取的Oaid值：\n " + oaid);
                    }
                });
            }
        });

        //msa SDK方式
        new MsaOaidHelper(new MsaOaidHelper.OaidUpdater() {
            @Override
            public void IdReceived(@NonNull final String id) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        MsaOaid.setText("Msa SDK获取的Oaid值：\n " + id);
                    }
                });
            }
        }).getDeviceIds(this);
    }

    public void refresh(View view) {
        getOAID();
    }

    public void getNow(View view) {
        MyOAIDHelper.getOAid(this, new AppIdsUpdater() {
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
                            new MSAOAIDHelper(MainActivity.this).getID(new AppIdsUpdater() {
                                @Override
                                public void OnIdsAvalid(@NonNull final String ids) {

                                    Log.d(TAG, "[MSA-OnIdsAvalid] oaid= " + ids);
                                    hasResult[0] = true;
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            nowOaid.setText("MyOaid SDK【实时】获取的Oaid值：\n " + ids);
                                        }
                                    });
                                    //无论获取值是否为空，都应该回调出去，防止无响应结果导致业务处理异常
//                                    if (callBack != null) {
//                                        callBack.onResult(ids);
//                                    }
                                    //如果不为空，那么进行本地存储
                                    if (TextUtils.isEmpty(ids)) {
//                                        OAIDManager.getInstance().setOaId(ids);
//                                        OAIDManager.saveString(context, SP_OAID, ids);
                                    }
                                }
                            });
                        } catch (Throwable e) {
//                            //已经有结果的话，不在回调
//                            if (!hasResult[0]) {
//                                if (callBack != null) {
//                                    callBack.onResult(oaid);
//                                }
//                            }
                            e.printStackTrace();
                        }
                        return;
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            nowOaid.setText("MyOaid SDK获取的实时Oaid值：\n " + oaid);
                        }
                    });
//                    OAIDManager.getInstance().setOaId(oaid);
//                    OAIDManager.saveString(context, SP_OAID, oaid);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}