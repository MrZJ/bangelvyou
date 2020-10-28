package com.shishoureport.system.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.shishoureport.system.entity.User;


/**
 * Created by jianzhang.
 * on 2017/7/24.
 * copyright easybiz.
 */

public class MySharepreference {
    public static final String DATA_HOLDER = "data.holder";
    public static final String USER = "user";
    public static final String ENTITY90LIST = "entity90list";
    public static final String ENTITY100LIST = "entity100list";
    public static final String ENTITY110LIST = "entity110list";
    public static final String ENTITY500LIST = "entity500list";
    public static final String ENTITY_USER = "entity_user";
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

    public void savaUser(String value) {
        putString(USER, value);
    }


    public User getUser() {
        String userJson = getString(USER);
        User user = new User();
        try {
            user = JSONObject.parseObject(userJson, User.class);
        } catch (Exception e) {
        }
        return user;
    }

    public void clearUser() {
        SharedPreferences.Editor editor = mPrerence.edit();
        editor.putString(USER, "");
        editor.apply();
    }

    public boolean isLogin() {
        return !StringUtil.isEmpty(getUser().id);
    }


}
