package com.fuping.system.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import com.fuping.system.entity.ConfigEntity;


/**
 * 网络地址，缓存数据工具类
 * Created by KevinLi on 2016/2/19.
 */
public class ConfigUtil {
    //    public static final String HTTP = "http://192.168.3.5:8449";
//        public static final String HTTP = "http://192.168.3.74:8082/jingzhun_fupin";//张强测试
//    public static final String HTTP = "http://60.174.233.26:8099";//杜彪 外网
    public static final String HTTP = "http://60.174.233.26:8100";//张强 外网
    //    public static final String HTTP = "http://192.168.3.70:8088/jingzhun_fupin";
    public static final String HTTP_URL = HTTP + "/m";
    public static final String HTTP_URL_NOTICE_DETAIL = HTTP_URL + "/MNewsInfo.do?id=";//公告详情页面
    public static final String HTTP_URL_SCAN_DETAIL = HTTP + "/website/TraceBackWap.do?";//扫描详情页面
    public static final String HTTP_URL_PLAN_MAG = HTTP_URL + "/MFpjhInfo.do";//计划管理
    public static final String HTTP_URL_PLAN_MAG_3 = HTTP_URL + "/MLog.do?method=list&type=3";//帮扶人计划管理
    public static final String HTTP_URL_HELP_OBJECT = HTTP_URL + "/MPoorList.do";//帮扶对象
    public static final String HTTP_URL_LOGIN_MAG = HTTP_URL + "/MLog.do";//日志管理
    public static final String HTTP_URL_MAIL = HTTP_URL + "/MContact.do";//通讯录
    public static final String HTTP_URL_PERSON_CHANNEL = HTTP_URL + "/MQaInfo.do";//民声通道
    public static final String HTTP_URL_MINE_INFO = HTTP_URL + "/MPoorer.do";//个人信息
    public static final String HTTP_URL_MINE_HELP = HTTP_URL + "/MPoorer.do?method=list_bfr";//我的帮扶人
    public static final String HTTP_URL_ABOUT_US = HTTP_URL + "/MAboutUs.do";//关于我们
    private static final String USER_NAME = "";
    private static final String USER_TYPE = "";
    private static final String PASS_WORD = "";
    private static final String KEY = "";
    private static final String PROVINCE = "";
    private static final String ADDRESS = "";
    private static final boolean IS_LOGIN = false;
    private static final boolean IS_FIRST = true;
    public static String phoneIMEI = "";
    public static boolean IS_DOWNLOAD = false;


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
        entity.usertype = share.getString("userType", USER_TYPE);
        entity.passwordMD5 = share.getString("password", PASS_WORD);
        entity.key = share.getString("key", KEY);
        entity.province = share.getString("province", PROVINCE);
        entity.address = share.getString("address", ADDRESS);
        entity.isLogin = share.getBoolean("isLogin", IS_LOGIN);
        entity.isFirst = share.getBoolean("isFirst", IS_FIRST);
        if (StringUtil.isEmpty(ConfigUtil.phoneIMEI)) {
            TelephonyManager TelephonyMgr = (TelephonyManager) cxt.getSystemService(Context.TELEPHONY_SERVICE);
            ConfigUtil.phoneIMEI = TelephonyMgr.getDeviceId();//获取手机唯一识别码
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
        editor.putString("userType", entity.usertype);
        editor.putString("password", entity.passwordMD5);
        editor.putString("key", entity.key);
        editor.putString("province", entity.province);
        editor.putString("address", entity.address);
        editor.putBoolean("isLogin", entity.isLogin);
        editor.putBoolean("isFirst", entity.isFirst);
        editor.commit();
    }

}
