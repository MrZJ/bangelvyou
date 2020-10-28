package com.zycreview.system.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.zycreview.system.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 药材分包操作界面
 */
public class SubPackageDetailActivity extends BaseActivity implements View.OnClickListener,OnDateSetListener{

    private TextView num, type, name, grow, instockDate, totalWeight, packNum, subDate, endDate;
    private EditText subWeight, subRule, suber;
    private String dateType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_package_detail);
    }

    private void initView(){
        initTopView();
        setTitle("分包");
        setLeftBackButton();
        num = (TextView) findViewById(R.id.tv_sub_package_num);//入库批次号
        type = (TextView) findViewById(R.id.tv_sub_package_type);
        name = (TextView) findViewById(R.id.tv_sub_package_name);
        grow = (TextView) findViewById(R.id.tv_sub_package_grow);
        instockDate = (TextView) findViewById(R.id.tv_sub_package_instockdate);
        totalWeight = (TextView) findViewById(R.id.tv_sub_package_totalweight);
        subWeight = (EditText) findViewById(R.id.edt_sub_package_weight);
        subRule = (EditText) findViewById(R.id.edt_sub_package_rule);
        packNum = (TextView) findViewById(R.id.tv_sub_package_packagenum);
        suber = (EditText) findViewById(R.id.edt_sub_package_suber);
        subDate = (TextView) findViewById(R.id.tv_sub_package_subdate);
        endDate = (TextView) findViewById(R.id.tv_sub_package_enddate);
        initViewDateDialog(this);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        subDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_sub_package_subdate:
                dateType = "sub";
                if(!dialog.isAdded()) {
                    dialog.show(getSupportFragmentManager(), "all");
                }
                break;
            case R.id.tv_sub_package_enddate:
                dateType = "end";
                if(!dialog.isAdded()) {
                    dialog.show(getSupportFragmentManager(), "all");
                }
                break;
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date d = new Date(millseconds);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        if(dateType.equals("sub")){
            subDate.setText(sf.format(d));
        }else{
            endDate.setText(sf.format(d));
        }
    }
}
