package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONArray;
import com.shishoureport.system.R;
import com.shishoureport.system.databinding.ActivityApplyUseBinding;
import com.shishoureport.system.entity.ApplyUseEntity;
import com.shishoureport.system.entity.BaseDataEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.MedicalEntity;
import com.shishoureport.system.entity.MedicalListActivity;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.SaveApplyUseRequest;
import com.shishoureport.system.request.SaveLeaveAppRequest;
import com.shishoureport.system.ui.adapter.AddPeopleAdapter;
import com.shishoureport.system.ui.adapter.ProductAdapter;
import com.shishoureport.system.ui.fragment.ContactsFragment;
import com.shishoureport.system.ui.fragment.LeaveListFragment;
import com.shishoureport.system.ui.fragment.MedicalListFragment;
import com.shishoureport.system.utils.MySharepreference;
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

public class ApplyUseActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MyDataPickerDialog.OnButtonClick, MyBaseEntityListDialog.OnListItemClick {
    public static int CONTACTS_REQUEST = 1001;
    //Test
    public static int MEDICAL_REQUEST = 213;
    private ApplyUseEntity mApplyUseEntity = new ApplyUseEntity();
    private HorizontalListView mApproveListView;
    private AddPeopleAdapter addPeopleAdapter;
    private ProductAdapter mProductAdapter;
    private ActivityApplyUseBinding mBinding;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ApplyUseActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return -1;
    }

    @Override
    public void initView() {
        mBinding = ActivityApplyUseBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mApproveListView = findViewById(R.id.approve_listview);
        addPeopleAdapter = new AddPeopleAdapter(this, getData());
        mApproveListView.setAdapter(addPeopleAdapter);
        mApproveListView.setOnItemClickListener(this);
        mProductAdapter = new ProductAdapter(this, true);
        mBinding.myProductList.setAdapter(mProductAdapter);
        findViewById(R.id.commit_btn).setOnClickListener(this);
        findViewById(R.id.useLayout).setOnClickListener(this);
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
            case R.id.record_tv:
//                MyListActivity.startActivity(this, LeaveListFragment.TYPE_LEAVE_APP_LIST);
                break;
            case R.id.commit_btn:
                commitData();
                break;
            case R.id.useLayout:
                MedicalListActivity.start(this, true, MEDICAL_REQUEST);
                break;
        }

    }


    @Override
    public void onOkClick(String date, int type, int pos) {
    }

    @Override
    public void onCancelClick(int type, int pos) {

    }

    @Override
    public void onItemclick(BaseDataEntity entity) {
//        leave_type_tv.setText(mBaseDataEntity.type_name);
    }


    private void commitData() {
        if (TextUtils.isEmpty(mBinding.batchTV.getText().toString())) {
            showToast("请输入批次");
            return;
        }
        mApplyUseEntity.batch = mBinding.batchTV.getText().toString();
        if (mData.size() == 1) {
            showToast("请选择审批人");
            return;
        }
        StringBuilder names = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < mData.size() - 1; i++) {
            User user = mData.get(i);
            if (mData.size() - 2 == i) {
                names.append(user.user_name);
                ids.append(user.id);
            } else {
                names.append(user.user_name + ",");
                ids.append(user.id + ",");
            }
        }
        mApplyUseEntity.audit_name = names.toString();
        mApplyUseEntity.audit_uid = ids.toString();

        List<MedicalEntity> entities = mProductAdapter.getData();
        StringBuilder reagentIds = new StringBuilder();
        StringBuilder quantitys = new StringBuilder();
        StringBuilder remarks = new StringBuilder();
        for (int i = 0; i < entities.size(); i++) {
            MedicalEntity entity = entities.get(i);
            if (entities.size() - 1 == i) {
                reagentIds.append(entity.reagentId);
                if (TextUtils.isEmpty(entity.inputQuantity)) {
                    showToast("请输入领用数量");
                    return;
                }
                quantitys.append(entity.inputQuantity);
                if (TextUtils.isEmpty(entity.inputRemark)) {
                    entity.inputRemark = "无";
                }
                remarks.append(entity.inputRemark);
            } else {
                reagentIds.append(entity.reagentId + ",");
                if (TextUtils.isEmpty(entity.inputQuantity)) {
                    showToast("请输入领用数量");
                    return;
                }
                quantitys.append(entity.inputQuantity + ",");
                if (TextUtils.isEmpty(entity.inputRemark)) {
                    entity.inputRemark = "无";
                }
                remarks.append(entity.inputRemark + ",");
            }
        }
        mApplyUseEntity.reagentIds = reagentIds.toString();
        mApplyUseEntity.quantitys = quantitys.toString();
        mApplyUseEntity.remarks = remarks.toString();
        mApplyUseEntity.remark = mBinding.remarkEt.getText().toString();

        SaveApplyUseRequest request = new SaveApplyUseRequest(mApplyUseEntity, this);
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), SaveApplyUseRequest.SAVE_APPLY_USE_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case SaveApplyUseRequest.SAVE_APPLY_USE_REQUEST:
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
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            if (position == (mData.size() - 1)) {
                MyListActivity.startActivityForResult(this, MyListActivity.TYPE_CONTACTS_LIST, addPeopleAdapter.getIds(), CONTACTS_REQUEST);
            } else {
                mData.remove(position);
                addPeopleAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CONTACTS_REQUEST) {
                try {
                    User user = (User) data.getSerializableExtra(ContactsFragment.KEY_CONTACTS);
                    if (mData.size() >= 1) {
                        mData.add(mData.size() - 1, user);
                    } else {
                        mData.add(user);
                    }
                    addPeopleAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == MEDICAL_REQUEST) {
                MedicalEntity entity = (MedicalEntity) data.getSerializableExtra(MedicalListFragment.KEY_MEDICAL);
                mProductAdapter.add(entity);
            }
        }
    }
}
