package com.shishoureport.system.request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/12.
 * copyright easybiz.
 */

public class AttandenceRequest extends BaseRequest {
    private static final String METHOD = "/mobile/sheet/save";
    public static final int ATTANDENCE_REQUEST = 100060;
    private String user_id, lat, lng, is_no, is_cd, is_zt, in_or_out, punch_point, rmark, id;

    public AttandenceRequest(String id, String user_id, double lat, double lng, int is_no, int is_cd, int is_zt, int in_or_out, String punch_point, String rmark) {
        super();
        this.id = id;
        this.user_id = user_id;
        this.lat = lat + "";
        this.lng = lng + "";
        this.is_no = is_no + "";
        this.is_cd = is_cd + "";
        this.is_zt = is_zt + "";
        this.in_or_out = in_or_out + "";
        this.punch_point = punch_point;
        this.rmark = rmark + "";
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("lat", lat);
        map.put("lng", lng);
        map.put("is_no", is_no);
        map.put("is_cd", is_cd);
        map.put("is_zt", is_zt);
        map.put("in_or_out", in_or_out);
        map.put("punch_point", punch_point + "");
        map.put("rmark", rmark);
        if (id != null) {
            map.put("id", id);
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
