package com.basulvyou.system.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.basulvyou.system.R;
import com.basulvyou.system.api.CarpoolingApi;
import com.basulvyou.system.entity.EvaluateDetailEntity;

/**
 * Created by jianzhang.
 * on 2017/6/8.
 * copyright easybiz.
 * 拼车评价界面
 */

public class CarpoolingEvaluateActivity extends BaseActivity implements View.OnClickListener {
    public static final String KEY_OBJ = "obj";
    public static final String ORDER_ID = "order_id";
    private View img_top_goback, commit_btn;
    private RadioButton niming, haoping, zhongping, chaping;
    private EditText evaluate_et;
    private String order_id;
    private String resType;
    private EvaluateDetailEntity evaluateDetailEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpooling_evaluate);
        evaluateDetailEntity = (EvaluateDetailEntity) getIntent().getSerializableExtra(KEY_OBJ);
        order_id = getIntent().getStringExtra(ORDER_ID);
        resType = getIntent().getStringExtra("resType");
        if (evaluateDetailEntity == null || evaluateDetailEntity.comm_info == null) {
            finish();
            return;
        }
        img_top_goback = findViewById(R.id.img_top_goback);
        img_top_goback.setOnClickListener(this);
        niming = (RadioButton) findViewById(R.id.niming);
        haoping = (RadioButton) findViewById(R.id.haoping);
        zhongping = (RadioButton) findViewById(R.id.zhongping);
        chaping = (RadioButton) findViewById(R.id.chaping);
        niming.setOnClickListener(this);
        commit_btn = findViewById(R.id.commit_btn);
        commit_btn.setOnClickListener(this);
        evaluate_et = (EditText) findViewById(R.id.evaluate_et);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_top_goback:
                finish();
                break;
            case R.id.niming:
                if (niming.isSelected()) {
                    niming.setSelected(false);
                    niming.setChecked(false);
                } else {
                    niming.setSelected(true);
                }
                break;
            case R.id.commit_btn:
                if (evaluate_et.getText() == null || evaluate_et.getText().toString().equals("")) {
                    Toast.makeText(this, "评价不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    commitData();
                }
                break;
        }
    }

    private void commitData() {
        String comm_id = null, comm_score;
        if (evaluateDetailEntity != null) {
            comm_id = evaluateDetailEntity.comm_info.comm_id;
        }
        if (haoping.isChecked()) {
            comm_score = "5";
        } else if (zhongping.isChecked()) {
            comm_score = "3";
        } else {
            comm_score = "1";
        }
        httpGetRequest(CarpoolingApi.getCommitEvaluate(comm_id, configEntity.key, evaluate_et.getText().toString(), comm_score, order_id, resType), CarpoolingApi.API_CAPOOLING_ORDER_COMM_EVALUATE);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case CarpoolingApi.API_CAPOOLING_ORDER_COMM_EVALUATE:
                Toast.makeText(this, "提交成功！", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
