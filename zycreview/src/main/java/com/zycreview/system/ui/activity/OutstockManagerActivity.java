package com.zycreview.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zycreview.system.R;

/**
 * 库存管理
 */
public class OutstockManagerActivity extends BaseActivity implements View.OnClickListener{

    private View makeView, cheView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstock_manager);
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setTitle("库存管理");
        setLeftBackButton();
        makeView = findViewById(R.id.rel_outstock_make);
        cheView = findViewById(R.id.rel_outstock_check);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        makeView.setOnClickListener(this);
        cheView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rel_outstock_make:
                startActivity(new Intent(this,TradedManageActivity.class));
                break;
            case R.id.rel_outstock_check:
                startActivity(new Intent(this,StockCheckActivity.class));
                break;
        }
    }

}
