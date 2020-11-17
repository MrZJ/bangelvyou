package com.shishoureport.system.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.shishoureport.system.R;
import com.shishoureport.system.databinding.ActivityApplyUseBinding;
import com.shishoureport.system.databinding.ActivityApproveApplyUseBinding;
import com.shishoureport.system.entity.ApplyUseEntity;
import com.shishoureport.system.entity.BaseDataEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.MedicalEntity;
import com.shishoureport.system.entity.MedicalListActivity;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.ApproveApplyPurchaseRequest;
import com.shishoureport.system.request.ApproveApplyUseRequest;
import com.shishoureport.system.request.SaveApplyUseRequest;
import com.shishoureport.system.ui.adapter.AddPeopleAdapter;
import com.shishoureport.system.ui.adapter.ProductAdapter;
import com.shishoureport.system.ui.fragment.ContactsFragment;
import com.shishoureport.system.ui.fragment.MedicalListFragment;
import com.shishoureport.system.wibget.HorizontalListView;
import com.shishoureport.system.wibget.MyBaseEntityListDialog;
import com.shishoureport.system.wibget.MyDataPickerDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/4.
 * copyright easybiz.
 * 申请领用
 */

public class ApproveApplyUseActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MyDataPickerDialog.OnButtonClick, MyBaseEntityListDialog.OnListItemClick {
    private ApplyUseEntity mApplyUseEntity = new ApplyUseEntity();
    private ProductAdapter mProductAdapter;
    private ActivityApproveApplyUseBinding mBinding;

    public static void startActivity(Fragment fragment, ApplyUseEntity applyUseEntity) {
        Intent intent = new Intent(fragment.getContext(), ApproveApplyUseActivity.class);
        intent.putExtra("entity", applyUseEntity);
        fragment.startActivityForResult(intent, 121);
    }


    @Override
    public int getLayoutId() {
        return -1;
    }

    @Override
    public void initView() {
        mBinding = ActivityApproveApplyUseBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mProductAdapter = new ProductAdapter(this, true);
        mApplyUseEntity = (ApplyUseEntity) getIntent().getSerializableExtra("entity");
        mBinding.myProductList.setAdapter(mProductAdapter);
        mBinding.passBtn.setOnClickListener(this);
        mBinding.rejectBtn.setOnClickListener(this);
        mBinding.titleLayout.titleTv.setText("领用审核");
        mBinding.batchTV.setText(mApplyUseEntity.batch);
        mBinding.remarkEt.setText(mApplyUseEntity.remark);
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

    private void pass() {
        ApproveApplyUseRequest request = new ApproveApplyUseRequest(this, "1", mBinding.auditSuggestionEt.getText().toString(), mApplyUseEntity.id);
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), ApproveApplyUseRequest.APPROVE_APPLY_USE_REQUEST);
    }

    private void reject() {
        ApproveApplyUseRequest request = new ApproveApplyUseRequest(this, "0", mBinding.auditSuggestionEt.getText().toString(), mApplyUseEntity.id);
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), ApproveApplyUseRequest.APPROVE_APPLY_USE_REQUEST);
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


    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case ApproveApplyUseRequest.APPROVE_APPLY_USE_REQUEST:
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}
