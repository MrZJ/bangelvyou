package com.zycreview.system.ui.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zycreview.system.R;
import com.zycreview.system.api.CheckManageApi;
import com.zycreview.system.entity.CheckDetailEntity;
import com.zycreview.system.entity.FieldErrors;
import com.zycreview.system.utils.ListUtils;
import com.zycreview.system.utils.StringUtil;

/**
 * 检验管理检查界面
 */
public class CheckDetailActivity extends BaseActivity {

    private String id;
    private TextView medicinal_name;
    private TextView executive_standard;
    private TextView check_detail;
    private TextView holding_conditions;
    private TextView check_time;
    private TextView check_person;
    private LinearLayout checkItemName,checkItemContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);
        id = getIntent().getStringExtra("id");
        initView();
        initListener();
        setData();
    }

    private void initView() {
        initTopView();
        setTitle("药材检验详情");
        medicinal_name = (TextView) findViewById(R.id.medicinal_name);
        executive_standard = (TextView) findViewById(R.id.executive_standard);
        check_detail = (TextView) findViewById(R.id.check_detail);
        holding_conditions = (TextView) findViewById(R.id.holding_conditions);
        check_time = (TextView) findViewById(R.id.check_time);
        check_person = (TextView) findViewById(R.id.check_person);
        checkItemName = (LinearLayout) findViewById(R.id.lin_check_detail_name);
        checkItemContent = (LinearLayout) findViewById(R.id.lin_check_detail_text);
    }

    public void initListener() {
        super.initListener();
        initSideBarListener();
    }

    private void setData() {
        httpGetRequest(CheckManageApi.getCheckSeeCheckUrl(id, configEntity.key), CheckManageApi.API_CHECKSEE_CHECK);
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case CheckManageApi.API_CHECKSEE_CHECK:
                checkHander(json);
                break;
        }
    }

    private void checkHander(String json){
        if(!StringUtil.isEmpty(json)){
            CheckDetailEntity entity = JSON.parseObject(json, CheckDetailEntity.class);
            medicinal_name.setText(entity.testName);
            executive_standard.setText(entity.rm_norm);
            check_time.setText(entity.testDate);
            holding_conditions.setText(entity.holdz_cond);
            check_person.setText(entity.testMan);
            if(entity !=null && !ListUtils.isEmpty(entity.methods)){
                for (int i = 0; i < entity.methods.size(); i++) {
                    CheckDetailEntity.CheckItem  checkItem = entity.methods.get(i);
                    TextView checkName = (TextView) getLayoutInflater().inflate(R.layout.item_check_name_tv, null);
                    TextView checkContent = (TextView) getLayoutInflater().inflate(R.layout.item_check_result_tv, null);
                    checkName.setText(checkItem.item_name+":");
                    checkContent.setText(checkItem.result);
                    checkItemName.addView(checkName);
                    checkItemContent.addView(checkContent);
                }
            }
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
    }

}
