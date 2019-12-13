package com.example.foodsetgo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;



public class SharedPreferencesApp {

    //SessionState will be equal to
    // "User" if already loggedin as user
    // "Owner" if already loggedin as owner
    // "NULL" otherwise

    static final String SessionState= "NULL";
    static  SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setSessionState(Context ctx, String sessionState)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(SessionState, sessionState);
        editor.commit();
    }



    public static String getSessionState(Context ctx)
    {
        return getSharedPreferences(ctx).getString(SessionState, "NULL");
    }


}