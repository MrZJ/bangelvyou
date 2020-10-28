package com.fuping.system.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.entity.FieldErrors;
import com.fuping.system.entity.InstructionEntity;
import com.fuping.system.request.SaveReplyInstructionRequest;
import com.fuping.system.utils.Constant;
import com.fuping.system.utils.StringUtil;

import static com.fuping.system.ui.fragment.InstructionFragment.IS_REPLAY;

/**
 * Created by jianzhang.
 * on 2017/10/11.
 * copyright easybiz.
 */

public class InstructionDetailActivity extends BaseActivity implements View.OnClickListener {
    private InstructionEntity mEntity;
    private TextView time_tv, content_tv, reply_et, replyed_tv;
    private View commit_task_tv, replyed_layout;
    private int mType = -1;

    public static void startActivityForResult(Activity context, InstructionEntity entity, int requestcode) {
        Intent intent = new Intent(context, InstructionDetailActivity.class);
        intent.putExtra("data", entity);
        intent.putExtra("type", requestcode);
        context.startActivityForResult(intent, requestcode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_detail);
        mEntity = (InstructionEntity) getIntent().getSerializableExtra("data");
        mType = getIntent().getIntExtra("type", -1);
        if (mEntity == null) {
            finish();
            return;
        }
        initTopView();
        initListener();
        setLeftBackButton();
        initSideBarListener();
        setTitle("历史批示");
        initView();
    }

    private void initView() {
        time_tv = (TextView) findViewById(R.id.time_tv);
        content_tv = (TextView) findViewById(R.id.content_tv);
        reply_et = (TextView) findViewById(R.id.reply_et);
        commit_task_tv = findViewById(R.id.commit_task_tv);
        time_tv.setText(mEntity.instructions_date);
        content_tv.setText(mEntity.instructions_content);
        commit_task_tv.setOnClickListener(this);
        replyed_tv = (TextView) findViewById(R.id.replyed_tv);
        replyed_layout = findViewById(R.id.replyed_layout);
        if (mType == IS_REPLAY) {
            replyed_layout.setVisibility(View.VISIBLE);
            replyed_tv.setText(mEntity.replay_content);
        } else {
            replyed_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (commit_task_tv == v) {
            if (Constant.getReplyInstruction(configEntity.usertype)) {
                commitReply();
            } else {
                showToast("您没有权限回复");
            }
        }
    }

    private void commitReply() {
        if (StringUtil.isEmpty(reply_et.getText().toString())) {
            showToast("请填写回复内容");
            return;
        }
        SaveReplyInstructionRequest replyInstructionRequest = new SaveReplyInstructionRequest(configEntity.key, mEntity.instructions_id,
                reply_et.getText().toString());
        httpPostRequest(replyInstructionRequest.getRequestUrl(), replyInstructionRequest.getRequestParams(), SaveReplyInstructionRequest.SAVE_INS_REP_INS_REQ);
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
            case SaveReplyInstructionRequest.SAVE_INS_REP_INS_REQ:
                showToast("提交成功");
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}