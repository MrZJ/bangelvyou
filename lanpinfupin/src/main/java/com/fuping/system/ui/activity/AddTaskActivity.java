package com.fuping.system.ui.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fuping.system.R;
import com.fuping.system.adapter.AutoCompleteCountryAdapter;
import com.fuping.system.adapter.AutoCompleteGovermentAdapter;
import com.fuping.system.entity.AddTaskDetailEntity;
import com.fuping.system.entity.CountryEntity;
import com.fuping.system.entity.FieldErrors;
import com.fuping.system.entity.GovermentEntity;
import com.fuping.system.request.AddTaskDetailRequest;
import com.fuping.system.request.SaveTaskRequest;
import com.fuping.system.utils.ListUtils;
import com.fuping.system.utils.StringUtil;
import com.fuping.system.wibget.AdvancedAutoCompleteTextView;

import java.util.Calendar;

/**
 * Created by jianzhang.
 * on 2017/10/9.
 * copyright easybiz.
 */

public class AddTaskActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private AdvancedAutoCompleteTextView pool_country_sp, associate_goverment_sp;
    private AutoCompleteCountryAdapter countryAdapter;
    private AutoCompleteGovermentAdapter govermentAdapter;
    private View add_task;
    private TextView task_name_tv, task_taget_tv, complete_time_tv, connecter_tv;
    private CountryEntity mCountryEntity;
    private GovermentEntity mGovermentEntity;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddTaskActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        initTopView();
        initListener();
        setLeftBackButton();
        initSideBarListener();
        setTitle("添加任务");
        initView();
    }

    private void initView() {
        pool_country_sp = (AdvancedAutoCompleteTextView) findViewById(R.id.pool_country_sp);
        pool_country_sp.setThreshold(0);
        associate_goverment_sp = (AdvancedAutoCompleteTextView) findViewById(R.id.associate_goverment_sp);
        associate_goverment_sp.setThreshold(0);
        add_task = findViewById(R.id.add_task);
        add_task.setOnClickListener(this);
        task_name_tv = (TextView) findViewById(R.id.task_name_tv);
        task_taget_tv = (TextView) findViewById(R.id.task_taget_tv);
        complete_time_tv = (TextView) findViewById(R.id.complete_time_tv);
        complete_time_tv.setOnClickListener(this);
        connecter_tv = (TextView) findViewById(R.id.connecter_tv);
        loadData();
    }

    private void loadData() {
        AddTaskDetailRequest request = new AddTaskDetailRequest();
        httpGetRequest(request.getRequestUrl(), AddTaskDetailRequest.HELP_DETAIL_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case AddTaskDetailRequest.HELP_DETAIL_REQUEST:
                handRequest(json);
                break;
            case SaveTaskRequest.SAVE_TASK_REQUEST:
                showToast("提交成功");
                finish();
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        switch (action) {
            case AddTaskDetailRequest.HELP_DETAIL_REQUEST:
                showToast("获取信息失败");
                break;
            case SaveTaskRequest.SAVE_TASK_REQUEST:
                showToast("提交失败");
                break;
        }
    }

    private void handRequest(String json) {
        try {
            AddTaskDetailEntity parentEntity = JSONObject.parseObject(json, AddTaskDetailEntity.class);
            if (parentEntity == null || ListUtils.isEmpty(parentEntity.poorvillageList) || ListUtils.isEmpty(parentEntity.unitinfoList)) {
                showToast("获取贫困村列表或帮扶单位列表有问题");
                finish();
                return;
            }
            countryAdapter = new AutoCompleteCountryAdapter(this, parentEntity.poorvillageList, new AutoCompleteCountryAdapter.OnItemClick() {
                @Override
                public void onItemClick(CountryEntity entity) {
                    mCountryEntity = entity;
                    pool_country_sp.setText2(entity.link_village_name);
                    pool_country_sp.hidden(AddTaskActivity.this);
                    pool_country_sp.hideList();
                }
            });
            govermentAdapter = new AutoCompleteGovermentAdapter(this, parentEntity.unitinfoList, new AutoCompleteGovermentAdapter.OnItemClick() {
                @Override
                public void onItemClick(GovermentEntity entity) {
                    mGovermentEntity = entity;
                    associate_goverment_sp.setText2(entity.link_unit_name);
                    associate_goverment_sp.hidden(AddTaskActivity.this);
                    associate_goverment_sp.hideList();
                    connecter_tv.setText(entity.link_head_name);
                }
            });
            pool_country_sp.setTextSieze(14);
            pool_country_sp.setAdapter(countryAdapter);
            associate_goverment_sp.setTextSieze(14);
            associate_goverment_sp.setAdapter(govermentAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == add_task) {
            saveTask();
        } else if (v == complete_time_tv) {
            showDataPickerDialog();
        }
    }

    private void saveTask() {
        if (StringUtil.isEmpty(task_name_tv.getText().toString())) {
            showToast("请输入任务名称");
            return;
        }
        if (StringUtil.isEmpty(task_taget_tv.getText().toString())) {
            showToast("请输入任务目标");
            return;
        }
        if (StringUtil.isEmpty(complete_time_tv.getText().toString())) {
            showToast("请输入完成期限");
            return;
        }
        try {
            CountryEntity countryEntity = mCountryEntity;
            GovermentEntity govermentEntity = mGovermentEntity;
            SaveTaskRequest request = new SaveTaskRequest(configEntity.key, task_name_tv.getText().toString(), task_taget_tv.getText().toString(),
                    govermentEntity.link_unit_name, govermentEntity.link_unit_id, govermentEntity.link_head_name, countryEntity.link_village_name,
                    countryEntity.link_village_id, complete_time_tv.getText().toString());
            httpPostRequest(request.getRequestUrl(), request.getRequestParams(), SaveTaskRequest.SAVE_TASK_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDataPickerDialog() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        complete_time_tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
    }
}
