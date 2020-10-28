package com.basulvyou.system.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import com.basulvyou.system.entity.ConfigEntity;


/**
 * 网络地址，缓存数据工具类
 * Created by KevinLi on 2016/2/19.
 */
public class ConfigUtil {
//    public static final String HTTP = "http://192.168.3.132:8080/iecp_bs";
    public static final String HTTP = "http://60.174.234.249:8136";
    public static final String NEWS_HTTP = "http://www.bgncds.com/service";
    public static final String HTTP_URL = HTTP + "/service/bs";
    public static final String HTTP_CART_URL = HTTP_URL + "/AppMyCartInfo.do?key=";
    public static final String HTTP_URL_BAIDU_MAP = "http://api.map.baidu.com/staticimage/v2" +
            "?ak=c0QRoIGQMUEOyrAcn6MtMtN3";
    public static final String HTTP_URL_NEWS_DETAIL = NEWS_HTTP +"/AppLocalInfoView!view.do?id=";//新闻详情地址
    public static final String HTTP_GOOD_DESCRIBE = HTTP_URL +"/AppEntpInfo.do?method=viewDetails&comm_id=";//商品详情
    public static final String HTTP_GROUP_GOOD_DESCRIBE = HTTP_URL +"/AppEntpInfo.do?method=viewTgDetails&comm_id=";//团购商品详情
    public static final String HTTP_GOOD_COMMENT = HTTP_URL +"/AppEntpInfo.do?method=getCommentList&comm_id=";//商品评论
    public static final String HTTP_VIRTUAL_BUY = HTTP_URL +"/AppMyVirtualCartInfo.do?method=virtualBuying&key=";//虚拟商品提交订单页面
    public static final String HTTP_GROUP_BUY = HTTP_URL +"/AppMyCartInfo.do?method=groupBuying&key=";//团购商品提交订单页面
    public static final String HTTP_COUPON_CHANGE = HTTP_URL +"/AppEntpInfo.do?method=getEntpYhqList&key=";//优惠券领取页面
    public static final String HTTP_ASK_BAR = HTTP_URL +"/WapIndex.do?method=wenbalist";//问吧地址
//    public static final String HTTP_WEB_WEATHER = "http://e.weather.com.cn/d/detail/101140508.shtml";//天气web
    public static final String HTTP_WEB_WEATHER = "http://e.weather.com.cn/d/detail/101060501.shtml";//天气web
    public static final String HTTP_PAY_WEB =HTTP_URL + "/AppIndexPayment.do?";//订单支付页面
    public static final String HTTP_SHARE_DETAIL_WEB =HTTP_URL + "/FriendPublishContent.do?method=view&id=";//分享详情页面
    private static final String USER_NAME = "";
    private static final String KEY = "";
    private static final String PROVINCE = " ";
    private static final String ADDRESS = "";
    private static final String LEVEL = "";
    private static final String KUAIADDRESS = "";//用户选择的收货地址
    private static final String KUAIADDRESSCODE = "";//用户选择的收货地址编码
    private static final String SEARCH_HISTORY = "";//用户搜索历史
    private static final String POINT = "";//用户积分
    private static final boolean IS_LOGIN = false;
    private static final boolean IS_FIRST = true;
    public static String phoneIMEI = "";
    public static  boolean IS_DOWNLOAD = false;
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
        entity.key = share.getString("key", KEY);
        entity.province = share.getString("province", PROVINCE);
        entity.address = share.getString("address", ADDRESS);
        entity.level = share.getString("level", LEVEL);
        entity.isLogin = share.getBoolean("isLogin", IS_LOGIN);
        entity.isFirst = share.getBoolean("isFirst", IS_FIRST);
        entity.kuaiAddress = share.getString("kuaiAddress", KUAIADDRESS);
        entity.kuaiAddressCode = share.getString("kuaiAddressCode", KUAIADDRESSCODE);
        entity.searchHistory = share.getString("searchHistory", SEARCH_HISTORY);
        entity.point = share.getString("point", POINT);
        if(StringUtil.isEmpty(ConfigUtil.phoneIMEI)){
            TelephonyManager TelephonyMgr = (TelephonyManager) cxt.getSystemService(Context.TELEPHONY_SERVICE);
            ConfigUtil.phoneIMEI = TelephonyMgr.getDeviceId();//获取手机唯一识别码
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
        editor.putString("key", entity.key);
        editor.putString("province", entity.province);
        editor.putString("address", entity.address);
        editor.putString("level", entity.level);
        editor.putBoolean("isLogin", entity.isLogin);
        editor.putBoolean("isFirst", entity.isFirst);
        editor.putString("kuaiAddress", entity.kuaiAddress);
        editor.putString("kuaiAddressCode", entity.kuaiAddressCode);
        editor.putString("searchHistory", entity.searchHistory);
        editor.putString("point", entity.point);
        editor.commit();
    }

}
