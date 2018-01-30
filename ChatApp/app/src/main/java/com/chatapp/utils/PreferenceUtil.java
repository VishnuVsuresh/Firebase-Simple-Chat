package com.chatapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressLint("CommitPrefEdits")
public abstract class PreferenceUtil {

    private static final String SHARED_PREF = "chatApp_pref";
    private static final String GENERAL_PREF = "general_pref";
    private static final String FIRST_TIME_VISIT = "is_first_time";
    private static final String DIVISION_CD = "div_cd";

    /**
     * @param context
     * @return
     */
    protected static final SharedPreferences getGenericSharedPreferance(
            Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                GENERAL_PREF, Context.MODE_PRIVATE);
        return sharedPref;
    }

    protected static final SharedPreferences getSharedPreferance(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPref;
    }

    /**
     * @param context
     * @return
     */
    protected static final SharedPreferences.Editor getPreferenceEditor(
            Context context) {
        SharedPreferences.Editor editor = getSharedPreferance(context).edit();
        return editor;
    }

    /**
     * @param context
     * @return
     */
    protected static final SharedPreferences.Editor getGenericPreferenceEditor(
            Context context) {
        SharedPreferences.Editor editor = getGenericSharedPreferance(context)
                .edit();
        return editor;
    }

    /**
     * @param context
     */
    public static final void logout(Context context) {
        SharedPreferences.Editor sharePref = getPreferenceEditor(context);
        sharePref.clear();
        sharePref.commit();
    }


}
