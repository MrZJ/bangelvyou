package com.fuping.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fuping.system.R;
import com.fuping.system.entity.CountryDetailAllEntity;
import com.fuping.system.entity.CountryDetailEntity;
import com.fuping.system.request.CountryDetailRequest;
import com.fuping.system.utils.StringUtil;

/**
 * Created by jianzhang.
 * on 2017/10/18.
 * copyright easybiz.
 */

public class CountryDuChaDetailActivity extends BaseActivity implements View.OnClickListener {
    private String village_id;
    private int is_poor_village;
    private TextView country_name_tv, loc_name_tv, country_chager_tv,
            country_chager_phone_tv, country_meet_chager_tv, meet_chager_phone_tv,
            monitor_tv, monitor_phone_tv, fram_size_tv, group_count_tv, town_num_tv,
            people_situation_tv, household_num_tv, population_tv, poor_household_tv, poor_population_tv;

    public static void startActivity(Context context, String village_id, int is_poor_village) {
        Intent intent = new Intent(context, CountryDuChaDetailActivity.class);
        intent.putExtra("village_id", village_id);
        intent.putExtra("is_poor_village", is_poor_village);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_ducha_detail);
        village_id = getIntent().getStringExtra("village_id");
        is_poor_village = getIntent().getIntExtra("is_poor_village", -1);
        if (StringUtil.isEmpty(village_id)) {
            finish();
            return;
        }
        initTopView();
        initListener();
        setLeftBackButton();
        initSideBarListener();
        setTitle("贫困村");
        btn_top_sidebar.setVisibility(View.VISIBLE);
        if (is_poor_village == 1) {
            top_right_title.setVisibility(View.VISIBLE);
        } else {
            top_right_title.setVisibility(View.GONE);
        }
        top_right_title.setText("脱贫督查");
        top_right_title.setOnClickListener(this);
        initView();
        loadData();
    }

    private void initView() {
        country_name_tv = (TextView) findViewById(R.id.country_name_tv);
        loc_name_tv = (TextView) findViewById(R.id.loc_name_tv);
        country_chager_tv = (TextView) findViewById(R.id.country_chager_tv);
        country_chager_phone_tv = (TextView) findViewById(R.id.country_chager_phone_tv);
        country_meet_chager_tv = (TextView) findViewById(R.id.country_meet_chager_tv);
        meet_chager_phone_tv = (TextView) findViewById(R.id.meet_chager_phone_tv);
        monitor_tv = (TextView) findViewById(R.id.monitor_tv);
        monitor_phone_tv = (TextView) findViewById(R.id.monitor_phone_tv);
        fram_size_tv = (TextView) findViewById(R.id.fram_size_tv);
        group_count_tv = (TextView) findViewById(R.id.group_count_tv);
        town_num_tv = (TextView) findViewById(R.id.town_num_tv);
        people_situation_tv = (TextView) findViewById(R.id.people_situation_tv);
        household_num_tv = (TextView) findViewById(R.id.household_num_tv);
        population_tv = (TextView) findViewById(R.id.population_tv);
        poor_household_tv = (TextView) findViewById(R.id.poor_household_tv);
        poor_population_tv = (TextView) findViewById(R.id.poor_population_tv);
    }

    private void loadData() {
        CountryDetailRequest request = new CountryDetailRequest(configEntity.key, village_id);
        httpGetRequest(request.getRequestUrl(), CountryDetailRequest.COUNTRY_DETAIL_REQUEST);
    }

    @Override
    public void onClick(View v) {
        if (top_right_title == v) {
            DuChaListActivity.startActivity(this, DuChaListActivity.TYPE_COUNTRY, village_id);
        }
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case CountryDetailRequest.COUNTRY_DETAIL_REQUEST:
                handleRequest(json);
                break;
        }
    }

    private void handleRequest(String json) {
        try {
            CountryDetailAllEntity detailAllEntity = JSONObject.parseObject(json, CountryDetailAllEntity.class);
            if (detailAllEntity != null && detailAllEntity.village_each != null) {
                initData(detailAllEntity.village_each);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData(CountryDetailEntity entity) {
        country_name_tv.setText(entity.name);
        loc_name_tv.setText(entity.p_name);
        country_chager_tv.setText(entity.party_respon);
        country_chager_phone_tv.setText(entity.party_respon_tel);
        country_meet_chager_tv.setText(entity.village_respon);
        meet_chager_phone_tv.setText(entity.village_tel);
        monitor_tv.setText(entity.village_secretary);
        monitor_phone_tv.setText(entity.secretary_mobile);
        fram_size_tv.setText(entity.land_area);
        group_count_tv.setText(entity.group_count);
        town_num_tv.setText(entity.tun_count);
        people_situation_tv.setText(entity.develop_desc);
        household_num_tv.setText(entity.house_number);
        population_tv.setText(entity.per_number);
        poor_household_tv.setText(entity.poor_house_number);
        poor_population_tv.setText(entity.poor_per_number);
    }
}
