package com.zycreview.system.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zycreview.system.R;
import com.zycreview.system.adapter.PlantDetailAdapter;
import com.zycreview.system.api.PlantTaskDetailApi;
import com.zycreview.system.entity.PlantDetailEntity;
import com.zycreview.system.entity.PlantTaskDetailEntity;
import com.zycreview.system.utils.StringUtil;

import java.util.List;

/**
 * 种植详情界面
 */
public class PlantDetailActivity extends AbsLoadMoreActivity<ListView,PlantDetailEntity> {

    private PullToRefreshListView mPullToRefreshListView;
    private TextView tv_head_plant_task_name;
    private TextView tv_head_plant_name;
    private TextView tv_head_plant_base;
    private TextView tv_head_plant_area;
    private TextView tv_head_plant_source;
    private TextView tv_head_plant_weight;
    private TextView tv_head_plant_principal;
    private TextView tv_head_plant_principal_phone;
    private TextView tv_head_plant_week;
    private TextView tv_head_plant_time;
    private String id;
    private String time;
    private String plantType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);
        id = getIntent().getStringExtra("id");
        time = getIntent().getStringExtra("time");
        plantType = getIntent().getStringExtra("type");
        initView();
        initListener();
        setAdapter();
        loadData();
    }

    private void initView() {
        initTopView();
        setTitle("种植详情");
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.plant_task_list);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.head_plant_detail, null);
        tv_head_plant_task_name = (TextView) view.findViewById(R.id.tv_head_plant_task_name);
        tv_head_plant_name = (TextView) view.findViewById(R.id.tv_head_plant_name);
        tv_head_plant_base = (TextView) view.findViewById(R.id.tv_head_plant_base);
        tv_head_plant_area = (TextView) view.findViewById(R.id.tv_head_plant_area);
        tv_head_plant_source = (TextView) view.findViewById(R.id.tv_head_plant_source);
        tv_head_plant_weight = (TextView) view.findViewById(R.id.tv_head_plant_weight);
        tv_head_plant_principal = (TextView) view.findViewById(R.id.tv_head_plant_principal);
        tv_head_plant_principal_phone = (TextView) view.findViewById(R.id.tv_head_plant_principal_phone);
        tv_head_plant_week = (TextView) view.findViewById(R.id.tv_head_plant_week);
        tv_head_plant_time = (TextView) view.findViewById(R.id.tv_head_plant_time);
        mPullToRefreshListView.getRefreshableView().addHeaderView(view);
        if (!StringUtil.isEmpty(time)) {
            tv_head_plant_time.setText(time);
        } else {
            tv_head_plant_time.setText("无");
        }
    }

    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
    }

    private void setAdapter(){
        mAdapter = new PlantDetailAdapter(null,this);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }



    @Override
    protected void loadData() {
        if (plantType.equals("管理")) {
            httpGetRequest(PlantTaskDetailApi.getPlantTaskDetailUrl(id,configEntity.key),PlantTaskDetailApi.API_PLANTTASK_DETAIL);
        } else {
            httpGetRequest(PlantTaskDetailApi.getPlantHistoryDetailUrl(id,configEntity.key),PlantTaskDetailApi.API_PLANTHISTORY_DETAIL);
        }
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case PlantTaskDetailApi.API_PLANTTASK_DETAIL:
            case PlantTaskDetailApi.API_PLANTHISTORY_DETAIL:
                plantTaskHander(json);
                break;
        }
    }

    private void plantTaskHander(String json){
        final long start = System.currentTimeMillis();
        if(!StringUtil.isEmpty(json)){
            PlantTaskDetailEntity entity = JSON.parseObject(json, PlantTaskDetailEntity.class);
            if (!StringUtil.isEmpty(entity.name)) {
                tv_head_plant_task_name.setText(entity.name);
            } else {
                tv_head_plant_task_name.setText("无");
            }
            if (!StringUtil.isEmpty(entity.plant_name)) {
                tv_head_plant_name.setText(entity.plant_name);
            } else {
                tv_head_plant_name.setText("无");
            }
            if (!StringUtil.isEmpty(entity.base_code_in)) {
                tv_head_plant_base.setText(entity.base_code_in);
            } else {
                tv_head_plant_base.setText("无");
            }
            if (!StringUtil.isEmpty(entity.plant_area)) {
                tv_head_plant_area.setText(entity.plant_area);
            } else {
                tv_head_plant_area.setText("无");
            }if (!StringUtil.isEmpty(entity.seed_source)) {
                tv_head_plant_source.setText(entity.seed_source);
            } else {
                tv_head_plant_source.setText("无");
            }
            if (!StringUtil.isEmpty(entity.seed_weight)) {
                tv_head_plant_weight.setText(entity.seed_weight);
            } else {
                tv_head_plant_weight.setText("无");
            }
            if (!StringUtil.isEmpty(entity.manager)) {
                tv_head_plant_principal.setText(entity.manager);
            } else {
                tv_head_plant_principal.setText("无");
            }
            if (!StringUtil.isEmpty(entity.manager_phone)) {
                tv_head_plant_principal_phone.setText(entity.manager_phone);
            } else {
                tv_head_plant_principal_phone.setText("无");
            }
            if (!StringUtil.isEmpty(entity.plant_zq)) {
                tv_head_plant_week.setText(entity.plant_zq);
            } else {
                tv_head_plant_week.setText("无");
            }
            if (null != entity.list) {
                List<PlantDetailEntity> infos = entity.list;
                appendData(infos, start,null);
            }
        }
    }

}
