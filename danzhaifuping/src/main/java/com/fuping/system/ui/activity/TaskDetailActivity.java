package com.fuping.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fuping.system.R;
import com.fuping.system.entity.HelpDetailEntity;
import com.fuping.system.entity.HelpDetailParentEntity;
import com.fuping.system.request.HelpDetailRequest;
import com.fuping.system.utils.Constant;
import com.fuping.system.utils.StringUtil;
import com.fuping.system.utils.TimeDateUtil;


/**
 * Created by jianzhang.
 * on 2017/10/9.
 * copyright easybiz.
 */

public class TaskDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final int ADD_INS_REQUEST_CODE = 20;
    private View history_layout, process_layout, action_layout;
    private TextView task_name_tv, task_taget_tv, complete_time_tv, associate_goverment_tv,
            connecter_tv, task_process_tv, count_tv, add_task;
    private String mId;
    private HelpDetailEntity mHelpDetailEntity;


    public static void startActivity(Context context, String id) {
        Intent intent = new Intent(context, TaskDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        mId = getIntent().getStringExtra("id");
        initTopView();
        initListener();
        setLeftBackButton();
        initSideBarListener();
        setTitle("任务详情");
        initView();
    }

    private void initView() {
        add_task = (TextView) findViewById(R.id.add_task);
        add_task.setOnClickListener(this);
        history_layout = findViewById(R.id.history_layout);
        history_layout.setOnClickListener(this);
        if (Constant.getInstractionPermission(configEntity.usertype)) {
            history_layout.setVisibility(View.VISIBLE);
        } else {
            history_layout.setVisibility(View.GONE);
        }
        if (Constant.getAddInstractionPermission(configEntity.usertype)) {
            add_task.setText("添加批示");
            add_task.setVisibility(View.VISIBLE);
        } else {
            if (Constant.getReplyTaskProcess(configEntity.usertype)) {
                add_task.setText("上报进度");
                add_task.setVisibility(View.VISIBLE);
            } else {
                add_task.setVisibility(View.GONE);
            }
        }
        process_layout = findViewById(R.id.process_layout);
        action_layout = findViewById(R.id.action_layout);
        task_name_tv = (TextView) findViewById(R.id.task_name_tv);
        task_taget_tv = (TextView) findViewById(R.id.task_taget_tv);
        complete_time_tv = (TextView) findViewById(R.id.complete_time_tv);
        count_tv = (TextView) findViewById(R.id.count_tv);
        associate_goverment_tv = (TextView) findViewById(R.id.associate_goverment_tv);
        connecter_tv = (TextView) findViewById(R.id.connecter_tv);
        task_process_tv = (TextView) findViewById(R.id.task_process_tv);
        if (StringUtil.isEmpty(mId)) {
            finish();
            return;
        }
        loadData();
    }

    private void loadData() {
        HelpDetailRequest request = new HelpDetailRequest(mId, configEntity.key);
        httpGetRequest(request.getRequestUrl(), HelpDetailRequest.HELP_DETAIL_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case HelpDetailRequest.HELP_DETAIL_REQUEST:
                handRequest(json);
                break;
        }
    }

    private void handRequest(String json) {
        try {
            HelpDetailParentEntity parentEntity = JSONObject.parseObject(json, HelpDetailParentEntity.class);
            if (parentEntity != null && parentEntity.villageTask_each != null) {
                mHelpDetailEntity = parentEntity.villageTask_each;
                task_name_tv.setText(parentEntity.villageTask_each.task_name);
                task_taget_tv.setText(parentEntity.villageTask_each.poor_target);
                if (!StringUtil.isEmpty(parentEntity.villageTask_each.complete_date)) {
                    complete_time_tv.setText(TimeDateUtil.dateTime(Long.valueOf(parentEntity.villageTask_each.complete_date)));
                }
                associate_goverment_tv.setText(parentEntity.villageTask_each.link_unit_name);
                connecter_tv.setText(parentEntity.villageTask_each.link_head_name);
                task_process_tv.setText(parentEntity.villageTask_each.complete_progess + "%");
                count_tv.setText("" + parentEntity.villageTask_each.count);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        if (history_layout == v) {
            if (mHelpDetailEntity != null) {
                InstructionActivity.startActivity(this, mHelpDetailEntity.villageTask_id);
            }
        } else if (add_task == v) {
            if (mHelpDetailEntity != null) {
                if (Constant.getAddInstractionPermission(configEntity.usertype)) {
                    AddInstructionActivity.startActivityForResult(this, mHelpDetailEntity.villageTask_id, ADD_INS_REQUEST_CODE);
                } else {
                    ReplyProcessActivity.startActivityForResult(this, mHelpDetailEntity.villageTask_id, mHelpDetailEntity.complete_progess, ADD_INS_REQUEST_CODE);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (ADD_INS_REQUEST_CODE == requestCode) {
                loadData();
            }
        }
    }
}
