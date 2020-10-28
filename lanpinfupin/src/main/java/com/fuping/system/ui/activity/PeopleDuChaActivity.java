package com.fuping.system.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fuping.system.R;
import com.fuping.system.adapter.MyViewPagerAdaper;
import com.fuping.system.entity.CountryDuChaEntity;
import com.fuping.system.entity.PeopleDetailEntity;
import com.fuping.system.entity.PeoplePoorTownEntity;
import com.fuping.system.request.PeopleDuChaDetailRequest;
import com.fuping.system.request.SavePeopleInspectRequest;

import java.util.ArrayList;

/**
 * Created by jianzhang.
 * on 2017/10/18.
 * copyright easybiz.
 */

public class PeopleDuChaActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private RadioButton bcc_yes_rb, bcc_no_rb, bcchuan_yes_rb, bcchuan_no_rb, pkcx_yes_rb, pkcx_no_rb, jbyl_yes_rb, jbyl_no_rb,
            zfaq_yes_rb, zfaq_no_rb, cjgz_yes_rb, cjgz_no_rb, djgz_yes_rb, djgz_no_rb, aqzf_yes_rb, aqzf_no_rb,
            zzbm_yes_rb, zzbm_no_rb, ysaq_yes_rb, ysaq_no_rb, szbg_yes_rb, szbg_no_rb, xdj_yes_rb, xdj_no_rb, gxaz_yes_rb,
            gxaz_no_rb, glsb_yes_rb, glsb_no_rb, csr_yes_rb, csr_no_rb, cytp_yes_rb, cytp_no_rb, zcbz_yes_rb, zcbz_no_rb, bfgz_yes_rb,
            bfgz_no_rb, bflxk_yes_rb, bflxk_no_rb, nrtxqq_yes_rb, nrtxqq_no_rb, pkyyysjxf_yes_rb, pkyyysjxf_no_rb, cyfzcs_yes_rb, cyfzcs_no_rb,
            pkyyybfcsyz_yes_rb, pkyyybfcsyz_no_rb, bfsctxqq_yes_rb, bfsctxqq_no_rb, wksjqq_yes_rb, wksjqq_no_rb,
            bftztxqq_yes_rb, bftztxqq_no_rb, szlsztxqq_yes_rb, szlsztxqq_no_rb, tptztxqq_yes_rb, tptztxqq_no_rb,
            tpggztxqq_yes_rb, tpggztxqq_no_rb, datxzql_yes_rb, datxzql_no_rb;
    private EditText csr_et, cytp_et, zcbz_et, monitor_et, menber_et;
    private View szjc_layout, xdj_layout, gxaz_layout, glsb_layout, layout1, layout2, layout3;
    private TextView tag1, title1, tag2, title2, tag3, title3, submit_task;
    private String inspection_poor_id;
    private int mType = -1, selCorlor, norCorlor, mCurrentIndex;
    private ViewPager viewpager;

    public static void startActivity(Context context, String inspection_poor_id, int type) {
        Intent intent = new Intent(context, PeopleDuChaActivity.class);
        intent.putExtra("inspection_poor_id", inspection_poor_id);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity context, String inspection_poor_id, int type, int requestCode) {
        Intent intent = new Intent(context, PeopleDuChaActivity.class);
        intent.putExtra("inspection_poor_id", inspection_poor_id);
        intent.putExtra("type", type);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_ducha1);
        mType = getIntent().getIntExtra("type", -1);
        inspection_poor_id = getIntent().getStringExtra("inspection_poor_id");
        selCorlor = getResources().getColor(R.color.gray_dark);
        norCorlor = getResources().getColor(R.color.text_gray);
        if (inspection_poor_id == null) {
            finish();
            return;
        }
        initTopView();
        initListener();
        setLeftBackButton();
        initSideBarListener();
        setTitle("贫困户脱贫攻坚督查");
        initView();
    }

    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        ArrayList<View> mViews = new ArrayList<>();
        View view1 = LayoutInflater.from(this).inflate(R.layout.item_people_ducha_1, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.item_people_ducha_2, null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.item_people_ducha_3, null);
        mViews.add(view1);
        mViews.add(view2);
        mViews.add(view3);
        MyViewPagerAdaper adaper = new MyViewPagerAdaper(mViews);
        viewpager.setAdapter(adaper);
        viewpager.addOnPageChangeListener(this);

        bcc_yes_rb = (RadioButton) view1.findViewById(R.id.bcc_yes_rb);
        bcc_no_rb = (RadioButton) view1.findViewById(R.id.bcc_no_rb);
        bcchuan_yes_rb = (RadioButton) view1.findViewById(R.id.bcchuan_yes_rb);
        bcchuan_no_rb = (RadioButton) view1.findViewById(R.id.bcchuan_no_rb);
        pkcx_yes_rb = (RadioButton) view1.findViewById(R.id.pkcx_yes_rb);
        pkcx_no_rb = (RadioButton) view1.findViewById(R.id.pkcx_no_rb);
        jbyl_yes_rb = (RadioButton) view1.findViewById(R.id.jbyl_yes_rb);
        jbyl_no_rb = (RadioButton) view1.findViewById(R.id.jbyl_no_rb);
        zfaq_yes_rb = (RadioButton) view1.findViewById(R.id.zfaq_yes_rb);
        zfaq_no_rb = (RadioButton) view1.findViewById(R.id.zfaq_no_rb);
        cjgz_yes_rb = (RadioButton) view1.findViewById(R.id.cjgz_yes_rb);
        cjgz_no_rb = (RadioButton) view1.findViewById(R.id.cjgz_no_rb);
        djgz_yes_rb = (RadioButton) view1.findViewById(R.id.djgz_yes_rb);
        djgz_no_rb = (RadioButton) view1.findViewById(R.id.djgz_no_rb);
        aqzf_yes_rb = (RadioButton) view1.findViewById(R.id.aqzf_yes_rb);
        aqzf_no_rb = (RadioButton) view1.findViewById(R.id.aqzf_no_rb);
        zzbm_yes_rb = (RadioButton) view1.findViewById(R.id.zzbm_yes_rb);
        zzbm_no_rb = (RadioButton) view1.findViewById(R.id.zzbm_no_rb);
        ysaq_yes_rb = (RadioButton) view1.findViewById(R.id.ysaq_yes_rb);
        ysaq_no_rb = (RadioButton) view1.findViewById(R.id.ysaq_no_rb);
        szbg_yes_rb = (RadioButton) view1.findViewById(R.id.szbg_yes_rb);
        szbg_no_rb = (RadioButton) view1.findViewById(R.id.szbg_no_rb);
        xdj_yes_rb = (RadioButton) view1.findViewById(R.id.xdj_yes_rb);
        xdj_no_rb = (RadioButton) view1.findViewById(R.id.xdj_no_rb);
        gxaz_yes_rb = (RadioButton) view1.findViewById(R.id.gxaz_yes_rb);
        gxaz_no_rb = (RadioButton) view1.findViewById(R.id.gxaz_no_rb);
        glsb_yes_rb = (RadioButton) view1.findViewById(R.id.glsb_yes_rb);
        glsb_no_rb = (RadioButton) view1.findViewById(R.id.glsb_no_rb);
        csr_yes_rb = (RadioButton) view2.findViewById(R.id.csr_yes_rb);
        csr_no_rb = (RadioButton) view2.findViewById(R.id.csr_no_rb);
        cytp_yes_rb = (RadioButton) view2.findViewById(R.id.cytp_yes_rb);
        cytp_no_rb = (RadioButton) view2.findViewById(R.id.cytp_no_rb);
        zcbz_yes_rb = (RadioButton) view2.findViewById(R.id.zcbz_yes_rb);
        zcbz_no_rb = (RadioButton) view2.findViewById(R.id.zcbz_no_rb);

        bfgz_yes_rb = (RadioButton) view2.findViewById(R.id.bfgz_yes_rb);
        bfgz_no_rb = (RadioButton) view2.findViewById(R.id.bfgz_no_rb);
        bflxk_yes_rb = (RadioButton) view3.findViewById(R.id.bflxk_yes_rb);
        bflxk_no_rb = (RadioButton) view3.findViewById(R.id.bflxk_no_rb);
        nrtxqq_yes_rb = (RadioButton) view3.findViewById(R.id.nrtxqq_yes_rb);
        nrtxqq_no_rb = (RadioButton) view3.findViewById(R.id.nrtxqq_no_rb);
        pkyyysjxf_yes_rb = (RadioButton) view3.findViewById(R.id.pkyyysjxf_yes_rb);
        pkyyysjxf_no_rb = (RadioButton) view3.findViewById(R.id.pkyyysjxf_no_rb);
        cyfzcs_yes_rb = (RadioButton) view3.findViewById(R.id.cyfzcs_yes_rb);
        cyfzcs_no_rb = (RadioButton) view3.findViewById(R.id.cyfzcs_no_rb);
        pkyyybfcsyz_yes_rb = (RadioButton) view3.findViewById(R.id.pkyyybfcsyz_yes_rb);
        pkyyybfcsyz_no_rb = (RadioButton) view3.findViewById(R.id.pkyyybfcsyz_no_rb);
        bfsctxqq_yes_rb = (RadioButton) view3.findViewById(R.id.bfsctxqq_yes_rb);
        bfsctxqq_no_rb = (RadioButton) view3.findViewById(R.id.bfsctxqq_no_rb);
        wksjqq_yes_rb = (RadioButton) view3.findViewById(R.id.wksjqq_yes_rb);
        wksjqq_no_rb = (RadioButton) view3.findViewById(R.id.wksjqq_no_rb);
        bftztxqq_yes_rb = (RadioButton) view3.findViewById(R.id.bftztxqq_yes_rb);
        bftztxqq_no_rb = (RadioButton) view3.findViewById(R.id.bftztxqq_no_rb);
        szlsztxqq_yes_rb = (RadioButton) view3.findViewById(R.id.szlsztxqq_yes_rb);
        szlsztxqq_no_rb = (RadioButton) view3.findViewById(R.id.szlsztxqq_no_rb);
        tptztxqq_yes_rb = (RadioButton) view3.findViewById(R.id.tptztxqq_yes_rb);
        tptztxqq_no_rb = (RadioButton) view3.findViewById(R.id.tptztxqq_no_rb);
        tpggztxqq_yes_rb = (RadioButton) view3.findViewById(R.id.tpggztxqq_yes_rb);
        tpggztxqq_no_rb = (RadioButton) view3.findViewById(R.id.tpggztxqq_no_rb);
        datxzql_yes_rb = (RadioButton) view3.findViewById(R.id.datxzql_yes_rb);
        datxzql_no_rb = (RadioButton) view3.findViewById(R.id.datxzql_no_rb);

        csr_et = (EditText) view2.findViewById(R.id.csr_et);
        cytp_et = (EditText) view2.findViewById(R.id.cytp_et);
        zcbz_et = (EditText) view2.findViewById(R.id.zcbz_et);
        monitor_et = (EditText) view1.findViewById(R.id.monitor_et);
        menber_et = (EditText) view1.findViewById(R.id.menber_et);
        submit_task = (TextView) findViewById(R.id.submit_task);
        szjc_layout = view1.findViewById(R.id.szjc_layout);
        xdj_layout = view1.findViewById(R.id.xdj_layout);
        gxaz_layout = view1.findViewById(R.id.gxaz_layout);
        glsb_layout = view1.findViewById(R.id.glsb_layout);

        if (mType == CountryDuChaActivity.TYPE_LIST) {
            submit_task.setVisibility(View.GONE);
            csr_et.setFocusable(false);
            cytp_et.setFocusable(false);
            zcbz_et.setFocusable(false);
            monitor_et.setFocusable(false);
            menber_et.setFocusable(false);
            csr_et.setHint("");
            cytp_et.setHint("");
            zcbz_et.setHint("");
            monitor_et.setHint("");
            menber_et.setHint("");
        } else {
            submit_task.setOnClickListener(this);
        }
        loadData();
        ysaq_yes_rb.setOnCheckedChangeListener(this);
        initTitleLayout();
    }

    @Override
    public void onClick(View v) {
        if (submit_task == v) {
            if (mCurrentIndex == 2) {
                save();
            } else {
                viewpager.setCurrentItem(mCurrentIndex + 1);
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
        PeopleDuChaDetailRequest request = new PeopleDuChaDetailRequest(mType, configEntity.key, inspection_poor_id);
        httpGetRequest(request.getRequestUrl(), PeopleDuChaDetailRequest.PEOPLE_DUCHA_DETAI_LREQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case PeopleDuChaDetailRequest.PEOPLE_DUCHA_DETAI_LREQUEST:
                handleRequest(json);
                break;
            case SavePeopleInspectRequest.SAVE_COUNTRY_INSPECT_REQUEST:
                setResult(Activity.RESULT_OK);
                finish();
                break;
        }
    }

    private void handleRequest(String json) {
        try {
            PeoplePoorTownEntity entity = JSONObject.parseObject(json, PeoplePoorTownEntity.class);
            if (entity != null && entity.inspectionPooerInfo_each != null &&
                    entity.inspectionPooerInfo_each.is_have_eat != -1) {
                setRbStatus(bcc_yes_rb, bcc_no_rb, entity.inspectionPooerInfo_each.is_have_eat);
                setRbStatus(bcchuan_yes_rb, bcchuan_no_rb, entity.inspectionPooerInfo_each.is_have_wear);
                setRbStatus(pkcx_yes_rb, pkcx_no_rb, entity.inspectionPooerInfo_each.is_have_drop_out);
                setRbStatus(jbyl_yes_rb, jbyl_no_rb, entity.inspectionPooerInfo_each.is_have_medical);
                setRbStatus(zfaq_yes_rb, zfaq_no_rb, entity.inspectionPooerInfo_each.is_have_house);
                setRbStatus(cjgz_yes_rb, cjgz_no_rb, entity.inspectionPooerInfo_each.house_c_reform);
                setRbStatus(djgz_yes_rb, djgz_no_rb, entity.inspectionPooerInfo_each.house_d_create);
                setRbStatus(aqzf_yes_rb, aqzf_no_rb, entity.inspectionPooerInfo_each.house_have_agree);
                setRbStatus(zzbm_yes_rb, zzbm_no_rb, entity.inspectionPooerInfo_each.houser_have_report);
                setRbStatus(ysaq_yes_rb, ysaq_no_rb, entity.inspectionPooerInfo_each.is_have_water);
                setRbStatus(szbg_yes_rb, szbg_no_rb, entity.inspectionPooerInfo_each.water_have_report);
                setRbStatus(xdj_yes_rb, xdj_no_rb, entity.inspectionPooerInfo_each.water_have_create);
                setRbStatus(gxaz_yes_rb, gxaz_no_rb, entity.inspectionPooerInfo_each.water_have_line);
                setRbStatus(glsb_yes_rb, glsb_no_rb, entity.inspectionPooerInfo_each.water_have_device);
                setRbStatus(csr_yes_rb, csr_no_rb, entity.inspectionPooerInfo_each.year_pserson_income);
                setRbStatus(cytp_yes_rb, cytp_no_rb, entity.inspectionPooerInfo_each.is_have_help);
                setRbStatus(zcbz_yes_rb, zcbz_no_rb, entity.inspectionPooerInfo_each.is_have_policy);

                setRbStatus(bfgz_yes_rb, bfgz_no_rb, entity.inspectionPooerInfo_each.is_have_help_degree);
                setRbStatus(bflxk_yes_rb, bflxk_no_rb, entity.inspectionPooerInfo_each.is_have_help_link_card);
                setRbStatus(nrtxqq_yes_rb, nrtxqq_no_rb, entity.inspectionPooerInfo_each.is_have_content_whole);
                setRbStatus(pkyyysjxf_yes_rb, pkyyysjxf_no_rb, entity.inspectionPooerInfo_each.is_have_poor_reson_same);
                setRbStatus(cyfzcs_yes_rb, cyfzcs_no_rb, entity.inspectionPooerInfo_each.is_have_industry);
                setRbStatus(pkyyybfcsyz_yes_rb, pkyyybfcsyz_no_rb, entity.inspectionPooerInfo_each.is_have_reson_cushi_same);
                setRbStatus(bfsctxqq_yes_rb, bfsctxqq_no_rb, entity.inspectionPooerInfo_each.is_have_help_fill_whole);
                setRbStatus(wksjqq_yes_rb, wksjqq_no_rb, entity.inspectionPooerInfo_each.is_have_five_whole);
                setRbStatus(bftztxqq_yes_rb, bftztxqq_no_rb, entity.inspectionPooerInfo_each.is_have_help_ledger_whole);
                setRbStatus(szlsztxqq_yes_rb, szlsztxqq_no_rb, entity.inspectionPooerInfo_each.is_have_income_whole);
                setRbStatus(tptztxqq_yes_rb, tptztxqq_no_rb, entity.inspectionPooerInfo_each.is_have_tuopin_whole);
                setRbStatus(tpggztxqq_yes_rb, tpggztxqq_no_rb, entity.inspectionPooerInfo_each.is_have_tuopin_solid_whole);
                setRbStatus(datxzql_yes_rb, datxzql_no_rb, entity.inspectionPooerInfo_each.is_have_true);


                csr_et.setText(entity.inspectionPooerInfo_each.income_money);
                cytp_et.setText(entity.inspectionPooerInfo_each.help_method);
                zcbz_et.setText(entity.inspectionPooerInfo_each.policy_method);
                monitor_et.setText(entity.inspectionPooerInfo_each.inspection_manage);
                menber_et.setText(entity.inspectionPooerInfo_each.inspection_person);
                if (ysaq_yes_rb.isChecked()) {
                    szjc_layout.setVisibility(View.VISIBLE);
                    xdj_layout.setVisibility(View.GONE);
                    gxaz_layout.setVisibility(View.GONE);
                    glsb_layout.setVisibility(View.GONE);
                } else if (ysaq_no_rb.isChecked()) {
                    szjc_layout.setVisibility(View.GONE);
                    xdj_layout.setVisibility(View.VISIBLE);
                    gxaz_layout.setVisibility(View.VISIBLE);
                    glsb_layout.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {

        }
    }

    private void setRbStatus(RadioButton yes_rb, RadioButton no_rb, int status) {
        if (status == CountryDuChaEntity.YES) {
            yes_rb.setChecked(true);
        } else if (status == CountryDuChaEntity.NO) {
            no_rb.setChecked(true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            szjc_layout.setVisibility(View.VISIBLE);
            xdj_layout.setVisibility(View.GONE);
            gxaz_layout.setVisibility(View.GONE);
            glsb_layout.setVisibility(View.GONE);
        } else {
            szjc_layout.setVisibility(View.GONE);
            xdj_layout.setVisibility(View.VISIBLE);
            gxaz_layout.setVisibility(View.VISIBLE);
            glsb_layout.setVisibility(View.VISIBLE);
        }
    }

    private void save() {
        PeopleDetailEntity entity = new PeopleDetailEntity();
        if (bcc_yes_rb.isChecked()) {
            entity.is_have_eat = 1;
        } else if (bcc_no_rb.isChecked()) {
            entity.is_have_eat = 0;
        } else {
            showToast("请选择是否不愁吃");
            return;
        }
        if (bcchuan_yes_rb.isChecked()) {
            entity.is_have_wear = 1;
        } else if (bcchuan_no_rb.isChecked()) {
            entity.is_have_wear = 0;
        } else {
            showToast("请选择是否不愁穿");
            return;
        }
        if (pkcx_yes_rb.isChecked()) {
            entity.is_have_drop_out = 1;
        } else if (pkcx_no_rb.isChecked()) {
            entity.is_have_drop_out = 0;
        } else {
            showToast("请选择有无贫困辍学情况");
            return;
        }
        if (jbyl_yes_rb.isChecked()) {
            entity.is_have_medical = 1;
        } else if (jbyl_no_rb.isChecked()) {
            entity.is_have_medical = 0;
        } else {
            showToast("请选择是否基本医疗情况");
            return;
        }
        if (zfaq_yes_rb.isChecked()) {
            entity.is_have_house = 1;
        } else if (zfaq_no_rb.isChecked()) {
            entity.is_have_house = 0;
        } else {
            showToast("请选择是否住房安全情况");
            return;
        }
        if (cjgz_yes_rb.isChecked()) {
            entity.house_c_reform = 1;
        } else if (cjgz_no_rb.isChecked()) {
            entity.house_c_reform = 0;
        }
        if (djgz_yes_rb.isChecked()) {
            entity.house_d_create = 1;
        } else if (djgz_no_rb.isChecked()) {
            entity.house_d_create = 0;
        }
        if (aqzf_yes_rb.isChecked()) {
            entity.house_have_agree = 1;
        } else if (aqzf_no_rb.isChecked()) {
            entity.house_have_agree = 0;
        }
        if (zzbm_yes_rb.isChecked()) {
            entity.houser_have_report = 1;
        } else if (zzbm_no_rb.isChecked()) {
            entity.houser_have_report = 0;
        }
        if (ysaq_yes_rb.isChecked()) {
            entity.is_have_water = 1;
            if (szbg_yes_rb.isChecked()) {
                entity.water_have_report = 1;
            } else if (szbg_no_rb.isChecked()) {
                entity.water_have_report = 0;
            } else {
                showToast("请选择有无水质监测报告");
                return;
            }
        } else if (ysaq_no_rb.isChecked()) {
            entity.is_have_water = 0;
        } else {
            showToast("请选择有无饮水安全情况");
            return;
        }
        if (xdj_yes_rb.isChecked()) {
            entity.water_have_create = 1;
        } else if (xdj_no_rb.isChecked()) {
            entity.water_have_create = 0;
        }
        if (gxaz_yes_rb.isChecked()) {
            entity.water_have_line = 1;
        } else if (gxaz_no_rb.isChecked()) {
            entity.water_have_line = 0;
        }
        if (glsb_yes_rb.isChecked()) {
            entity.water_have_device = 1;
        } else if (glsb_no_rb.isChecked()) {
            entity.water_have_device = 0;
        }
        if (csr_yes_rb.isChecked()) {
            entity.year_pserson_income = 1;
        } else if (csr_no_rb.isChecked()) {
            entity.year_pserson_income = 0;
        } else {
            showToast("请选择年人均纯收入是否达标");
            return;
        }
        entity.income_money = csr_et.getText().toString();
        if (cytp_yes_rb.isChecked()) {
            entity.is_have_help = 1;
        } else if (cytp_no_rb.isChecked()) {
            entity.is_have_help = 0;
        }
        entity.help_method = cytp_et.getText().toString();
        if (zcbz_yes_rb.isChecked()) {
            entity.is_have_policy = 1;
        } else if (zcbz_no_rb.isChecked()) {
            entity.is_have_policy = 0;
        }

        if (bfgz_yes_rb.isChecked()) {
            entity.is_have_help_degree = 1;
        } else if (bfgz_no_rb.isChecked()) {
            entity.is_have_help_degree = 0;
        }
        if (bflxk_yes_rb.isChecked()) {
            entity.is_have_help_link_card = 1;
        } else if (bflxk_no_rb.isChecked()) {
            entity.is_have_help_link_card = 0;
        }
        if (nrtxqq_yes_rb.isChecked()) {
            entity.is_have_content_whole = 1;
        } else if (nrtxqq_no_rb.isChecked()) {
            entity.is_have_content_whole = 0;
        }
        if (pkyyysjxf_yes_rb.isChecked()) {
            entity.is_have_poor_reson_same = 1;
        } else if (pkyyysjxf_no_rb.isChecked()) {
            entity.is_have_poor_reson_same = 0;
        }
        if (cyfzcs_yes_rb.isChecked()) {
            entity.is_have_industry = 1;
        } else if (cyfzcs_no_rb.isChecked()) {
            entity.is_have_industry = 0;
        }
        if (pkyyybfcsyz_yes_rb.isChecked()) {
            entity.is_have_reson_cushi_same = 1;
        } else if (pkyyybfcsyz_no_rb.isChecked()) {
            entity.is_have_reson_cushi_same = 0;
        }
        if (bfsctxqq_yes_rb.isChecked()) {
            entity.is_have_help_fill_whole = 1;
        } else if (bfsctxqq_no_rb.isChecked()) {
            entity.is_have_help_fill_whole = 0;
        }
        if (wksjqq_yes_rb.isChecked()) {
            entity.is_have_five_whole = 1;
        } else if (wksjqq_no_rb.isChecked()) {
            entity.is_have_five_whole = 0;
        }
        if (bftztxqq_yes_rb.isChecked()) {
            entity.is_have_help_ledger_whole = 1;
        } else if (bftztxqq_no_rb.isChecked()) {
            entity.is_have_help_ledger_whole = 0;
        }
        if (szlsztxqq_yes_rb.isChecked()) {
            entity.is_have_income_whole = 1;
        } else if (szlsztxqq_no_rb.isChecked()) {
            entity.is_have_income_whole = 0;
        }
        if (tptztxqq_yes_rb.isChecked()) {
            entity.is_have_tuopin_whole = 1;
        } else if (tptztxqq_no_rb.isChecked()) {
            entity.is_have_tuopin_whole = 0;
        }
        if (tpggztxqq_yes_rb.isChecked()) {
            entity.is_have_tuopin_solid_whole = 1;
        } else if (tpggztxqq_no_rb.isChecked()) {
            entity.is_have_tuopin_solid_whole = 0;
        }
        if (datxzql_yes_rb.isChecked()) {
            entity.is_have_true = 1;
        } else if (datxzql_no_rb.isChecked()) {
            entity.is_have_true = 0;
        }
        entity.policy_method = zcbz_et.getText().toString();
        entity.inspection_manage = monitor_et.getText().toString();
        entity.inspection_person = menber_et.getText().toString();
        SavePeopleInspectRequest request = new SavePeopleInspectRequest(configEntity.key, inspection_poor_id, entity);
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), SavePeopleInspectRequest.SAVE_COUNTRY_INSPECT_REQUEST);
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
        mCurrentIndex = pos;
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
