package com.zycreview.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zycreview.system.R;

/**
 * 检验管理
 */
public class InspectManagerActivity extends BaseActivity implements View.OnClickListener{

    private View magView, hisView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_manager);
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setTitle("检验管理");
        setLeftBackButton();
        magView = findViewById(R.id.rel_inspect_mag);
        hisView = findViewById(R.id.rel_inspect_mag_his);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        magView.setOnClickListener(this);
        hisView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, CheckListActivity.class);
        switch (v.getId()){
            case R.id.rel_inspect_mag:
                intent.putExtra("checkType", "检验管理");
                break;
            case R.id.rel_inspect_mag_his:
                intent.putExtra("checkType", "已检药材");
                break;
        }
        startActivity(intent);
    }
}
