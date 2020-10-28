package com.fuping.system.request;

import com.fuping.system.entity.PeopleDetailEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/11.
 * copyright easybiz.
 */

public class SavePeopleInspectRequest extends BaseRequest {
    private static final String METHOD = "/MInspection.do?method=saveInspectionPoorer";
    public static final int SAVE_COUNTRY_INSPECT_REQUEST = 100090;
    private String user_id, poor_id;
    private PeopleDetailEntity entity;

    public SavePeopleInspectRequest(String user_id, String poor_id, PeopleDetailEntity entity) {
        super();
        this.user_id = user_id;
        this.poor_id = poor_id;
        this.entity = entity;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("poor_id", poor_id);
        map.put("is_have_eat", entity.is_have_eat + "");
        map.put("is_have_wear", entity.is_have_wear + "");
        map.put("is_have_drop_out", entity.is_have_drop_out + "");
        map.put("is_have_medical", entity.is_have_medical + "");
        map.put("is_have_house", entity.is_have_house + "");
        map.put("house_c_reform", entity.house_c_reform + "");
        map.put("house_d_create", entity.house_d_create + "");
        map.put("house_have_agree", entity.house_have_agree + "");
        map.put("houser_have_report", entity.houser_have_report + "");
        map.put("is_have_water", entity.is_have_water + "");
        map.put("water_have_report", entity.water_have_report + "");
        map.put("water_have_create", entity.water_have_create + "");
        map.put("water_have_line", entity.water_have_line + "");
        map.put("water_have_device", entity.water_have_device + "");
        map.put("year_pserson_income", entity.year_pserson_income + "");
        map.put("income_money", entity.income_money + "");
        map.put("is_have_help", entity.is_have_help + "");
        map.put("help_method", entity.help_method + "");
        map.put("is_have_policy", entity.is_have_policy + "");
        map.put("policy_method", entity.policy_method + "");
        map.put("inspection_manage", entity.inspection_manage + "");
        map.put("inspection_person", entity.inspection_person + "");
        map.put("is_have_help_degree", entity.is_have_help_degree + "");
        map.put("is_have_help_link_card", entity.is_have_help_link_card + "");
        map.put("is_have_content_whole", entity.is_have_content_whole + "");
        map.put("is_have_poor_reson_same", entity.is_have_poor_reson_same + "");
        map.put("is_have_industry", entity.is_have_industry + "");
        map.put("is_have_reson_cushi_same", entity.is_have_reson_cushi_same + "");
        map.put("is_have_help_fill_whole", entity.is_have_help_fill_whole + "");
        map.put("is_have_five_whole", entity.is_have_five_whole + "");
        map.put("is_have_help_ledger_whole", entity.is_have_help_ledger_whole + "");
        map.put("is_have_income_whole", entity.is_have_income_whole + "");
        map.put("is_have_tuopin_whole", entity.is_have_tuopin_whole + "");
        map.put("is_have_tuopin_solid_whole", entity.is_have_tuopin_solid_whole + "");
        map.put("is_have_true", entity.is_have_true + "");

        return map;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD);
        return builder.toString();
    }
}
