package com.fuping.system.request;

import com.fuping.system.ui.activity.CountryDuChaActivity;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/10.
 * copyright easybiz.
 */

public class PeopleDuChaDetailRequest extends BaseRequest {
    public static final int PEOPLE_DUCHA_DETAI_LREQUEST = 1000241;
    private static final String METHOD_LIST = "/MInspection.do?method=getInspectionPoorerView";
    private static final String METHOD_DETAIL = "/MInspection.do?method=getInspectionPoorerAdd";
    private String user_id, village_id;
    private int type;

    public PeopleDuChaDetailRequest(int type, String user_id, String village_id) {
        super();
        this.type = type;
        this.user_id = user_id;
        this.village_id = village_id;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        if (type == CountryDuChaActivity.TYPE_LIST) {
            builder.append(METHOD_LIST).append("&user_id=").
                    append(user_id).append("&inspection_poor_id=").append(village_id);
        } else {
            builder.append(METHOD_DETAIL).append("&user_id=").
                    append(user_id).append("&poor_id=").append(village_id);
        }
        return builder.toString();
    }
}
