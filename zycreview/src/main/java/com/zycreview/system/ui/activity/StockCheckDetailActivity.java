package com.zycreview.system.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.zycreview.system.R;
import com.zycreview.system.entity.StockCheckEntity;

public class StockCheckDetailActivity extends BaseActivity {

    private TextView code, name, num, weight, grow, unit;
    private StockCheckEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_check_detail);
        entity = (StockCheckEntity) getIntent().getSerializableExtra("entity");
        initView();
        initListener();
        setData();
    }

    private void initView(){
        initTopView();
        setTitle("库存详情");
        setLeftBackButton();
        code = (TextView) findViewById(R.id.tv_stock_check_code);
        name = (TextView) findViewById(R.id.tv_stock_check_name);
        num = (TextView) findViewById(R.id.tv_stock_check_num);
        weight = (TextView) findViewById(R.id.tv_stock_check_weight);
        grow = (TextView) findViewById(R.id.tv_stock_check_grow);
        unit = (TextView) findViewById(R.id.tv_stock_check_unit);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
    }

    private void setData(){
//        code.setText(entity.input_code);
        name.setText(entity.drugs_name);
        num.setText(entity.drugs_code);
        weight.setText(entity.drugs_number);
        grow.setText(entity.grow_pattern);
        unit.setText(entity.drugs_unit);
    }
}
