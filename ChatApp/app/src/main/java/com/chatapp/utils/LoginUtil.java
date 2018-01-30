package com.chatapp.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LoginUtil extends PreferenceUtil {
    private static final String IS_LOGGED_IN = "is_logged_in";
    private static final String FIRE_BASE_TOKEN = "fire_base_token";


    @Inject
    public LoginUtil(){
    }
    public boolean isLoggedIn(Context context) {

        return getSharedPreferance(context).getBoolean(IS_LOGGED_IN, false);
    }

    public void setLoggedInStatus(Context context) {
        Editor editor = getPreferenceEditor(context);
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.commit();
    }


    public void setLoggedOutStatus(Context context) {
        Editor editor = getPreferenceEditor(context);
        editor.putBoolean(IS_LOGGED_IN, false);
        editor.commit();
    }

    public void setFRBAccessToken(Context context, String token) {
        Editor editor = getPreferenceEditor(context);
        editor.putString(FIRE_BASE_TOKEN, token);
        editor.commit();
    }

    public String getFBAccessToken(Context context) {
        return getSharedPreferance(context).getString(FIRE_BASE_TOKEN, "");
    }


}
