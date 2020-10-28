package com.zycreview.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zycreview.system.R;
import com.zycreview.system.utils.TopClickUtil;

/**
 * 种植管理
 */
public class PlantManageActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_plant_task, tv_plant_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_manage);
        initView();
        initListener();
    }

    private void initView() {
        initTopView();
        setTitle("种植管理");
        tv_plant_task = (TextView) findViewById(R.id.tv_plant_task);
        tv_plant_history = (TextView) findViewById(R.id.tv_plant_history);
    }

    public void initListener() {
        super.initListener();
        initSideBarListener(TopClickUtil.TOP_PLANT_BACK);
        tv_plant_task.setOnClickListener(this);
        tv_plant_history.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, PlantTaskActivity.class);
        switch (v.getId()) {
            case R.id.tv_plant_task://种植任务
                intent.putExtra("plantType", "管理");
                break;
            case R.id.tv_plant_history://种植历史
                intent.putExtra("plantType", "历史");
                break;
        }
        startActivity(intent);
    }
}
