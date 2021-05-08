package com.flayone.oaid;

import android.content.Context;
import android.content.SharedPreferences;

public class OAIDManager {
    private static OAIDManager instance;
    private String oaId;
    private static String NAME = "myoaid_setting_sp";

    public static synchronized OAIDManager getInstance() {
        if (instance == null) {
            instance = new OAIDManager();
        }
        return instance;
    }

    public String getOaId() {
        return oaId;
    }

    public void setOaId(String oaId) {
        this.oaId = oaId;
    }



    public static void saveString(Context context, String key, String value) {
        try {
            context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit().putString(key, value).commit();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static String getSavedString(Context context, String key) {
        try {
            SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            return sp.getString(key, "");
        } catch (Throwable e) {
            e.printStackTrace();
            return "";
        }
    }
}
