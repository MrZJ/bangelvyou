package com.fuping.system.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.entity.FieldErrors;
import com.fuping.system.request.ReplyTaskProcessRequset;
import com.fuping.system.utils.StringUtil;

/**
 * Created by jianzhang.
 * on 2017/10/11.
 * copyright easybiz.
 */

public class ReplyProcessActivity extends BaseActivity implements View.OnClickListener {
    private String villageTask_id, process;
    private TextView reply_process_et, reply_et;
    private View commit_task_tv;

    public static void startActivityForResult(Activity context, String villageTask_id, String process, int requestcode) {
        Intent intent = new Intent(context, ReplyProcessActivity.class);
        intent.putExtra("villageTask_id", villageTask_id);
        intent.putExtra("process", process);
        context.startActivityForResult(intent, requestcode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_process);
        villageTask_id = getIntent().getStringExtra("villageTask_id");
        process = getIntent().getStringExtra("process");
        if (villageTask_id == null || process == null) {
            finish();
            return;
        }
        initTopView();
        initListener();
        setLeftBackButton();
        initSideBarListener();
        setTitle("上报进度");
        initView();
    }

    private void initView() {
        commit_task_tv = findViewById(R.id.commit_task_tv);
        commit_task_tv.setOnClickListener(this);
        reply_process_et = (TextView) findViewById(R.id.reply_process_et);
        reply_et = (TextView) findViewById(R.id.reply_et);
    }

    @Override
    public void onClick(View v) {
        if (commit_task_tv == v) {
            commitInstruction();
        }
    }

    private void commitInstruction() {
        if (StringUtil.isEmpty(reply_process_et.getText().toString())) {
            showToast("请填本次进度");
            return;
        }
        try {
            if (Integer.valueOf(reply_process_et.getText().toString()) <= Integer.valueOf(process)) {
                showToast("填写进度应大于当前进度" + process + "%");
                return;
            }
            if (Integer.valueOf(reply_process_et.getText().toString()) > 100) {
                showToast("填写进度应小于100");
                return;
            }
        } catch (Exception e) {
            return;
        }
        if (StringUtil.isEmpty(reply_et.getText().toString())) {
            showToast("请填写说明");
            return;
        }
        ReplyTaskProcessRequset requset = new ReplyTaskProcessRequset(configEntity.key, villageTask_id,
                reply_process_et.getText().toString(), reply_et.getText().toString());
        httpPostRequest(requset.getRequestUrl(), requset.getRequestParams(), ReplyTaskProcessRequset.REPLY_TASK_PROCESS_REQUSET);
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        showToast("提交失败");
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case ReplyTaskProcessRequset.REPLY_TASK_PROCESS_REQUSET:
                showToast("提交成功");
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}
