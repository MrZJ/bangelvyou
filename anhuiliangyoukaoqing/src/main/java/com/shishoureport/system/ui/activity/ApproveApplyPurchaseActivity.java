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
import com.shishoureport.system.databinding.ActivityApplyPurchaseBinding;
import com.shishoureport.system.databinding.ActivityApproveApplyPurchaseBinding;
import com.shishoureport.system.entity.ApplyPurchaseEntity;
import com.shishoureport.system.entity.BaseDataEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.MedicalEntity;
import com.shishoureport.system.entity.MedicalListActivity;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.ApproveApplyPurchaseRequest;
import com.shishoureport.system.request.SaveApplyPurchaseRequest;
import com.shishoureport.system.ui.adapter.AddPeopleAdapter;
import com.shishoureport.system.ui.adapter.ProductAdapter;
import com.shishoureport.system.ui.fragment.ContactsFragment;
import com.shishoureport.system.ui.fragment.MedicalListFragment;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.TimeDateUtil;
import com.shishoureport.system.wibget.HorizontalListView;
import com.shishoureport.system.wibget.MyBaseEntityListDialog;
import com.shishoureport.system.wibget.MyDataPickerDialog;

import java.util.ArrayList;
import java.util.List;

import static com.shishoureport.system.ui.activity.ApplyUseActivity.MEDICAL_REQUEST;

/**
 * Created by jianzhang.
 * on 2017/9/4.
 * copyright easybiz.
 * 采购申请
 */

public class ApproveApplyPurchaseActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MyDataPickerDialog.OnButtonClick, MyBaseEntityListDialog.OnListItemClick {
    private ApplyPurchaseEntity mApplyPurchaseEntity = new ApplyPurchaseEntity();
    private ActivityApproveApplyPurchaseBinding mBinding;
    private ProductAdapter mProductAdapter;

    public static void startActivity(Fragment fragment, ApplyPurchaseEntity entity) {
        Intent intent = new Intent(fragment.getContext(), ApproveApplyPurchaseActivity.class);
        intent.putExtra("entity", entity);
        fragment.startActivityForResult(intent, 100);
    }


    @Override
    public int getLayoutId() {
        return -1;
    }

    @Override
    public void initView() {
        mBinding = ActivityApproveApplyPurchaseBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        User user = MySharepreference.getInstance(this).getUser();
        mApplyPurchaseEntity = (ApplyPurchaseEntity) getIntent().getSerializableExtra("entity");
        mBinding.personTv.setText(user.real_name);
        mProductAdapter = new ProductAdapter(this, false);
        mBinding.myProductList.setAdapter(mProductAdapter);
        mBinding.receiveTimeLayout.setOnClickListener(this);
        findViewById(R.id.receiverLayout).setOnClickListener(this);
        mBinding.batchEt.setText(mApplyPurchaseEntity.batch);
        mBinding.applyPlaceTv.setText(mApplyPurchaseEntity.place);
        mBinding.personTv.setText(mApplyPurchaseEntity.applyName);
        mBinding.receiverName.setText(mApplyPurchaseEntity.reciveName);
        mBinding.receiveTimeTv.setText(TimeDateUtil.dateTime2(mApplyPurchaseEntity.reciveDate));
        mBinding.remarkEt.setText(mApplyPurchaseEntity.remark);
        mBinding.titleLayout.titleTv.setText("采购申请");
        mBinding.passBtn.setOnClickListener(this);
        mBinding.rejectBtn.setOnClickListener(this);
    }


    List<User> mData = new ArrayList<>();

    private List<User> getData() {
        mData = new ArrayList<>();
        User p = new User();
        mData.add(p);
        return mData;
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
        if (type == MyDataPickerDialog.TYPE_START_TIME) {
            mApplyPurchaseEntity.reciveDate = date;
            mBinding.receiveTimeTv.setText(date);
        }
    }

    @Override
    public void onCancelClick(int type, int pos) {

    }

    @Override
    public void onItemclick(BaseDataEntity entity) {
    }


    private void pass() {
        ApproveApplyPurchaseRequest request = new ApproveApplyPurchaseRequest(this, "1", mBinding.auditSuggestionEt.getText().toString(), mApplyPurchaseEntity.id);
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), ApproveApplyPurchaseRequest.APPROVE_APPLY_PURCHASE_REQUEST);
    }

    private void reject() {
        ApproveApplyPurchaseRequest request = new ApproveApplyPurchaseRequest(this, "0", mBinding.auditSuggestionEt.getText().toString(), mApplyPurchaseEntity.id);
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), ApproveApplyPurchaseRequest.APPROVE_APPLY_PURCHASE_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case ApproveApplyPurchaseRequest.APPROVE_APPLY_PURCHASE_REQUEST:
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
