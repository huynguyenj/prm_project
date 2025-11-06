package com.nguyenlegiahuy.prm_project_v2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Arrays;

public class SessionManager {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_TOKEN = "access_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_AGENCY_ID = "agency_id";
    private static final String KEY_ROLE = "role";

    private static Context appContext;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        appContext = context.getApplicationContext();
        prefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }


    public static Context getAppContext() {
        if (appContext == null) {
            throw new IllegalStateException(
                    "SessionManager not initialized. Call new SessionManager(context) after login.");
        }
        return appContext;
    }


    public void saveUserSession(String token, int userId, int agencyId, String[] roles) {
        editor.putString(KEY_TOKEN, token);
        editor.putInt(KEY_USER_ID, userId);
        editor.putInt(KEY_AGENCY_ID, agencyId);
        editor.putString(KEY_ROLE, String.join(",", roles));
        editor.apply();
    }

    public String getToken() { return prefs.getString(KEY_TOKEN, null); }
    public int getUserId() { return prefs.getInt(KEY_USER_ID, -1); }
    public int getAgencyId() { return prefs.getInt(KEY_AGENCY_ID, -1); }
    public String[] getRoles() {
        String saved = prefs.getString(KEY_ROLE, "");
        return saved.isEmpty() ? new String[0] : saved.split(",");
    }

    public void clearSession() { editor.clear().apply(); }
}