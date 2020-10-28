package com.basulvyou.system.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


/**
 * Created by jianzhang.
 * on 2017/7/24.
 * copyright easybiz.
 */

public class MySharepreference {
    public static final String DATA_HOLDER = "data.holder";
    public static final String USER = "user";
    public static final String TOKEN = "token";
    public static final String LOGIN_NAME = "login_name";
    public static final String IS_SEL_LOGIN = "is_sel_login";
    public static final String LOGIN_PWD = "login_pwd";
    public static final String USER_PHOTO = "user_photo";
    private SharedPreferences mPrerence;
    private static MySharepreference mySharepreference;

    public static synchronized MySharepreference getInstance(Context context) {
        if (context != null) {
            mySharepreference = new MySharepreference(context);
        }
        return mySharepreference;
    }

    private MySharepreference(Context context) {
        mPrerence = context.getApplicationContext().getSharedPreferences(DATA_HOLDER, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = mPrerence.edit();
        editor.putString(key, value);
        Log.e("jianzhang", "value = " + value);
        editor.commit();
    }

    public String getString(String key) {
        return mPrerence.getString(key, "");
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = mPrerence.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        return mPrerence.getInt(key, 0);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mPrerence.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean value) {
        return mPrerence.getBoolean(key, value);
    }

    public boolean putToken(String token) {
        SharedPreferences.Editor editor = mPrerence.edit();
        editor.putString(TOKEN, token);
        Log.e("jianzhang", "value = " + token);
        return editor.commit();
    }

    public String getToken() {
        return mPrerence.getString(TOKEN, "");
    }

    public void savaUser(String value) {
        putString(USER, value);
    }



    public void clearUser() {
        SharedPreferences.Editor editor = mPrerence.edit();
        editor.putString(USER, "");
        editor.putString(TOKEN, "");
        editor.apply();
    }

}
