package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.util.ConfigUtil;

/**
 * 我的积分
 */
public class MyPointActivity extends BaseActivity implements View.OnClickListener{

    private TextView pointTotal , pointDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_point);
        configEntity = ConfigUtil.loadConfig(this);
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setTitle("我的积分");
        setLeftBackButton();
        pointTotal = (TextView) findViewById(R.id.tv_my_point_total);
        pointDetail = (TextView) findViewById(R.id.tv_my_point_detail);
        pointTotal.setText("当前积分:"+configEntity.point);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        pointDetail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_my_point_detail:
                Intent pointDetail = new Intent(this,PointDetailActivity.class);
                startActivity(pointDetail);
                break;
        }
    }
}
