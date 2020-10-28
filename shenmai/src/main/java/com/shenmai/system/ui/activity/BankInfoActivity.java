package com.shenmai.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.shenmai.system.R;
import com.shenmai.system.api.RegisterApi;
import com.shenmai.system.entity.BankEntity;

/**
 * 银行账户信息显示界面
 */
public class BankInfoActivity extends BaseActivity implements View.OnClickListener{

    private TextView userName, bankName, bankNum;
    private Button addOrEdt;
    private BankEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_info);
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBankInfo();
    }

    private void initView(){
        initTopView();
        setTitle("提现信息");
        setLeftBackButton();
        userName = (TextView) findViewById(R.id.tv_bank_info_user);
        bankName = (TextView) findViewById(R.id.tv_bank_info_bankname);
        bankNum = (TextView) findViewById(R.id.tv_bank_info_banknum);
        addOrEdt = (Button) findViewById(R.id.btn_bank_info_add);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        addOrEdt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bank_info_add:
                Intent bankSet = new Intent(this,BankSetActivity.class);
                if(entity != null){
                    bankSet.putExtra("name",entity.bank_account_name);
                    bankSet.putExtra("bankname",entity.bank_name);
                    bankSet.putExtra("cardnum",entity.bank_account);
                }
                startActivity(bankSet);
                break;
        }
    }

    private void  getBankInfo(){
        httpGetRequest(RegisterApi.getBankInfoUrl(configEntity.key),RegisterApi.API_GET_BANK_INFO);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case RegisterApi.API_GET_BANK_INFO:
                infoHander(json);
                break;
        }
    }

    private void infoHander(String json){
        entity = JSON.parseObject(json, BankEntity.class);
        if(entity != null){
            userName.setText(entity.bank_account_name);
            bankName.setText(entity.bank_name);
            bankNum.setText(entity.bank_account);
        }
    }
}
