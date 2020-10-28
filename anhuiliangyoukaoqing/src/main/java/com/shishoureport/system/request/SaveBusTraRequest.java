package com.shishoureport.system.request;

import com.shishoureport.system.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/10.
 * copyright easybiz.
 */

public class SaveBusTraRequest extends BaseRequest {
    private static final String METHOD = "/mobile/attendancetravel/save";
    public static final int SAVE_BUS_TRA_REQUEST = 100016;
    private String user_ids;
    private String user_names;
    private String is_submit = "1";
    private String user_id;
    private String p_name;
    private String start_time;
    private String end_time;
    private String business_purpose;
    private String rmark;
    private String fellow_people;
    private String departure_time;
    private String temp_travel_money;
    private String company_head_uname;
    private String business_route, cc_user_names, cc_user_ids;


    public SaveBusTraRequest(String user_id, String user_names, String user_ids, String p_name, String start_time,
                             String end_time, String fellow_people, String business_purpose, String rmark,
                             String departure_time, String temp_travel_money, String company_head_uname, String business_route, String cc_user_names, String cc_user_ids) {
        super();
        this.user_id = user_id;
        this.user_names = user_names;
        this.user_ids = user_ids;
        this.p_name = p_name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.fellow_people = fellow_people;
        this.business_purpose = business_purpose;
        this.rmark = rmark;
        this.departure_time = departure_time;
        this.temp_travel_money = temp_travel_money;
        this.company_head_uname = company_head_uname;
        this.business_route = business_route;
        this.cc_user_names = cc_user_names;
        this.cc_user_ids = cc_user_ids;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("user_names", user_names);
        map.put("user_ids", user_ids);
        map.put("p_name", p_name);
        map.put("start_time", start_time);
        map.put("end_time", end_time);
        if (!StringUtil.isEmpty(fellow_people)) {
            map.put("fellow_people", fellow_people);
        }
        map.put("business_purpose", business_purpose);
        map.put("is_submit", is_submit);
        map.put("rmark", rmark);
        map.put("departure_time", departure_time);
        if (!StringUtil.isEmpty(temp_travel_money)) {
            map.put("temp_travel_money", temp_travel_money);
        }
        map.put("company_head_uname", company_head_uname);
        if (!StringUtil.isEmpty(business_route)) {
            map.put("business_route", business_route);
        }
        if (!StringUtil.isEmpty(cc_user_names)) {
            map.put("cc_user_names", cc_user_names);
        }
        if (!StringUtil.isEmpty(cc_user_ids)) {
            map.put("cc_user_ids", cc_user_ids);
        }
        return map;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD);
        return builder.toString();
    }
}
