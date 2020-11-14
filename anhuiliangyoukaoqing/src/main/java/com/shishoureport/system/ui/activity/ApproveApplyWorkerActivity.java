package com.shishoureport.system.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.databinding.ActivityApplyWorkerBinding;
import com.shishoureport.system.databinding.ActivityApproveApplyWorkerBinding;
import com.shishoureport.system.entity.ApplyWorkerEntity;
import com.shishoureport.system.entity.BaseDataEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.ApproveApplyPurchaseRequest;
import com.shishoureport.system.request.ApproveApplyWorkerRequest;
import com.shishoureport.system.request.SaveApplyWorkerRequest;
import com.shishoureport.system.ui.adapter.AddPeopleAdapter;
import com.shishoureport.system.ui.fragment.ContactsFragment;
import com.shishoureport.system.utils.TimeDateUtil;
import com.shishoureport.system.wibget.HorizontalListView;
import com.shishoureport.system.wibget.MyBaseEntityListDialog;
import com.shishoureport.system.wibget.MyDataPickerDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/4.
 * copyright easybiz.
 */

public class ApproveApplyWorkerActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MyDataPickerDialog.OnButtonClick, MyBaseEntityListDialog.OnListItemClick {
    private ApplyWorkerEntity mApplyWorkerEntity = new ApplyWorkerEntity();
    private ActivityApproveApplyWorkerBinding mBinding;

    public static void startActivity(Fragment fragment, ApplyWorkerEntity entity) {
        Intent intent = new Intent(fragment.getContext(), ApproveApplyWorkerActivity.class);
        intent.putExtra("entity", entity);
        fragment.startActivityForResult(intent, 121);
    }


    @Override
    public int getLayoutId() {
        return -1;
    }

    public void initView() {
        mBinding = ActivityApproveApplyWorkerBinding.inflate(getLayoutInflater());
        mApplyWorkerEntity = (ApplyWorkerEntity) getIntent().getSerializableExtra("entity");
        setContentView(mBinding.getRoot());
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("临时借用人员审核");
        mBinding.startTimeTv.setText(TimeDateUtil.dateTime2(mApplyWorkerEntity.start_time));
        mBinding.endTimeTv.setText(TimeDateUtil.dateTime2(mApplyWorkerEntity.end_time));
        mBinding.reasonTv.setText(mApplyWorkerEntity.reason);
        mBinding.personNumTv.setText(mApplyWorkerEntity.num);
        mBinding.personInfoTv.setText(mApplyWorkerEntity.detail);
        mBinding.workDetailTv.setText(mApplyWorkerEntity.content);
        mBinding.remarkEt.setText(mApplyWorkerEntity.rmark);
        mBinding.passBtn.setOnClickListener(this);
        mBinding.rejectBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pass_btn:
                pass();
                break;
            case R.id.reject_btn:
                reject();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }


    @Override
    public void onOkClick(String date, int type, int pos) {
    }

    @Override
    public void onCancelClick(int type, int pos) {

    }

    @Override
    public void onItemclick(BaseDataEntity entity) {
    }


    private void pass() {
        ApproveApplyWorkerRequest request = new ApproveApplyWorkerRequest(this, "1", mBinding.auditSuggestionEt.getText().toString(), mApplyWorkerEntity.id);
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), ApproveApplyWorkerRequest.APPROVE_APPLY_WORKER_REQUEST);
    }

    private void reject() {
        ApproveApplyWorkerRequest request = new ApproveApplyWorkerRequest(this, "0", mBinding.auditSuggestionEt.getText().toString(), mApplyWorkerEntity.id);
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), ApproveApplyWorkerRequest.APPROVE_APPLY_WORKER_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case ApproveApplyWorkerRequest.APPROVE_APPLY_WORKER_REQUEST:
                handleRequest(json);
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        showToast("提交失败，请重试");
    }

    private void handleRequest(String json) {
        showToast("提交成功");
        setResult(Activity.RESULT_OK);
        finish();
    }
}
