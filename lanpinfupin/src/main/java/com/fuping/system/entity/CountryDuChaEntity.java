package com.fuping.system.entity;

/**
 * Created by jianzhang.
 * on 2017/10/19.
 * copyright easybiz.
 */

public class CountryDuChaEntity extends BaseEntity {
    public static final int YES = 1;
    public static final int NO = 0;
    public String inspection_village_id;//贫困村督查数据的id
    public String inspection_poor_id;// 贫困户督查数据的id
    public String inspection_manage;//督查组长
    public String inspection_person;//督查成员
    public String process_percent;//督查进度
    public String inspection_date;//督查时间
    public int is_my;//是否有权限查看

    public int is_low_process = -1;//   '贫困发生率是否降至低于2%以下，1是，0否',
    public int is_have_cement = -1;//   '是否通水泥路,1是，0否',
    public int is_have_net = -1;//    '是否通宽带，1：是，0否',
    public int is_have_watch = -1;//   '是否通有线电视，1是，0否',
    public int is_have_clinic = -1;//   '是否有卫生室，0无，1有',
    public int is_have_doctor = -1;// '是否有合格医生，0无，1是',
    public int is_have_active_address = -1;// '是否有文化活动场所，0无，1有',
    public int is_have_farm_guide = -1;//   '有无依托农业主体引领脱贫，0无，1有',
    public String farm_guide_desc;//'引领脱贫详情',
    public int is_have_industry_guide = -1;//  '是否有依托特色产业0无，1有',
    public String industry_guide_desc;// '依托特色产业具体说明',
    public int is_have_three_industry = -1;// 'a是否依托乳，豆，水三大产业,0无，1有',
    public String three_industry_desc;// '依托乳，豆，水三大产业详情',
    public int is_have_pv_power = -1;// '是否有光伏发电，0无，1有',
    public String pv_power_desc;// '光伏发电详情',
    public int is_have_finance = -1;//   '是否有金融扶贫，0无，1有',
    public String finance_desc;//'金融扶贫详情',
    public int is_have_workers = -1;// '是否有务工增收，0无，1有',
    public String workers_desc;//'务工增收详情',
    public int is_have_online = -1;// '是否有电商扶贫，0无，1有',
    public String online_desc;//'电商扶贫详情',
    public int is_have_courtyard = -1;//  '是否有庭院经济，0无，1有',
    public String courtyard_desc;// '庭院经济详情',
    public int is_have_tourism = -1;// '是否有文化旅游,0无，1有',
    public String tourism_desc;//'文化旅游详情',
    public int is_have_piety = -1;// '是否孝亲敬老，0无，1有',
    public String piety_desc;//'孝亲敬老详情',

    public int is_have_tuopin_storm_plan = -1;//     '是否有脱贫攻坚规划，0无，1有',
    public int is_have_base_record = -1;//     '是否有基本概括',
    public int is_have_base_datas = -1;//     '是否有基础数据,0无，1有',
    public int is_have_poor_counts = -1;//     '是否有贫困人口数量,0无，1有',
    public int is_have_industry = -1;//     '是否有产业发展情况，0无，1有',
    public int is_have_tuopin_date = -1;//     '是否有脱贫年限，0无，1有',
    public int is_have_group_mechanism = -1;//     '是否有组织机构，0无，1有',
    public int is_have_tuopin_schematic_board = -1;//     '是否有脱贫攻坚图示板，0无，1有',
    public int is_have_poorandpersons_counts = -1;//     '是否有贫困户数及人口数，0无，1有',
    public int is_have_register_counts = -1;//     '是否有户籍人口数，0无，1有',
    public int is_have_resident_counts = -1;//     '是否有常驻人口数，0无，1有',
    public int is_have_out_person_counts = -1;//     '是否有外出人口数，0无，1有',
    public int is_avhe_out_marriage_counts = -1;//     '是否有婚出人口数，0无，1有',
    public int is_have_in_marriage_counts = -1;//     '是否有婚入人口数，0无，1无',
    public int is_have_birth_counts = -1;//     '是否有出生人口数，0无，1有',
    public int is_have_die_counts = -1;//     '是否有死亡人口数，0无，1有',
    public int is_have_data_update = -1;//     '是否有数据更新，0无，1有',
    public int is_have_group_record = -1;//     '是否有村民小组会议记录，0无，1有',
    public int is_have_group_video_file = -1;//     '是否有村民小组会议现场影像资料,0无，1有',
    public int is_have_person_record = -1;//     '是否有村民会议记录，0无，1有',
    public int is_have_now_video_file = -1;//     '是否有村民会议现场影像资料，0无，1有',
    public int is_have_person_vote = -1;//     '是否有村民投票，0无，1有',
    public int is_have_audit_report = -1;//     '是否有审计局数据比对结果报告,0无，1有',
    public int is_have_one_public = -1;//     '是否有第一次公示，0无，1有',
    public int is_have_one_name_list = -1;//     '是否有第一次报告名单,0无，1有',
    public int is_have_video_file = -1;//     '是否有影像资料；0无，1有',
    public int is_have_two_public = -1;//     '是否有乡镇第一次公示，0无，1有',
    public int is_have_two_name_list = -1;//   ,
    public int is_have_two_video_file = -1;//     '第二次公示是否有影像资料，0无，1有',
    public int is_have_file_complete = -1;//     '是否分类装订齐全，0否，1是',
    public int is_have_file_box = -1;//     '是否有档案盒，0无，1有',
    public int is_have_one_file = -1;//     '是否一户一档，0无，1有',
    public int is_have_file_cab = -1;//     '是否有档案柜，0无，1有',
    public int is_low_poor_p = -1;//贫困户漏评率是否降至2%以下，1是，0否
    public int is_low_no_p = -1;//贫困户错退率是否降至2%以下,1是，0否
}
