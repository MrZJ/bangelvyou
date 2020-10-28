package com.fuping.system.request;

import com.fuping.system.entity.CountryDuChaEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/10/11.
 * copyright easybiz.
 */

public class SaveCountryInspectRequest extends BaseRequest {
    private static final String METHOD = "/MInspection.do?method=saveInspectionVillage";
    public static final int SAVE_COUNTRY_INSPECT_REQUEST = 100021;
    private String user_id, village_id;
    private CountryDuChaEntity entity;

    public SaveCountryInspectRequest(String user_id, String village_id, CountryDuChaEntity entity) {
        super();
        this.user_id = user_id;
        this.village_id = village_id;
        this.entity = entity;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("village_id", village_id);
        map.put("is_low_process", entity.is_low_process + "");
        map.put("is_have_cement", entity.is_have_cement + "");
        map.put("is_have_net", entity.is_have_net + "");
        map.put("is_have_watch", entity.is_have_watch + "");
        map.put("is_have_clinic", entity.is_have_clinic + "");
        map.put("is_have_doctor", entity.is_have_doctor + "");
        map.put("is_have_active_address", entity.is_have_active_address + "");
        map.put("is_have_farm_guide", entity.is_have_farm_guide + "");
        map.put("farm_guide_desc", entity.farm_guide_desc + "");
        map.put("is_have_industry_guide", entity.is_have_industry_guide + "");
        map.put("industry_guide_desc", entity.industry_guide_desc + "");
        map.put("is_have_three_industry", entity.is_have_three_industry + "");
        map.put("three_industry_desc", entity.three_industry_desc + "");
        map.put("is_have_pv_power", entity.is_have_pv_power + "");
        map.put("pv_power_desc", entity.pv_power_desc + "");
        map.put("is_have_finance", entity.is_have_finance + "");
        map.put("finance_desc", entity.finance_desc + "");
        map.put("is_have_workers", entity.is_have_workers + "");
        map.put("workers_desc", entity.workers_desc + "");
        map.put("is_have_online", entity.is_have_online + "");
        map.put("online_desc", entity.online_desc + "");
        map.put("is_have_courtyard", entity.is_have_courtyard + "");
        map.put("courtyard_desc", entity.courtyard_desc + "");
        map.put("is_have_tourism", entity.is_have_tourism + "");
        map.put("tourism_desc", entity.tourism_desc + "");
        map.put("is_have_piety", entity.is_have_piety + "");
        map.put("piety_desc", entity.piety_desc + "");
        map.put("inspection_manage", entity.inspection_manage + "");
        map.put("inspection_person", entity.inspection_person + "");

        map.put("is_have_tuopin_storm_plan", entity.is_have_tuopin_storm_plan + "");
        map.put("is_have_base_record", entity.is_have_base_record + "");
        map.put("is_have_base_datas", entity.is_have_base_datas + "");
        map.put("is_have_poor_counts", entity.is_have_poor_counts + "");
        map.put("is_have_industry", entity.is_have_industry + "");
        map.put("is_have_tuopin_date", entity.is_have_tuopin_date + "");
        map.put("is_have_group_mechanism", entity.is_have_group_mechanism + "");
        map.put("is_have_tuopin_schematic_board", entity.is_have_tuopin_schematic_board + "");
        map.put("is_have_poorandpersons_counts", entity.is_have_poorandpersons_counts + "");
        map.put("is_have_register_counts", entity.is_have_register_counts + "");
        map.put("is_have_resident_counts", entity.is_have_resident_counts + "");
        map.put("is_have_out_person_counts", entity.is_have_out_person_counts + "");
        map.put("is_avhe_out_marriage_counts", entity.is_avhe_out_marriage_counts + "");
        map.put("is_have_in_marriage_counts", entity.is_have_in_marriage_counts + "");
        map.put("is_have_birth_counts", entity.is_have_birth_counts + "");
        map.put("is_have_die_counts", entity.is_have_die_counts + "");
        map.put("is_have_data_update", entity.is_have_data_update + "");
        map.put("is_have_group_record", entity.is_have_group_record + "");
        map.put("is_have_group_video_file", entity.is_have_group_video_file + "");
        map.put("is_have_person_record", entity.is_have_person_record + "");
        map.put("is_have_now_video_file", entity.is_have_now_video_file + "");
        map.put("is_have_person_vote", entity.is_have_person_vote + "");
        map.put("is_have_audit_report", entity.is_have_audit_report + "");
        map.put("is_have_one_public", entity.is_have_one_public + "");
        map.put("is_have_one_name_list", entity.is_have_one_name_list + "");
        map.put("is_have_video_file", entity.is_have_video_file + "");
        map.put("is_have_two_public", entity.is_have_two_public + "");
        map.put("is_have_two_name_list", entity.is_have_two_name_list + "");
        map.put("is_have_two_video_file", entity.is_have_two_video_file + "");
        map.put("is_have_file_complete", entity.is_have_file_complete + "");
        map.put("is_have_file_box", entity.is_have_file_box + "");
        map.put("is_have_one_file", entity.is_have_one_file + "");
        map.put("is_have_file_cab", entity.is_have_file_cab + "");

        map.put("is_low_poor_p", entity.is_low_poor_p + "");
        map.put("is_low_no_p", entity.is_low_no_p + "");
        return map;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD);
        return builder.toString();
    }
}
