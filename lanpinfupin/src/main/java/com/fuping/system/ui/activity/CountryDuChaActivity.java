package com.fuping.system.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fuping.system.R;
import com.fuping.system.adapter.MyViewPagerAdaper;
import com.fuping.system.entity.CountryDetailAllEntity;
import com.fuping.system.entity.CountryDuChaEntity;
import com.fuping.system.request.CountryDuChaDetailRequest;
import com.fuping.system.request.CountryDuChaDetailRequest2;
import com.fuping.system.request.SaveCountryInspectRequest;
import com.fuping.system.utils.StringUtil;

import java.util.ArrayList;


/**
 * Created by jianzhang.
 * on 2017/10/18.
 * copyright easybiz.
 */

public class CountryDuChaActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    public static int TYPE_LIST = 0;
    public static int TYPE_DETAIL = 1;
    private RadioButton pkl_yes_rb, pkl_no_rb, snl_yes_rb, snl_no_rb, kd_yes_rb, kd_no_rb, yxds_yes_rb, yxds_no_rb, wss_yes_rb, wss_no_rb,
            ys_yes_rb, ys_no_rb, whhd_yes_rb, whhd_no_rb, xxny_yes_rb, xxny_no_rb, zy_yes_rb, zy_no_rb, rds_yes_rb, rds_no_rb, gffd_yes_rb, gffd_no_rb,
            jrfp_yes_rb, jrfp_no_rb, wgzs_yes_rb, wgzs_no_rb, dsfp_yes_rb, dsfp_no_rb, tyjj_yes_rb, tyjj_no_rb, whlv_yes_rb, whlv_no_rb, xqjl_yes_rb, xqjl_no_rb,
            cjtpgh_yes_rb, cjtpgh_no_rb, jbgk_yes_rb, jbgk_no_rb, jcsj_yes_rb, jcsj_no_rb, pkrksl_yes_rb, pkrksl_no_rb, cyfzqk_yes_rb, cyfzqk_no_rb,
            tpnx_yes_rb, tpnx_no_rb, zzldjg_yes_rb, zzldjg_no_rb, tpgjtsb_yes_rb, tpgjtsb_no_rb, pkhs_yes_rb, pkhs_no_rb, hjrks_yes_rb, hjrks_no_rb, czrks_yes_rb, czrks_no_rb, wcrks_yes_rb,
            wcrks_no_rb,
            hcrks_yes_rb, hcrks_no_rb, hrrk_yes_rb, hrrk_no_rb, csrks_yes_rb, csrks_no_rb, swrks_yes_rb, swrks_no_rb, sjgx_yes_rb, sjgx_no_rb, cmxjhyjl_yes_rb, cmxjhyjl_no_rb,
            cmxzhyxcyxzl_yes_rb, cmxzhyxcyxzl_no_rb, cmdbhyjl_yes_rb, cmdbhyjl_no_rb, cmdbhyxcyxzl_yes_rb, cmdbhyxcyxzl_no_rb, cmdbtpqk_yes_rb, cmdbtpqk_no_rb,
            sjjsjdbjgbg_yes_rb,
            sjjsjdbjgbg_no_rb, cjdycgs_yes_rb, cjdycgs_no_rb, bgmd_yes_rb, bgmd_no_rb, yxzl_yes_rb, yxzl_no_rb, xzdecgs_yes_rb, xzdecgs_no_rb, bgmdpf_yes_rb,
            bgmdpf_no_rb, zyxzl_yes_rb, zyxzl_no_rb,
            flzdqq_yes_rb, flzdqq_no_rb, dah_yes_rb, dah_no_rb, yhyd_yes_rb, yhyd_no_rb, dag_yes_rb, dag_no_rb, lpl_yes_rb, lpl_no_rb, cpl_yes_rb, cpl_no_rb;

    private TextView monitor_et, menber_et, xxny_et, zy_et, rds_et, gffd_et, jrfp_et, wgzs_et, dsfp_et, tyjj_et, whly_et, xqjl_et, submit_task;
    private View layout1, layout2, layout3;
    private TextView tag1, title1, tag2, title2, tag3, title3;
    private String inspection_village_id;
    private int mType = -1, selCorlor, norCorlor, mCurentIndex;
    private ViewPager viewpager;

    public static void startActivity(Context context, String inspection_village_id, int type) {
        Intent intent = new Intent(context, CountryDuChaActivity.class);
        intent.putExtra("inspection_village_id", inspection_village_id);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity context, String inspection_village_id, int type, int requestCode) {
        Intent intent = new Intent(context, CountryDuChaActivity.class);
        intent.putExtra("inspection_village_id", inspection_village_id);
        intent.putExtra("type", type);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_ducha1);
        selCorlor = getResources().getColor(R.color.gray_dark);
        norCorlor = getResources().getColor(R.color.text_gray);
        inspection_village_id = getIntent().getStringExtra("inspection_village_id");
        mType = getIntent().getIntExtra("type", -1);
        if (StringUtil.isEmpty(inspection_village_id)) {
            finish();
            return;
        }
        initTopView();
        initListener();
        setLeftBackButton();
        initSideBarListener();
        setTitle("贫困村脱贫攻坚督查");
        initView();
    }

    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        ArrayList<View> mViews = new ArrayList<>();
        View view1 = LayoutInflater.from(this).inflate(R.layout.item_country_ducha1, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.item_country_ducha2, null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.item_country_ducha3, null);
        mViews.add(view1);
        mViews.add(view2);
        mViews.add(view3);
        MyViewPagerAdaper adaper = new MyViewPagerAdaper(mViews);
        viewpager.setAdapter(adaper);
        viewpager.addOnPageChangeListener(this);

        pkl_yes_rb = (RadioButton) view1.findViewById(R.id.pkl_yes_rb);
        pkl_no_rb = (RadioButton) view1.findViewById(R.id.pkl_no_rb);
        snl_yes_rb = (RadioButton) view1.findViewById(R.id.snl_yes_rb);
        snl_no_rb = (RadioButton) view1.findViewById(R.id.snl_no_rb);
        kd_yes_rb = (RadioButton) view1.findViewById(R.id.kd_yes_rb);
        kd_no_rb = (RadioButton) view1.findViewById(R.id.kd_no_rb);
        yxds_yes_rb = (RadioButton) view1.findViewById(R.id.yxds_yes_rb);
        yxds_no_rb = (RadioButton) view1.findViewById(R.id.yxds_no_rb);
        wss_yes_rb = (RadioButton) view1.findViewById(R.id.wss_yes_rb);
        wss_no_rb = (RadioButton) view1.findViewById(R.id.wss_no_rb);
        ys_yes_rb = (RadioButton) view1.findViewById(R.id.ys_yes_rb);
        ys_no_rb = (RadioButton) view1.findViewById(R.id.ys_no_rb);
        whhd_yes_rb = (RadioButton) view1.findViewById(R.id.whhd_yes_rb);
        whhd_no_rb = (RadioButton) view1.findViewById(R.id.whhd_no_rb);
        xxny_yes_rb = (RadioButton) view2.findViewById(R.id.xxny_yes_rb);
        xxny_no_rb = (RadioButton) view2.findViewById(R.id.xxny_no_rb);
        zy_yes_rb = (RadioButton) view2.findViewById(R.id.zy_yes_rb);
        zy_no_rb = (RadioButton) view2.findViewById(R.id.zy_no_rb);
        rds_yes_rb = (RadioButton) view2.findViewById(R.id.rds_yes_rb);
        rds_no_rb = (RadioButton) view2.findViewById(R.id.rds_no_rb);
        gffd_yes_rb = (RadioButton) view2.findViewById(R.id.gffd_yes_rb);
        gffd_no_rb = (RadioButton) view2.findViewById(R.id.gffd_no_rb);
        jrfp_yes_rb = (RadioButton) view2.findViewById(R.id.jrfp_yes_rb);
        jrfp_no_rb = (RadioButton) view2.findViewById(R.id.jrfp_no_rb);
        wgzs_yes_rb = (RadioButton) view2.findViewById(R.id.wgzs_yes_rb);
        wgzs_no_rb = (RadioButton) view2.findViewById(R.id.wgzs_no_rb);
        dsfp_yes_rb = (RadioButton) view2.findViewById(R.id.dsfp_yes_rb);
        dsfp_no_rb = (RadioButton) view2.findViewById(R.id.dsfp_no_rb);
        tyjj_yes_rb = (RadioButton) view2.findViewById(R.id.tyjj_yes_rb);
        tyjj_no_rb = (RadioButton) view2.findViewById(R.id.tyjj_no_rb);
        whlv_yes_rb = (RadioButton) view2.findViewById(R.id.whlv_yes_rb);
        whlv_no_rb = (RadioButton) view2.findViewById(R.id.whlv_no_rb);
        xqjl_yes_rb = (RadioButton) view2.findViewById(R.id.xqjl_yes_rb);
        xqjl_no_rb = (RadioButton) view2.findViewById(R.id.xqjl_no_rb);

        cjtpgh_yes_rb = (RadioButton) view3.findViewById(R.id.cjtpgh_yes_rb);
        cjtpgh_no_rb = (RadioButton) view3.findViewById(R.id.cjtpgh_no_rb);
        jbgk_yes_rb = (RadioButton) view3.findViewById(R.id.jbgk_yes_rb);
        jbgk_no_rb = (RadioButton) view3.findViewById(R.id.jbgk_no_rb);
        jcsj_yes_rb = (RadioButton) view3.findViewById(R.id.jcsj_yes_rb);
        jcsj_no_rb = (RadioButton) view3.findViewById(R.id.jcsj_no_rb);
        pkrksl_yes_rb = (RadioButton) view3.findViewById(R.id.pkrksl_yes_rb);
        pkrksl_no_rb = (RadioButton) view3.findViewById(R.id.pkrksl_no_rb);
        cyfzqk_yes_rb = (RadioButton) view3.findViewById(R.id.cyfzqk_yes_rb);
        cyfzqk_no_rb = (RadioButton) view3.findViewById(R.id.cyfzqk_no_rb);
        tpnx_yes_rb = (RadioButton) view3.findViewById(R.id.tpnx_yes_rb);
        tpnx_no_rb = (RadioButton) view3.findViewById(R.id.tpnx_no_rb);
        zzldjg_yes_rb = (RadioButton) view3.findViewById(R.id.zzldjg_yes_rb);
        zzldjg_no_rb = (RadioButton) view3.findViewById(R.id.zzldjg_no_rb);
        tpgjtsb_yes_rb = (RadioButton) view3.findViewById(R.id.tpgjtsb_yes_rb);
        tpgjtsb_no_rb = (RadioButton) view3.findViewById(R.id.tpgjtsb_no_rb);
        pkhs_yes_rb = (RadioButton) view3.findViewById(R.id.pkhs_yes_rb);
        pkhs_no_rb = (RadioButton) view3.findViewById(R.id.pkhs_no_rb);
        hjrks_yes_rb = (RadioButton) view3.findViewById(R.id.hjrks_yes_rb);
        hjrks_no_rb = (RadioButton) view3.findViewById(R.id.hjrks_no_rb);
        czrks_yes_rb = (RadioButton) view3.findViewById(R.id.czrks_yes_rb);
        czrks_no_rb = (RadioButton) view3.findViewById(R.id.czrks_no_rb);
        wcrks_yes_rb = (RadioButton) view3.findViewById(R.id.wcrks_yes_rb);
        wcrks_no_rb = (RadioButton) view3.findViewById(R.id.wcrks_no_rb);
        hcrks_yes_rb = (RadioButton) view3.findViewById(R.id.hcrks_yes_rb);
        hcrks_no_rb = (RadioButton) view3.findViewById(R.id.hcrks_no_rb);
        csrks_yes_rb = (RadioButton) view3.findViewById(R.id.csrks_yes_rb);
        csrks_no_rb = (RadioButton) view3.findViewById(R.id.csrks_no_rb);
        swrks_yes_rb = (RadioButton) view3.findViewById(R.id.swrks_yes_rb);
        swrks_no_rb = (RadioButton) view3.findViewById(R.id.swrks_no_rb);
        sjgx_yes_rb = (RadioButton) view3.findViewById(R.id.sjgx_yes_rb);
        sjgx_no_rb = (RadioButton) view3.findViewById(R.id.sjgx_no_rb);
        cmxjhyjl_yes_rb = (RadioButton) view3.findViewById(R.id.cmxjhyjl_yes_rb);
        cmxjhyjl_no_rb = (RadioButton) view3.findViewById(R.id.cmxjhyjl_no_rb);
        cmxzhyxcyxzl_yes_rb = (RadioButton) view3.findViewById(R.id.cmxzhyxcyxzl_yes_rb);
        cmxzhyxcyxzl_no_rb = (RadioButton) view3.findViewById(R.id.cmxzhyxcyxzl_no_rb);
        cmdbhyjl_yes_rb = (RadioButton) view3.findViewById(R.id.cmdbhyjl_yes_rb);
        cmdbhyjl_no_rb = (RadioButton) view3.findViewById(R.id.cmdbhyjl_no_rb);
        cmdbhyxcyxzl_yes_rb = (RadioButton) view3.findViewById(R.id.cmdbhyxcyxzl_yes_rb);
        cmdbhyxcyxzl_no_rb = (RadioButton) view3.findViewById(R.id.cmdbhyxcyxzl_no_rb);
        cmdbtpqk_yes_rb = (RadioButton) view3.findViewById(R.id.cmdbtpqk_yes_rb);
        cmdbtpqk_no_rb = (RadioButton) view3.findViewById(R.id.cmdbtpqk_no_rb);
        sjjsjdbjgbg_yes_rb = (RadioButton) view3.findViewById(R.id.sjjsjdbjgbg_yes_rb);
        sjjsjdbjgbg_no_rb = (RadioButton) view3.findViewById(R.id.sjjsjdbjgbg_no_rb);
        cjdycgs_yes_rb = (RadioButton) view3.findViewById(R.id.cjdycgs_yes_rb);
        cjdycgs_no_rb = (RadioButton) view3.findViewById(R.id.cjdycgs_no_rb);
        bgmd_yes_rb = (RadioButton) view3.findViewById(R.id.bgmd_yes_rb);
        bgmd_no_rb = (RadioButton) view3.findViewById(R.id.bgmd_no_rb);
        yxzl_yes_rb = (RadioButton) view3.findViewById(R.id.yxzl_yes_rb);
        yxzl_no_rb = (RadioButton) view3.findViewById(R.id.yxzl_no_rb);
        xzdecgs_yes_rb = (RadioButton) view3.findViewById(R.id.xzdecgs_yes_rb);
        xzdecgs_no_rb = (RadioButton) view3.findViewById(R.id.xzdecgs_no_rb);
        bgmdpf_yes_rb = (RadioButton) view3.findViewById(R.id.bgmdpf_yes_rb);
        bgmdpf_no_rb = (RadioButton) view3.findViewById(R.id.bgmdpf_no_rb);
        flzdqq_yes_rb = (RadioButton) view3.findViewById(R.id.flzdqq_yes_rb);
        flzdqq_no_rb = (RadioButton) view3.findViewById(R.id.flzdqq_no_rb);
        dah_yes_rb = (RadioButton) view3.findViewById(R.id.dah_yes_rb);
        dah_no_rb = (RadioButton) view3.findViewById(R.id.dah_no_rb);
        yhyd_yes_rb = (RadioButton) view3.findViewById(R.id.yhyd_yes_rb);
        yhyd_no_rb = (RadioButton) view3.findViewById(R.id.yhyd_no_rb);
        dag_yes_rb = (RadioButton) view3.findViewById(R.id.dag_yes_rb);
        dag_no_rb = (RadioButton) view3.findViewById(R.id.dag_no_rb);
        zyxzl_yes_rb = (RadioButton) view3.findViewById(R.id.zyxzl_yes_rb);
        zyxzl_no_rb = (RadioButton) view3.findViewById(R.id.zyxzl_no_rb);
        hrrk_yes_rb = (RadioButton) view3.findViewById(R.id.hrrk_yes_rb);
        hrrk_no_rb = (RadioButton) view3.findViewById(R.id.hrrk_no_rb);
        lpl_yes_rb = (RadioButton) view1.findViewById(R.id.lpl_yes_rb);
        lpl_no_rb = (RadioButton) view1.findViewById(R.id.lpl_no_rb);
        cpl_yes_rb = (RadioButton) view1.findViewById(R.id.cpl_yes_rb);
        cpl_no_rb = (RadioButton) view1.findViewById(R.id.cpl_no_rb);

        monitor_et = (TextView) view1.findViewById(R.id.monitor_et);
        menber_et = (TextView) view1.findViewById(R.id.menber_et);
        xxny_et = (TextView) view2.findViewById(R.id.xxny_et);
        zy_et = (TextView) view2.findViewById(R.id.zy_et);
        rds_et = (TextView) view2.findViewById(R.id.rds_et);
        gffd_et = (TextView) view2.findViewById(R.id.gffd_et);
        jrfp_et = (TextView) view2.findViewById(R.id.jrfp_et);
        wgzs_et = (TextView) view2.findViewById(R.id.wgzs_et);
        dsfp_et = (TextView) view2.findViewById(R.id.dsfp_et);
        tyjj_et = (TextView) view2.findViewById(R.id.tyjj_et);
        whly_et = (TextView) view2.findViewById(R.id.whly_et);
        xqjl_et = (TextView) view2.findViewById(R.id.xqjl_et);
        submit_task = (TextView) findViewById(R.id.submit_task);

        if (mType == TYPE_DETAIL) {
            submit_task.setOnClickListener(this);
        } else {
            monitor_et.setFocusable(false);
            menber_et.setFocusable(false);
            xxny_et.setFocusable(false);
            zy_et.setFocusable(false);
            rds_et.setFocusable(false);
            gffd_et.setFocusable(false);
            jrfp_et.setFocusable(false);
            wgzs_et.setFocusable(false);
            dsfp_et.setFocusable(false);
            tyjj_et.setFocusable(false);
            whly_et.setFocusable(false);
            xqjl_et.setFocusable(false);

            monitor_et.setHint("");
            menber_et.setHint("");
            xxny_et.setHint("");
            zy_et.setHint("");
            rds_et.setHint("");
            gffd_et.setHint("");
            jrfp_et.setHint("");
            wgzs_et.setHint("");
            dsfp_et.setHint("");
            tyjj_et.setHint("");
            whly_et.setHint("");
            xqjl_et.setHint("");
            submit_task.setVisibility(View.GONE);
        }
        initTitleLayout();
        loadData();
    }


    @Override
    public void onClick(View v) {
        if (submit_task == v) {
            if (mCurentIndex == 2) {
                save();
            } else {
                viewpager.setCurrentItem(mCurentIndex + 1);
            }
        } else if (layout1 == v) {
            viewpager.setCurrentItem(0);
        } else if (layout2 == v) {
            viewpager.setCurrentItem(1);
        } else if (layout3 == v) {
            viewpager.setCurrentItem(2);
        }
    }

    private void loadData() {
        if (mType == TYPE_DETAIL) {
            CountryDuChaDetailRequest request = new CountryDuChaDetailRequest(configEntity.key, inspection_village_id);
            httpGetRequest(request.getRequestUrl(), CountryDuChaDetailRequest.COUNTRY_DUCHA_DETAIL_REQUEST);
        } else {
            CountryDuChaDetailRequest2 request2 = new CountryDuChaDetailRequest2(configEntity.key, inspection_village_id);
            httpGetRequest(request2.getRequestUrl(), CountryDuChaDetailRequest.COUNTRY_DUCHA_DETAIL_REQUEST);
        }
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case CountryDuChaDetailRequest.COUNTRY_DUCHA_DETAIL_REQUEST:
                handleRquest(json);
                break;
            case SaveCountryInspectRequest.SAVE_COUNTRY_INSPECT_REQUEST:
                setResult(Activity.RESULT_OK);
                finish();
                break;
        }
    }

    private void handleRquest(String json) {
        try {
            CountryDetailAllEntity detailAllEntity = JSONObject.parseObject(json, CountryDetailAllEntity.class);
            if (detailAllEntity != null && detailAllEntity.inspectionVillageInfo_each != null) {
                setRbStatus(pkl_yes_rb, pkl_no_rb, detailAllEntity.inspectionVillageInfo_each.is_low_process);
                setRbStatus(snl_yes_rb, snl_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_cement);
                setRbStatus(kd_yes_rb, kd_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_net);
                setRbStatus(yxds_yes_rb, yxds_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_watch);
                setRbStatus(wss_yes_rb, wss_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_clinic);
                setRbStatus(ys_yes_rb, ys_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_doctor);
                setRbStatus(zy_yes_rb, zy_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_industry_guide);
                setRbStatus(rds_yes_rb, rds_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_three_industry);
                setRbStatus(gffd_yes_rb, gffd_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_pv_power);
                setRbStatus(jrfp_yes_rb, jrfp_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_finance);
                setRbStatus(wgzs_yes_rb, wgzs_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_workers);
                setRbStatus(dsfp_yes_rb, dsfp_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_online);
                setRbStatus(tyjj_yes_rb, tyjj_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_courtyard);
                setRbStatus(whlv_yes_rb, whlv_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_tourism);
                setRbStatus(xqjl_yes_rb, xqjl_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_piety);
                setRbStatus(xxny_yes_rb, xxny_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_farm_guide);
                setRbStatus(whhd_yes_rb, whhd_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_active_address);

                setRbStatus(cjtpgh_yes_rb, cjtpgh_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_tuopin_storm_plan);
                setRbStatus(jbgk_yes_rb, jbgk_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_base_record);
                setRbStatus(jcsj_yes_rb, jcsj_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_base_datas);
                setRbStatus(pkrksl_yes_rb, pkrksl_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_poor_counts);
                setRbStatus(cyfzqk_yes_rb, cyfzqk_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_industry);
                setRbStatus(tpnx_yes_rb, tpnx_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_tuopin_date);
                setRbStatus(zzldjg_yes_rb, zzldjg_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_group_mechanism);
                setRbStatus(tpgjtsb_yes_rb, tpgjtsb_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_tuopin_schematic_board);
                setRbStatus(pkhs_yes_rb, pkhs_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_poorandpersons_counts);
                setRbStatus(hjrks_yes_rb, hjrks_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_register_counts);
                setRbStatus(czrks_yes_rb, czrks_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_resident_counts);
                setRbStatus(wcrks_yes_rb, wcrks_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_out_person_counts);
                setRbStatus(hcrks_yes_rb, hcrks_no_rb, detailAllEntity.inspectionVillageInfo_each.is_avhe_out_marriage_counts);
                setRbStatus(hrrk_yes_rb, hrrk_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_in_marriage_counts);
                setRbStatus(csrks_yes_rb, csrks_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_birth_counts);
                setRbStatus(swrks_yes_rb, swrks_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_die_counts);
                setRbStatus(sjgx_yes_rb, sjgx_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_data_update);
                setRbStatus(cmxjhyjl_yes_rb, cmxjhyjl_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_group_record);
                setRbStatus(cmxzhyxcyxzl_yes_rb, cmxzhyxcyxzl_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_group_video_file);
                setRbStatus(cmdbhyjl_yes_rb, cmdbhyjl_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_person_record);
                setRbStatus(cmdbhyxcyxzl_yes_rb, cmdbhyxcyxzl_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_now_video_file);
                setRbStatus(cmdbtpqk_yes_rb, cmdbtpqk_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_person_vote);
                setRbStatus(sjjsjdbjgbg_yes_rb, sjjsjdbjgbg_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_audit_report);
                setRbStatus(cjdycgs_yes_rb, cjdycgs_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_one_public);
                setRbStatus(bgmd_yes_rb, bgmd_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_one_name_list);
                setRbStatus(yxzl_yes_rb, yxzl_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_video_file);
                setRbStatus(xzdecgs_yes_rb, xzdecgs_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_two_public);
                setRbStatus(bgmdpf_yes_rb, bgmdpf_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_two_name_list);
                setRbStatus(zyxzl_yes_rb, zyxzl_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_two_video_file);
                setRbStatus(flzdqq_yes_rb, flzdqq_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_file_complete);
                setRbStatus(dah_yes_rb, dah_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_file_box);
                setRbStatus(yhyd_yes_rb, yhyd_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_one_file);
                setRbStatus(dag_yes_rb, dag_no_rb, detailAllEntity.inspectionVillageInfo_each.is_have_file_cab);
                setRbStatus(lpl_yes_rb, lpl_no_rb, detailAllEntity.inspectionVillageInfo_each.is_low_poor_p);
                setRbStatus(cpl_yes_rb, cpl_no_rb, detailAllEntity.inspectionVillageInfo_each.is_low_no_p);

                monitor_et.setText(detailAllEntity.inspectionVillageInfo_each.inspection_manage);
                menber_et.setText(detailAllEntity.inspectionVillageInfo_each.inspection_person);
                xxny_et.setText(detailAllEntity.inspectionVillageInfo_each.farm_guide_desc);
                zy_et.setText(detailAllEntity.inspectionVillageInfo_each.industry_guide_desc);
                rds_et.setText(detailAllEntity.inspectionVillageInfo_each.three_industry_desc);
                gffd_et.setText(detailAllEntity.inspectionVillageInfo_each.pv_power_desc);
                jrfp_et.setText(detailAllEntity.inspectionVillageInfo_each.finance_desc);
                wgzs_et.setText(detailAllEntity.inspectionVillageInfo_each.workers_desc);
                dsfp_et.setText(detailAllEntity.inspectionVillageInfo_each.online_desc);
                tyjj_et.setText(detailAllEntity.inspectionVillageInfo_each.courtyard_desc);
                whly_et.setText(detailAllEntity.inspectionVillageInfo_each.tourism_desc);
                xqjl_et.setText(detailAllEntity.inspectionVillageInfo_each.piety_desc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setRbStatus(RadioButton yes_rb, RadioButton no_rb, int status) {
        if (status == CountryDuChaEntity.YES) {
            yes_rb.setChecked(true);
        } else if (status == CountryDuChaEntity.NO) {
            no_rb.setChecked(true);
        }
    }

    private void save() {
        CountryDuChaEntity entity = new CountryDuChaEntity();
        if (pkl_yes_rb.isChecked()) {
            entity.is_low_process = 1;
        } else if (pkl_no_rb.isChecked()) {
            entity.is_low_process = 0;
        } else {
            showToast("请选择贫困发生率有无降至2%以下");
            return;
        }
        if (snl_yes_rb.isChecked()) {
            entity.is_have_cement = 1;
        } else if (snl_no_rb.isChecked()) {
            entity.is_have_cement = 0;
        } else {
            showToast("请选择中心屯有无通水泥路");
            return;
        }
        if (kd_yes_rb.isChecked()) {
            entity.is_have_net = 1;
        } else if (kd_no_rb.isChecked()) {
            entity.is_have_net = 0;
        } else {
            showToast("请选择中心屯有无通宽带");
            return;
        }
        if (yxds_yes_rb.isChecked()) {
            entity.is_have_watch = 1;
        } else if (yxds_no_rb.isChecked()) {
            entity.is_have_watch = 0;
        } else {
            showToast("请选择中心屯有无通广播有线电视");
            return;
        }
        if (wss_yes_rb.isChecked()) {
            entity.is_have_clinic = 1;
        } else if (wss_no_rb.isChecked()) {
            entity.is_have_clinic = 0;
        } else {
            showToast("请选择是否有卫生室");
            return;
        }
        if (ys_yes_rb.isChecked()) {
            entity.is_have_doctor = 1;
        } else if (ys_no_rb.isChecked()) {
            entity.is_have_doctor = 0;
        } else {
            showToast("请选择是否有合格医生");
            return;
        }
        if (whhd_yes_rb.isChecked()) {
            entity.is_have_active_address = 1;
        } else if (whhd_no_rb.isChecked()) {
            entity.is_have_active_address = 0;
        } else {
            showToast("请选择是否有文化活动场所");
            return;
        }
        if (xxny_yes_rb.isChecked()) {
            entity.is_have_farm_guide = 1;
        } else if (xxny_no_rb.isChecked()) {
            entity.is_have_farm_guide = 0;
        }
        entity.farm_guide_desc = xxny_et.getText().toString();
        if (zy_yes_rb.isChecked()) {
            entity.is_have_industry_guide = 1;
        } else if (zy_no_rb.isChecked()) {
            entity.is_have_industry_guide = 0;
        }
        entity.industry_guide_desc = zy_et.getText().toString();
        if (rds_yes_rb.isChecked()) {
            entity.is_have_three_industry = 1;
        } else if (rds_no_rb.isChecked()) {
            entity.is_have_three_industry = 0;
        }
        entity.three_industry_desc = rds_et.getText().toString();
        if (gffd_yes_rb.isChecked()) {
            entity.is_have_pv_power = 1;
        } else if (gffd_no_rb.isChecked()) {
            entity.is_have_pv_power = 0;
        }
        entity.pv_power_desc = gffd_et.getText().toString();
        if (jrfp_yes_rb.isChecked()) {
            entity.is_have_finance = 1;
        } else if (jrfp_no_rb.isChecked()) {
            entity.is_have_finance = 0;
        }
        entity.finance_desc = jrfp_et.getText().toString();

        if (wgzs_yes_rb.isChecked()) {
            entity.is_have_workers = 1;
        } else if (wgzs_no_rb.isChecked()) {
            entity.is_have_workers = 0;
        }
        entity.workers_desc = wgzs_et.getText().toString();
        if (dsfp_yes_rb.isChecked()) {
            entity.is_have_online = 1;
        } else if (dsfp_no_rb.isChecked()) {
            entity.is_have_online = 0;
        }
        entity.online_desc = dsfp_et.getText().toString();
        if (tyjj_yes_rb.isChecked()) {
            entity.is_have_courtyard = 1;
        } else if (tyjj_no_rb.isChecked()) {
            entity.is_have_courtyard = 0;
        }
        entity.courtyard_desc = tyjj_et.getText().toString();
        if (whlv_yes_rb.isChecked()) {
            entity.is_have_tourism = 1;
        } else if (whlv_no_rb.isChecked()) {
            entity.is_have_tourism = 0;
        }
        entity.tourism_desc = whly_et.getText().toString();
        if (xqjl_yes_rb.isChecked()) {
            entity.is_have_piety = 1;
        } else if (xqjl_no_rb.isChecked()) {
            entity.is_have_piety = 0;
        }

        //
        if (cjtpgh_yes_rb.isChecked()) {
            entity.is_have_tuopin_storm_plan = 1;
        } else if (cjtpgh_no_rb.isChecked()) {
            entity.is_have_tuopin_storm_plan = 0;
        }
        if (jbgk_yes_rb.isChecked()) {
            entity.is_have_base_record = 1;
        } else if (jbgk_no_rb.isChecked()) {
            entity.is_have_base_record = 0;
        }
        if (jcsj_yes_rb.isChecked()) {
            entity.is_have_base_datas = 1;
        } else if (jcsj_no_rb.isChecked()) {
            entity.is_have_base_datas = 0;
        }
        if (pkrksl_yes_rb.isChecked()) {
            entity.is_have_poor_counts = 1;
        } else if (pkrksl_no_rb.isChecked()) {
            entity.is_have_poor_counts = 0;
        }
        if (cyfzqk_yes_rb.isChecked()) {
            entity.is_have_industry = 1;
        } else if (cyfzqk_no_rb.isChecked()) {
            entity.is_have_industry = 0;
        }
        if (tpnx_yes_rb.isChecked()) {
            entity.is_have_tuopin_date = 1;
        } else if (tpnx_no_rb.isChecked()) {
            entity.is_have_tuopin_date = 0;
        }
        if (zzldjg_yes_rb.isChecked()) {
            entity.is_have_group_mechanism = 1;
        } else if (zzldjg_no_rb.isChecked()) {
            entity.is_have_group_mechanism = 0;
        }
        if (tpgjtsb_yes_rb.isChecked()) {
            entity.is_have_tuopin_schematic_board = 1;
        } else if (tpgjtsb_no_rb.isChecked()) {
            entity.is_have_tuopin_schematic_board = 0;
        }
        if (pkhs_yes_rb.isChecked()) {
            entity.is_have_poorandpersons_counts = 1;
        } else if (pkhs_no_rb.isChecked()) {
            entity.is_have_poorandpersons_counts = 0;
        }
        if (hjrks_yes_rb.isChecked()) {
            entity.is_have_register_counts = 1;
        } else if (hjrks_no_rb.isChecked()) {
            entity.is_have_register_counts = 0;
        }
        if (czrks_yes_rb.isChecked()) {
            entity.is_have_resident_counts = 1;
        } else if (czrks_no_rb.isChecked()) {
            entity.is_have_resident_counts = 0;
        }
        if (wcrks_yes_rb.isChecked()) {
            entity.is_have_out_person_counts = 1;
        } else if (wcrks_no_rb.isChecked()) {
            entity.is_have_out_person_counts = 0;
        }
        if (hcrks_yes_rb.isChecked()) {
            entity.is_avhe_out_marriage_counts = 1;
        } else if (hcrks_no_rb.isChecked()) {
            entity.is_avhe_out_marriage_counts = 0;
        }
        if (csrks_yes_rb.isChecked()) {
            entity.is_have_birth_counts = 1;
        } else if (csrks_no_rb.isChecked()) {
            entity.is_have_birth_counts = 0;
        }
        if (swrks_yes_rb.isChecked()) {
            entity.is_have_die_counts = 1;
        } else if (swrks_no_rb.isChecked()) {
            entity.is_have_die_counts = 0;
        }
        if (sjgx_yes_rb.isChecked()) {
            entity.is_have_data_update = 1;
        } else if (sjgx_no_rb.isChecked()) {
            entity.is_have_data_update = 0;
        }
        if (cmxjhyjl_yes_rb.isChecked()) {
            entity.is_have_group_record = 1;
        } else if (cmxjhyjl_no_rb.isChecked()) {
            entity.is_have_group_record = 0;
        }
        if (cmxzhyxcyxzl_yes_rb.isChecked()) {
            entity.is_have_group_video_file = 1;
        } else if (cmxzhyxcyxzl_no_rb.isChecked()) {
            entity.is_have_group_video_file = 0;
        }
        if (cmdbhyjl_yes_rb.isChecked()) {
            entity.is_have_person_record = 1;
        } else if (cmdbhyjl_no_rb.isChecked()) {
            entity.is_have_person_record = 0;
        }
        if (cmdbhyxcyxzl_yes_rb.isChecked()) {
            entity.is_have_now_video_file = 1;
        } else if (cmdbhyxcyxzl_no_rb.isChecked()) {
            entity.is_have_now_video_file = 0;
        }
        if (cmdbtpqk_yes_rb.isChecked()) {
            entity.is_have_person_vote = 1;
        } else if (cmdbtpqk_no_rb.isChecked()) {
            entity.is_have_person_vote = 0;
        }
        if (sjjsjdbjgbg_yes_rb.isChecked()) {
            entity.is_have_audit_report = 1;
        } else if (sjjsjdbjgbg_no_rb.isChecked()) {
            entity.is_have_audit_report = 0;
        }
        if (cjdycgs_yes_rb.isChecked()) {
            entity.is_have_one_public = 1;
        } else if (cjdycgs_no_rb.isChecked()) {
            entity.is_have_one_public = 0;
        }
        if (bgmd_yes_rb.isChecked()) {
            entity.is_have_one_name_list = 1;
        } else if (bgmd_no_rb.isChecked()) {
            entity.is_have_one_name_list = 0;
        }
        if (yxzl_yes_rb.isChecked()) {
            entity.is_have_video_file = 1;
        } else if (yxzl_no_rb.isChecked()) {
            entity.is_have_video_file = 0;
        }
        if (xzdecgs_yes_rb.isChecked()) {
            entity.is_have_two_public = 1;
        } else if (xzdecgs_no_rb.isChecked()) {
            entity.is_have_two_public = 0;
        }
        if (bgmdpf_yes_rb.isChecked()) {
            entity.is_have_two_name_list = 1;
        } else if (bgmdpf_no_rb.isChecked()) {
            entity.is_have_two_name_list = 0;
        }
        if (zyxzl_yes_rb.isChecked()) {
            entity.is_have_two_video_file = 1;
        } else if (zyxzl_no_rb.isChecked()) {
            entity.is_have_two_video_file = 0;
        }
        if (flzdqq_yes_rb.isChecked()) {
            entity.is_have_file_complete = 1;
        } else if (flzdqq_no_rb.isChecked()) {
            entity.is_have_file_complete = 0;
        }
        if (dah_yes_rb.isChecked()) {
            entity.is_have_file_box = 1;
        } else if (dah_no_rb.isChecked()) {
            entity.is_have_file_box = 0;
        }
        if (yhyd_yes_rb.isChecked()) {
            entity.is_have_one_file = 1;
        } else if (yhyd_no_rb.isChecked()) {
            entity.is_have_one_file = 0;
        }
        if (dag_yes_rb.isChecked()) {
            entity.is_have_file_cab = 1;
        } else if (dag_no_rb.isChecked()) {
            entity.is_have_file_cab = 0;
        }
        if (hrrk_yes_rb.isChecked()) {
            entity.is_have_in_marriage_counts = 1;
        } else if (hrrk_no_rb.isChecked()) {
            entity.is_have_in_marriage_counts = 0;
        }
        if (lpl_yes_rb.isChecked()) {
            entity.is_low_poor_p = 1;
        } else if (lpl_no_rb.isChecked()) {
            entity.is_low_poor_p = 0;
        }
        if (cpl_yes_rb.isChecked()) {
            entity.is_low_no_p = 1;
        } else if (cpl_no_rb.isChecked()) {
            entity.is_low_no_p = 0;
        }
        entity.piety_desc = xqjl_et.getText().toString();
        entity.inspection_manage = monitor_et.getText().toString();
        entity.inspection_person = menber_et.getText().toString();
        SaveCountryInspectRequest request = new SaveCountryInspectRequest(configEntity.key, inspection_village_id, entity);
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), SaveCountryInspectRequest.SAVE_COUNTRY_INSPECT_REQUEST);
    }

    private void initTitleLayout() {
        tag1 = (TextView) findViewById(R.id.tag1);
        title1 = (TextView) findViewById(R.id.title1);
        tag2 = (TextView) findViewById(R.id.tag2);
        title2 = (TextView) findViewById(R.id.title2);
        tag3 = (TextView) findViewById(R.id.tag3);
        title3 = (TextView) findViewById(R.id.title3);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        swichPager(0);
    }

    private void swichPager(int pos) {
        mCurentIndex = pos;
        if (pos == 0) {
            title1.setTextColor(selCorlor);
            title2.setTextColor(norCorlor);
            title3.setTextColor(norCorlor);
            tag1.setBackgroundResource(R.drawable.circle_black);
            tag2.setBackgroundResource(R.drawable.circle_gray);
            tag3.setBackgroundResource(R.drawable.circle_gray);
            submit_task.setText("下一步");
        } else if (pos == 1) {
            title1.setTextColor(norCorlor);
            title2.setTextColor(selCorlor);
            title3.setTextColor(norCorlor);
            tag1.setBackgroundResource(R.drawable.circle_gray);
            tag2.setBackgroundResource(R.drawable.circle_black);
            tag3.setBackgroundResource(R.drawable.circle_gray);
            submit_task.setText("下一步");
        } else {
            title1.setTextColor(norCorlor);
            title2.setTextColor(norCorlor);
            title3.setTextColor(selCorlor);
            tag1.setBackgroundResource(R.drawable.circle_gray);
            tag2.setBackgroundResource(R.drawable.circle_gray);
            tag3.setBackgroundResource(R.drawable.circle_black);
            submit_task.setText("提交");
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        swichPager(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
