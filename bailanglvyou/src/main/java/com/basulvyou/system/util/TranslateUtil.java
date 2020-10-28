package com.basulvyou.system.util;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.basulvyou.system.entity.TranslateEntity;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/8/23.
 * copyright easybiz.
 */

public class TranslateUtil {
    public interface TranslateInterface {
        void onTranslateResult(boolean success, String result);
    }

    public static final int SEARCH_TYPE_ZH_TO_TI = 1;
    public static final int SEARCH_TYPE_TI_TO_ZH = 2;
    public static final String APPCODE = "a82d9e891a8046b3a4701b522e6ac9d8";
    public static final String APPSECRET = "5c76b2574e89bdc5ebbfa8f21cf1bb97";

    public static void translate(String translates, int search_type, TranslateInterface listener) {
        String host = "http://niutrans1.market.alicloudapi.com";
        String path = "/NiuTransServer/translation";
        String method = "GET";
        String appcode = APPCODE;
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        String s = "";
        try {
//            s = URLEncoder.encode(translates, "utf-8");
//            s = "%E4%BD%A0%E5%A5%BD";
        } catch (Exception e) {

        }
        if (SEARCH_TYPE_ZH_TO_TI == search_type) {
            querys.put("from", "zh");
            querys.put("src_text", translates);
            querys.put("to", "ti");
        } else {
            querys.put("from", "ti");
            querys.put("src_text", translates);
            querys.put("to", "zh");
        }
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            System.out.println(response.toString());
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                TranslateEntity entity = JSONObject.parseObject(strResult, TranslateEntity.class);
                if (listener != null) {
                    if (entity != null && !StringUtil.isEmpty(entity.tgt_text)) {
//                        String res = URLEncoder.encode(translates, "utf-8");
                        Log.e("jianzhang", entity.tgt_text);
                        listener.onTranslateResult(true, entity.tgt_text);
                    } else {
                        listener.onTranslateResult(false, "");
                    }
                }
                Log.e("jianzhang", strResult);
            } else {
                if (listener != null) {
                    listener.onTranslateResult(false, "");
                }
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onTranslateResult(false, "");
            }
            e.printStackTrace();
        }
    }
}
