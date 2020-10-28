package com.fuping.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.fuping.system.R;
import com.fuping.system.adapter.RelationAdapter;
import com.fuping.system.entity.MenberEntity;
import com.fuping.system.entity.PeoplePoorTownEntity;
import com.fuping.system.request.PeopleMenberRequest;
import com.fuping.system.utils.ListUtils;

/**
 * Created by jianzhang.
 * on 2017/10/24.
 * copyright easybiz.
 */

public class RelationShipActivity extends BaseActivity {
    private ListView listview;
    private String poor_id;

    public static void startActivity(Context context, String poor_id) {
        Intent intent = new Intent(context, RelationShipActivity.class);
        intent.putExtra("poor_id", poor_id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relation_list);
        poor_id = getIntent().getStringExtra("poor_id");
        if (poor_id == null) {
            finish();
            return;
        }
        initTopView();
        initListener();
        setLeftBackButton();
        initSideBarListener();
        setTitle("家庭成员查看");
        initView();
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.listview);
        loadData();
    }

    private void loadData() {
        PeopleMenberRequest request = new PeopleMenberRequest(configEntity.key, poor_id);
        httpGetRequest(request.getRequestUrl(), PeopleMenberRequest.PEOPLE_MENBER_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case PeopleMenberRequest.PEOPLE_MENBER_REQUEST:
                try {
                    PeoplePoorTownEntity entity = JSONObject.parseObject(json, PeoplePoorTownEntity.class);
                    if (entity != null && !ListUtils.isEmpty(entity.familyInfoList)) {
                        MenberEntity menberEntity = new MenberEntity();
                        menberEntity.family_name = "姓名";
                        menberEntity.relation_ship = "与户主关系";
                        entity.familyInfoList.add(0, menberEntity);
                        listview.setAdapter(new RelationAdapter(this, entity.familyInfoList));
                    } else {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
