package com.fuping.system.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.entity.FieldErrors;
import com.fuping.system.request.AddInstructionRequset;
import com.fuping.system.utils.StringUtil;

/**
 * Created by jianzhang.
 * on 2017/10/11.
 * copyright easybiz.
 */

public class AddInstructionActivity extends BaseActivity implements View.OnClickListener {
    private String villageTask_id;
    private TextView reply_et;
    private View commit_task_tv;

    public static void startActivityForResult(Activity context, String villageTask_id, int requestcode) {
        Intent intent = new Intent(context, AddInstructionActivity.class);
        intent.putExtra("villageTask_id", villageTask_id);
        context.startActivityForResult(intent, requestcode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instruction);
        villageTask_id = getIntent().getStringExtra("villageTask_id");
        if (villageTask_id == null) {
            finish();
            return;
        }
        initTopView();
        initListener();
        setLeftBackButton();
        initSideBarListener();
        setTitle("添加批示");
        initView();
    }

    private void initView() {
        reply_et = (TextView) findViewById(R.id.reply_et);
        commit_task_tv = findViewById(R.id.commit_task_tv);
        commit_task_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (commit_task_tv == v) {
            commitInstruction();
        }
    }

    private void commitInstruction() {
        if (StringUtil.isEmpty(reply_et.getText().toString())) {
            showToast("请填写批示内容");
            return;
        }
        AddInstructionRequset requset = new AddInstructionRequset(configEntity.key, villageTask_id, reply_et.getText().toString());
        httpPostRequest(requset.getRequestUrl(), requset.getRequestParams(), AddInstructionRequset.ADD_INS_REQUEST);
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
            case AddInstructionRequset.ADD_INS_REQUEST:
                showToast("提交成功");
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}