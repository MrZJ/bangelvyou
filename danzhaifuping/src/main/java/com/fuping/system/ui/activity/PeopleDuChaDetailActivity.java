package com.fuping.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fuping.system.R;
import com.fuping.system.entity.PeopleDetailEntity;
import com.fuping.system.entity.PeoplePoorTownEntity;
import com.fuping.system.request.PeopleDetailRequest;
import com.fuping.system.utils.StringUtil;

import org.xutils.x;

/**
 * Created by jianzhang.
 * on 2017/10/18.
 * copyright easybiz.
 */

public class PeopleDuChaDetailActivity extends BaseActivity implements View.OnClickListener {
    private String poor_id;
    private TextView name_tv, age_tv, id_tv, phone_tv, reason_tv, loc_tv,
            family_menber_tv, goverment_tv, monitor_tv, monitor_phone_tv, check_tv;
    private ImageView imageView, img_top_goback;

    public static void startActivity(Context context, String poor_id) {
        Intent intent = new Intent(context, PeopleDuChaDetailActivity.class);
        intent.putExtra("poor_id", poor_id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_ducha_detail);
        poor_id = getIntent().getStringExtra("poor_id");
        if (poor_id == null) {
            finish();
            return;
        }
        initTopView();
        initListener();
        setLeftBackButton();
        initSideBarListener();
        setTitle("贫困户");
        btn_top_sidebar.setVisibility(View.VISIBLE);
        top_right_title.setVisibility(View.VISIBLE);
        top_right_title.setText("脱贫督查");
        top_right_title.setOnClickListener(this);
        initView();
    }

    private void initView() {
        name_tv = (TextView) findViewById(R.id.name_tv);
        age_tv = (TextView) findViewById(R.id.age_tv);
        id_tv = (TextView) findViewById(R.id.id_tv);
        phone_tv = (TextView) findViewById(R.id.phone_tv);
        reason_tv = (TextView) findViewById(R.id.reason_tv);
        loc_tv = (TextView) findViewById(R.id.loc_tv);
        family_menber_tv = (TextView) findViewById(R.id.family_menber_tv);
        goverment_tv = (TextView) findViewById(R.id.goverment_tv);
        monitor_tv = (TextView) findViewById(R.id.monitor_tv);
        monitor_phone_tv = (TextView) findViewById(R.id.monitor_phone_tv);
        imageView = (ImageView) findViewById(R.id.imageview);
        img_top_goback = (ImageView) findViewById(R.id.img_top_goback);
        img_top_goback.setOnClickListener(this);
        check_tv = (TextView) findViewById(R.id.check_tv);
        check_tv.setOnClickListener(this);
        loadData();
    }

    @Override
    public void onClick(View v) {
        if (img_top_goback == v) {
            finish();
        } else if (check_tv == v) {
            if (peopleDetailEntity != null && !"0".equals(peopleDetailEntity.familyInfo_count)) {
                RelationShipActivity.startActivity(this, poor_id);
            } else {
                showToast("没有家庭成员");
            }
        } else if (top_right_title == v) {
            DuChaListActivity.startActivity(this, DuChaListActivity.TYPE_PEOPLE, poor_id);
        }
    }

    private void loadData() {
        PeopleDetailRequest request = new PeopleDetailRequest(configEntity.key, poor_id);
        httpGetRequest(request.getRequestUrl(), PeopleDetailRequest.PEOPLE_DETAIL_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case PeopleDetailRequest.PEOPLE_DETAIL_REQUEST:
                handleRequest(json);
                break;
        }
    }

    public PeopleDetailEntity peopleDetailEntity;

    private void handleRequest(String json) {
        try {
            PeoplePoorTownEntity entity = JSONObject.parseObject(json, PeoplePoorTownEntity.class);
            if (entity != null && entity.poor_each != null) {
                peopleDetailEntity = entity.poor_each;
                setData(entity.poor_each);
            } else {
                showToast("请求失败");
                finish();
            }
        } catch (Exception e) {
        }
    }

    private void setData(PeopleDetailEntity entity) {
        name_tv.setText(entity.real_name);
        age_tv.setText(entity.age);
        id_tv.setText(entity.id_card);
        phone_tv.setText(entity.mobile);
        reason_tv.setText(entity.poor_reson);
        loc_tv.setText(entity.real_addr);
        family_menber_tv.setText(entity.familyInfo_count);
        goverment_tv.setText(entity.bfdw_name);
        monitor_tv.setText(entity.bfr_name);
        monitor_phone_tv.setText(entity.bfr_tel);
        if (!StringUtil.isEmpty(entity.image_path)) {
            x.image().bind(imageView, entity.image_path);
        }
    }
}
