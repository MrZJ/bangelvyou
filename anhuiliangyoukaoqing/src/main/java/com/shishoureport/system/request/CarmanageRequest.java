package com.shishoureport.system.request;

import com.shishoureport.system.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/13.
 * copyright easybiz.
 */

public class CarmanageRequest extends BaseRequest {
    private static final String METHOD = "/mobile/attendanceBus/save";
    public static final int CAR_MANAGE_REQUEST = 1002217;
    private String bus_number, start_time, end_time, days, peer_person, start_place, end_place, use_car_desc, number, user_ids, user_names, cc_user_ids,
            cc_user_names, user_id, rmark;

    public CarmanageRequest(String bus_number, String start_time, String end_time, String days, String peer_person, String start_place
            , String end_place, String use_car_desc, String number, String user_ids, String user_names,
                            String cc_user_ids, String cc_user_names, String user_id, String rmark) {
        super();
        this.bus_number = bus_number;
        this.start_time = start_time;
        this.end_time = end_time;
        this.days = days;
        this.peer_person = peer_person;
        this.start_place = start_place;
        this.end_place = end_place;
        this.use_car_desc = use_car_desc;
        this.number = number;
        this.user_ids = user_ids;
        this.user_names = user_names;
        this.cc_user_ids = cc_user_ids;
        this.cc_user_names = cc_user_names;
        this.user_id = user_id;
        this.rmark = rmark;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("bus_number", bus_number);
        hashMap.put("start_time", start_time);
        hashMap.put("end_time", end_time);
        hashMap.put("days", days);
        hashMap.put("peer_person", peer_person);
        hashMap.put("start_place", start_place);
        hashMap.put("end_place", end_place);
        hashMap.put("use_car_desc", use_car_desc);
        if (!StringUtil.isEmpty(rmark)) {
            hashMap.put("rmark", rmark);
        }
        if (!StringUtil.isEmpty(number)) {
            hashMap.put("number", number);
        }
        hashMap.put("user_ids", user_ids);
        hashMap.put("user_names", user_names);
        if (!StringUtil.isEmpty(cc_user_ids)) {
            hashMap.put("cc_user_ids", cc_user_ids);
        } else {
            hashMap.put("cc_user_ids", "");
        }
        if (!StringUtil.isEmpty(cc_user_names)) {
            hashMap.put("cc_user_names", cc_user_names);
        } else {
            hashMap.put("cc_user_names", "");
        }
        hashMap.put("user_id", user_id);
        hashMap.put("is_submit", "1");
        return hashMap;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD);
        return builder.toString();
    }
}
