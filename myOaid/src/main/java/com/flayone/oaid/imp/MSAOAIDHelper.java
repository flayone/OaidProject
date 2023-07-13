package com.flayone.oaid.imp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.IDGetterAction;

import mymsa.com.bun.lib.MsaIdInterface;

public class MSAOAIDHelper implements IDGetterAction {
    private final Context context;

    public MSAOAIDHelper(Context context) {
        this.context = context;
    }

    @Override
    public boolean supported() {
        if (context == null) {
            return false;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo("com.mdid.msa", 0);
            return pi != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void getID(final AppIdsUpdater _listener) {
        try {
            if (context == null) {
                if (_listener != null) {
                    _listener.OnIdsAvalid("");
                }
                return;
            }

            startMsaKlService();
            Intent intent = new Intent("com.bun.msa.action.bindto.service");
            intent.setClassName("com.mdid.msa", "com.mdid.msa.service.MsaIdService");
            intent.putExtra("com.bun.msa.param.pkgname", context.getPackageName());

            context.bindService(intent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    try {
                        MsaIdInterface anInterface = MsaIdInterface.Stub.asInterface(service);

                        if (anInterface == null || !anInterface.isSupported()) {
                            if (_listener != null) {
                                _listener.OnIdsAvalid("");
                            }
                            context.unbindService(this);
                            return;
                        }

                        //
                        String result = anInterface.getOAID();
                        Log.d("【MSAOAIDHelper】", " oaid result = " + result);

                        if (_listener != null) {
                            _listener.OnIdsAvalid(result);
                        }
                        context.unbindService(this);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    try {
                        if (_listener != null) {
                            _listener.OnIdsAvalid("");
                        }
                        context.unbindService(this);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startMsaKlService() {
        try {
            Intent intent = new Intent("com.bun.msa.action.start.service");
            intent.setClassName("com.mdid.msa", "com.mdid.msa.service.MsaKlService");
            intent.putExtra("com.bun.msa.param.pkgname", context.getPackageName());
            if (Build.VERSION.SDK_INT < 26) {
                context.startService(intent);
            } else {
                context.startForegroundService(intent);
            }
        } catch (Exception e) {
        }
    }
}
