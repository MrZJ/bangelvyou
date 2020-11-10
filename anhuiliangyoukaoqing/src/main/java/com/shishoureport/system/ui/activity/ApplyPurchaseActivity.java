package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.shishoureport.system.R;
import com.shishoureport.system.databinding.ActivityApplyPurchaseBinding;
import com.shishoureport.system.entity.ApplyPurchaseEntity;
import com.shishoureport.system.entity.BaseDataEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.MedicalEntity;
import com.shishoureport.system.entity.MedicalListActivity;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.SaveApplyPurchaseRequest;
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

import static com.shishoureport.system.ui.activity.ApplyUseActivity.MEDICAL_REQUEST;

/**
 * Created by jianzhang.
 * on 2017/9/4.
 * copyright easybiz.
 * 采购申请
 */

public class ApplyPurchaseActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MyDataPickerDialog.OnButtonClick, MyBaseEntityListDialog.OnListItemClick {
    public static int CONTACTS_REQUEST = 1001;
    public static int CONTACTS_COPY_REQUEST = 1201;
    private ApplyPurchaseEntity mApplyPurchaseEntity = new ApplyPurchaseEntity();
    private HorizontalListView mApproveListView;
    private TextView receiverName;
    private AddPeopleAdapter addPeopleAdapter;
    private ActivityApplyPurchaseBinding mBinding;
    private ProductAdapter mProductAdapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ApplyPurchaseActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return -1;
    }

    @Override
    public void initView() {
        mBinding = ActivityApplyPurchaseBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        User user = MySharepreference.getInstance(this).getUser();
        mBinding.personTv.setText(user.real_name);
        mProductAdapter = new ProductAdapter(this, false);
        mBinding.myProductList.setAdapter(mProductAdapter);
        mApproveListView = findViewById(R.id.approve_listview);
        addPeopleAdapter = new AddPeopleAdapter(this, getData());
        mApproveListView.setAdapter(addPeopleAdapter);
        receiverName = findViewById(R.id.receiverName);
        mApproveListView.setOnItemClickListener(this);
        mBinding.receiveTimeLayout.setOnClickListener(this);
        findViewById(R.id.receiverLayout).setOnClickListener(this);
        findViewById(R.id.commit_btn).setOnClickListener(this);
        mBinding.addProductTv.setOnClickListener(this);
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
            case R.id.receiveTimeLayout:
                showDataDialog("接收时间", MyDataPickerDialog.TYPE_START_TIME);
                break;
//            case R.id.record_tv:
//                MyListActivity.startActivity(this, LeaveListFragment.TYPE_LEAVE_APP_LIST);
//                break;
            case R.id.commit_btn:
                commitData();
                break;
            case R.id.receiverLayout:
                MyListActivity.startActivityForResult(this, MyListActivity.TYPE_CONTACTS_LIST, addPeopleAdapter.getIds(), CONTACTS_COPY_REQUEST);
                break;
            case R.id.addProductTv:
                MedicalListActivity.start(this, false, MEDICAL_REQUEST);
                break;
        }
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


    private void showDataDialog(String title, int type) {
        MyDataPickerDialog dataPickerDialog = new MyDataPickerDialog(MyDataPickerDialog.YEAR_MONTH, this, title, this, type);
        dataPickerDialog.show();
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
            } else if (requestCode == CONTACTS_COPY_REQUEST) {
                try {
                    User user = (User) data.getSerializableExtra(ContactsFragment.KEY_CONTACTS);
                    receiverName.setText(user.real_name);
                    mApplyPurchaseEntity.reciveId = user.id;
                    mApplyPurchaseEntity.reciveName = user.real_name;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == MEDICAL_REQUEST) {
                MedicalEntity entity = (MedicalEntity) data.getSerializableExtra(MedicalListFragment.KEY_MEDICAL);
                mProductAdapter.add(entity);
            }
        }
    }

    private void commitData() {
        if (TextUtils.isEmpty(mBinding.batchEt.getText().toString())) {
            showToast("请输入批次");
            return;
        }
        mApplyPurchaseEntity.batch = mBinding.batchEt.getText().toString();
        if (TextUtils.isEmpty(mBinding.applyPlaceTv.getText().toString())) {
            showToast("请输入采购地点");
            return;
        }
        mApplyPurchaseEntity.place = mBinding.applyPlaceTv.getText().toString();
        if (TextUtils.isEmpty(mBinding.receiverName.getText().toString())) {
            showToast("请选择接收人");
            return;
        }
        if (TextUtils.isEmpty(mBinding.receiveTimeTv.getText().toString())) {
            showToast("请选接收时间");
            return;
        }
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
        mApplyPurchaseEntity.audit_name = names.toString();
        mApplyPurchaseEntity.audit_uid = ids.toString();
        if (mProductAdapter.isEmpty()) {
            showToast("请添加要申请的物品");
            return;
        }
        List<MedicalEntity> entities = mProductAdapter.getData();
        StringBuilder reagentIds = new StringBuilder();
        StringBuilder quantitys = new StringBuilder();
        StringBuilder remarks = new StringBuilder();
        for (int i = 0; i < entities.size(); i++) {
            MedicalEntity entity = entities.get(i);
            if (entities.size() - 1 == i) {
                reagentIds.append(entity.id);
                if (TextUtils.isEmpty(entity.inputQuantity)) {
                    showToast("请输入采购数量");
                    return;
                }
                quantitys.append(entity.inputQuantity);
                if (TextUtils.isEmpty(entity.inputRemark)) {
                    entity.inputRemark = "无";
                }
                remarks.append(entity.inputRemark);
            } else {
                reagentIds.append(entity.id + ",");
                if (TextUtils.isEmpty(entity.inputQuantity)) {
                    showToast("请输入采购数量");
                    return;
                }
                quantitys.append(entity.inputQuantity + ",");
                if (TextUtils.isEmpty(entity.inputRemark)) {
                    entity.inputRemark = "无";
                }
                remarks.append(entity.inputRemark + ",");
            }
        }
        mApplyPurchaseEntity.reagentIds = reagentIds.toString();
        mApplyPurchaseEntity.quantitys = quantitys.toString();
        mApplyPurchaseEntity.remarks = remarks.toString();
        mApplyPurchaseEntity.remark = mBinding.remarkEt.getText().toString();
        SaveApplyPurchaseRequest request = new SaveApplyPurchaseRequest(mApplyPurchaseEntity, this);
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), SaveApplyPurchaseRequest.SAVE_APPLY_PURCHASE_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case SaveApplyPurchaseRequest.SAVE_APPLY_PURCHASE_REQUEST:
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
}
