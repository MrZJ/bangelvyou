package com.shenmai.system.utlis;

import android.content.Context;
import android.content.SharedPreferences;

import com.shenmai.system.entity.ConfigEntity;
import com.umeng.message.UmengRegistrar;


/**
 * 网络地址，缓存数据工具类
 * Created by KevinLi on 2016/2/19.
 */
public class ConfigUtil {
    public static final String HTTP = "http://test.mymyty.com";//内网测试地址http://192.168.3.16:8080/sd_ly/service/
//    public static final String HTTP = "http://192.168.3.16:8088/mmt_base";//内网测试地址http://192.168.3.16:8080/sd_ly/service/
//    public static final String HTTP = "http://192.168.3.193:8085/mmt_base";//内网测试地址http://192.168.3.16:8080/sd_ly/service/
//    public static final String HTTP = "http://192.168.1.102:8081";//内网测试地址http://192.168.3.16:8080/sd_ly/service/
//    public static final String HTTP = "http://www.mymyty.com";//内网测试地址http://192.168.3.16:8080/sd_ly/service/
    public static final String HTTP_URL = HTTP + "/service";
    public static final String HTTP_CART_URL = HTTP_URL + "/MMyCartInfoPt.do?key=";
    public static final String HTTP_GOODS_DETAIL_URL = HTTP_URL + "/MEntpInfo.do?";
    public static final String HTTP_GOODS_OPENSHOP_NOTICE = HTTP_URL + "/MNewsInfo.do?method=getViewByModId&mod_id=1019015005";//开店提示
    public static final String HTTP_GOODS_SHOP_PROT = HTTP_URL + "/MNewsInfo.do?method=getViewByModId&mod_id=1019015001";//神买协议
    public static final String HTTP_GOODS_ORDER = HTTP_URL + "/MMyOrder.do?method=list&order_type=10";//我的订单
    public static final String HTTP_MINE_NOTICE = HTTP_URL + "/MMyMsg.do";//消息中心
    public static final String HTTP_MINE_ABOUTS = HTTP_URL + "/MNewsInfo.do?method=getViewByModId&mod_id=1014002000";//关于我们
    public static final String HTTP_SHOP_LEVEL = HTTP_URL + "/MMyLowerLevel.do";//我的二级
    public static final String HTTP_SHOP_MENOY = HTTP_URL + "/MMyYongJing.do";//我的营收
    public static final String HTTP_SHOP_LEARN = HTTP_URL + "/MNewsInfo.do?method=list";//微学堂
    public static final String HTTP_MINE_HELP = HTTP_URL + "/MHelpDocument.do?method=index&h_mod_id=10010100";//帮助中心
    public static final String HTTP_SHOP_SEE = HTTP_URL + "/MFxsInfo.do?method=index&entp_id=2&key=";//我的小店预览
    private static final String USER_NAME = "";
    private static final String USER_ROLE = "";
    private static final String PASS_WORD = "";
    private static final String KEY = "";
    private static final String PROVINCE = "";
    private static final String ADDRESS = "";
    private static final boolean IS_LOGIN = false;
    private static final boolean IS_FIRST = true;
    public static String phoneIMEI = "";

    /**
     * 加载配置信息
     *
     * @param cxt
     *            Context
     * @return 配置实体对象
     */
    public static ConfigEntity loadConfig(Context cxt) {
        ConfigEntity entity = new ConfigEntity();
        SharedPreferences share = cxt.getSharedPreferences("ConfigPerference",Context.MODE_PRIVATE);
        entity.username = share.getString("userName", USER_NAME);
        entity.userRole = share.getString("userRole", USER_ROLE);
        entity.passwordMD5 = share.getString("password", PASS_WORD);
        entity.key = share.getString("key", KEY);
        entity.province = share.getString("province", PROVINCE);
        entity.address = share.getString("address", ADDRESS);
        entity.isLogin = share.getBoolean("isLogin", IS_LOGIN);
        entity.isFirst = share.getBoolean("isFirst", IS_FIRST);
        if(StringUtil.isEmpty(ConfigUtil.phoneIMEI)){
            /*TelephonyManager TelephonyMgr = (TelephonyManager) cxt.getSystemService(Context.TELEPHONY_SERVICE);
            ConfigUtil.phoneIMEI = TelephonyMgr.getDeviceId();//获取手机唯一识别码*/
            phoneIMEI = UmengRegistrar.getRegistrationId(cxt);
        }
        return entity;
    }

    /**
     * 保存配置信息
     *
     * @param cxt
     *            Context
     * @param entity
     *            配置实体对象
     */
    public static void saveConfig(Context cxt, ConfigEntity entity){
        SharedPreferences share = cxt.getSharedPreferences("ConfigPerference",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("userName", entity.username);
        editor.putString("userRole", entity.userRole);
        editor.putString("password", entity.passwordMD5);
        editor.putString("key", entity.key);
        editor.putString("province", entity.province);
        editor.putString("address", entity.address);
        editor.putBoolean("isLogin", entity.isLogin);
        editor.putBoolean("isFirst", entity.isFirst);
        editor.commit();
    }

    /**
     * 清除配置信息
     *
     * @param cxt
     *            Context
     *            配置实体对象
     */
    public static void clearConfig(Context cxt){
        SharedPreferences share = cxt.getSharedPreferences("ConfigPerference",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("userName", "");
        editor.putString("userRole", "");
        editor.putString("password", "");
        editor.putString("key", "");
        editor.putString("province", "");
        editor.putString("address", "");
        editor.putBoolean("isLogin", false);
        editor.putBoolean("isFirst", false);
        editor.commit();
    }

}
