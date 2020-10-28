package com.fuping.system.entity;

/**
 * Created by jianzhang.
 * on 2017/10/24.
 * copyright easybiz.
 */

public class PeopleDetailEntity extends BaseEntity {
    public String poor_id;//贫困户id
    public String real_name;// 贫困户姓名
    public String age;// 贫困户年龄
    public String id_card;// 贫困户身份证
    public String mobile;//贫困户电话
    public String poor_reson;// 贫困户致贫原因
    public String real_addr;//地址
    public String familyInfo_count;// 成员数量
    public String bfdw_name;//帮扶单位
    public String bfr_name;// 帮扶人姓名
    public String bfr_tel;// 帮扶人联系方式
    public String image_path;

    public int is_have_eat = -1;//    '不愁吃，0是，不是',
    public int is_have_wear = -1;//     '不愁穿，0是，不是',
    public int is_have_drop_out = -1;//  '有无辍学情况,0无，有',
    public int is_have_medical = -1;//  '是否参加相关医疗，是，0否',
    public int is_have_house = -1;//   '是否有安全住房，是，0否',
    public int house_c_reform = -1;//   '是否c级改造房，是，0否',
    public int house_d_create = -1;//    '是否d级新建房，是，0否',
    public int house_have_agree = -1;//  '有无租房协议，0没有，有',
    public int houser_have_report = -1;//'房屋是否有资质报告，有，0无',
    public int is_have_water = -1;//  '是否有安全饮水问题，有，0无',
    public int water_have_report = -1;//  '有无检测报告，0无，有',
    public int water_have_create = -1;// '有无新打井，0无，有',
    public int water_have_line = -1;//  '是否有管线安装，0否，是',
    public int water_have_device = -1;//'是否有过滤设备，0否，是',
    public int year_pserson_income = -1;// '年人均收入是否达标，0否，是',
    public String income_money;//		'年人均收入',
    public int is_have_help = -1;//		'是否有脱贫举措，0无，有',
    public String help_method;//    '具体举措方法说明',
    public int is_have_policy = -1;// '是否有政策保障，0无，有',
    public String policy_method;// '具体政策',
    public String inspection_manage;// '督查组长',
    public String inspection_person;//'督查人员',
    public String process_percent;//'督查进度百分比',

    public int is_have_help_degree = -1;//    '帮扶工作群众满意度达到100%，0无，1有',
    public int is_have_help_link_card = -1;//    '帮扶联系卡，0无，1有',
    public int is_have_content_whole = -1;//    '内容填写齐全，0否，1是',
    public int is_have_poor_reson_same = -1;//    '致贫原因和实际相符，0否，1是',
    public int is_have_industry = -1;//    '产业发展措施，0否，1是',
    public int is_have_reson_cushi_same = -1;//    '致贫原因和帮扶措施一致，0否，1是',
    public int is_have_help_fill_whole = -1;//    '帮扶手册填写齐全,0无，1有',
    public int is_have_five_whole = -1;//    '五卡收集齐全,0否，1是',
    public int is_have_help_ledger_whole = -1;//    '帮扶台账填写齐全，0否，1是',
    public int is_have_income_whole = -1;//    '收支流水账填写齐全，0否，1是',
    public int is_have_tuopin_whole = -1;//    '脱贫台账填写齐全，0否，1是',
    public int is_have_tuopin_solid_whole = -1;//    '脱贫巩固账填写齐全，0否，1是',
    public int is_have_true = -1;//    '档案填写准确率达到100%，0否，1是',
}
