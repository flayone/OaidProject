package com.flayone.oaid.imp;

import android.app.KeyguardManager;
import android.content.Context;
import android.util.Log;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.IDGetterAction;

import java.util.Objects;

// 酷赛手机
public class CooseaHelper implements IDGetterAction {
    private final Context context;
    private final KeyguardManager keyguardManager;

    public CooseaHelper(Context context) {
        this.context = context;
        this.keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
    }

    @Override
    public boolean supported() {
        if (context == null) {
            return false;
        }
        if (keyguardManager == null) {
            return false;
        }
        try {
            Object obj = keyguardManager.getClass().getDeclaredMethod("isSupported").invoke(keyguardManager);
            return (Boolean) Objects.requireNonNull(obj);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void getID(AppIdsUpdater _listener) {
        if (context == null) {
            if (_listener != null) {
                _listener.OnIdsAvalid("");
            }
            return;
        }
        if (keyguardManager == null) {
            _listener.OnIdsAvalid("");
            return;
        }
        try {
            Object obj = keyguardManager.getClass().getDeclaredMethod("obtainOaid").invoke(keyguardManager);
            if (obj == null) {
                _listener.OnIdsAvalid("");
                return;
            }
            String oaid = obj.toString();
            Log.d("[CooseaHelper] getID: ", "OAID obtain success: " + oaid);
            _listener.OnIdsAvalid(oaid);
        } catch (Exception e) {
//            OAIDLog.print(e);
        }
    }
}
