package com.shishoureport.system.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.shishoureport.system.entity.ConfigEntity;


/**
 * 网络地址，缓存数据工具类
 */
public class ConfigUtil {
            public static final String HTTP = "http://220.178.52.206:8666";//外网地址
    //    public static final String HTTP = "http://192.168.3.5:8495";//内网测试地址
    //    public static final String HTTP = "http://192.168.3.26:8084";//杜彪测试地址
//        public static final String HTTP = "http://192.168.3.5:8447";//王亚丽测试地址
//    public static final String HTTP = "http://192.168.3.74:8084";//张强测试地址
    //    public static final String HTTP = "http://192.168.3.56:8084";//胡文康测试地址
    public static final String HTTP_URL = HTTP + "";
    public static final String HTTP_MINE_URL = HTTP_URL + "/MMyHome.do";//我的
    //    public static final String HTTP = "http://220.178.14.5:8447";//杜彪
    public static final String HTTP_SEARCH_URL = HTTP_URL + "/WapSearch!search.do";//搜索
    private static final String USER_NAME = "";
    private static final String KEY = "";
    private static final boolean IS_LOGIN = false;
    private static final boolean IS_FIRST = true;
    public static String phoneIMEI = "";

    /**
     * 加载配置信息
     *
     * @param cxt Context
     * @return 配置实体对象
     */
    public static ConfigEntity loadConfig(Context cxt) {
        ConfigEntity entity = new ConfigEntity();
        SharedPreferences share = cxt.getSharedPreferences("ConfigPerference", Context.MODE_PRIVATE);
        entity.username = share.getString("userName", USER_NAME);
        entity.key = share.getString("key", KEY);
        entity.isLogin = share.getBoolean("isLogin", IS_LOGIN);
        entity.isFirst = share.getBoolean("isFirst", IS_FIRST);
        if (StringUtil.isEmpty(ConfigUtil.phoneIMEI)) {
//            phoneIMEI = UmengRegistrar.getRegistrationId(cxt);
        }
        return entity;
    }

    /**
     * 保存配置信息
     *
     * @param cxt    Context
     * @param entity 配置实体对象
     */
    public static void saveConfig(Context cxt, ConfigEntity entity) {
        SharedPreferences share = cxt.getSharedPreferences("ConfigPerference",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("userName", entity.username);
        editor.putString("key", entity.key);
        editor.putBoolean("isLogin", entity.isLogin);
        editor.putBoolean("isFirst", entity.isFirst);
        editor.commit();
    }

    /**
     * 清除配置信息
     *
     * @param cxt Context
     *            配置实体对象
     */
    public static void clearConfig(Context cxt) {
        SharedPreferences share = cxt.getSharedPreferences("ConfigPerference",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("userName", "");
        editor.putString("key", "");
        editor.putBoolean("isLogin", false);
        editor.putBoolean("isFirst", false);
        editor.commit();
    }

}
