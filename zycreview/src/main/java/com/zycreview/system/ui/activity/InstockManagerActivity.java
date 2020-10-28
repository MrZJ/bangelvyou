package com.zycreview.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zycreview.system.R;

/**
 * 入库管理
 */
public class InstockManagerActivity extends BaseActivity implements View.OnClickListener {

    private View magView, recView, subView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instock);
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setTitle("入库管理");
        setLeftBackButton();
        magView = findViewById(R.id.rel_instock_mag);
        recView = findViewById(R.id.rel_instock_rec);
//        subView = findViewById(R.id.rel_instock_subpackage);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        magView.setOnClickListener(this);
        recView.setOnClickListener(this);
//        subView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rel_instock_mag:
                startActivity(new Intent(this,MedicinalInstockActivity.class));
                break;
            case R.id.rel_instock_rec:
                startActivity(new Intent(this,InstockRecordActivity.class));
                break;
           /* case R.id.rel_instock_subpackage:
                startActivity(new Intent(this,SubPackageActivity.class));
                break;*/
        }
    }
}
