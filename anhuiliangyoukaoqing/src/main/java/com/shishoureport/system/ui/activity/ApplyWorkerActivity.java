package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.shishoureport.system.R;
import com.shishoureport.system.databinding.ActivityApplyWorkerBinding;
import com.shishoureport.system.entity.ApplyWorkerEntity;
import com.shishoureport.system.entity.BaseDataEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.SaveApplyWorkerRequest;
import com.shishoureport.system.request.SaveLeaveAppRequest;
import com.shishoureport.system.ui.adapter.AddPeopleAdapter;
import com.shishoureport.system.ui.fragment.ContactsFragment;
import com.shishoureport.system.ui.fragment.LeaveListFragment;
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
 */

public class ApplyWorkerActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MyDataPickerDialog.OnButtonClick, MyBaseEntityListDialog.OnListItemClick {
    public static int CONTACTS_REQUEST = 1001;
    public static int COPY_CONTACTS_REQUEST = 1201;
    private HorizontalListView mApproveListView, copy_listview;
    private View start_time_layout, end_time_layout, leave_type_layout, commit_btn;
    private TextView start_time_tv, end_time_tv;
    private AddPeopleAdapter addPeopleAdapter, copyPeopleAdapter;
    private ApplyWorkerEntity mApplyWorkerEntity = new ApplyWorkerEntity();
    private ActivityApplyWorkerBinding mBinding;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ApplyWorkerActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return -1;
    }

    public void initView() {
        mBinding = ActivityApplyWorkerBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mApproveListView = findViewById(R.id.approve_listview);
        addPeopleAdapter = new AddPeopleAdapter(this, getData());
        mApproveListView.setAdapter(addPeopleAdapter);
        copy_listview = findViewById(R.id.copy_listview);
        copyPeopleAdapter = new AddPeopleAdapter(this, getmCopyUsers());
        copy_listview.setAdapter(copyPeopleAdapter);
        copy_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (position == (mCopyUsers.size() - 1)) {
                        MyListActivity.startActivityForResult(ApplyWorkerActivity.this, MyListActivity.TYPE_CONTACTS_LIST, copyPeopleAdapter.getIds(), COPY_CONTACTS_REQUEST);
                    } else {
                        mCopyUsers.remove(position);
                        copyPeopleAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                }
            }
        });
        mApproveListView.setOnItemClickListener(this);
        leave_type_layout = findViewById(R.id.leave_type_layout);
        start_time_layout = findViewById(R.id.start_time_layout);
        end_time_layout = findViewById(R.id.end_time_layout);
        start_time_tv = (TextView) findViewById(R.id.start_time_tv);
        end_time_tv = (TextView) findViewById(R.id.end_time_tv);
        leave_type_layout.setOnClickListener(this);
        start_time_layout.setOnClickListener(this);
        end_time_layout.setOnClickListener(this);
        commit_btn = findViewById(R.id.commit_btn);
        commit_btn.setOnClickListener(this);
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("临时借用人员");
    }

    List<User> mData = new ArrayList<>();

    private List<User> getData() {
        mData = new ArrayList<>();
        User p = new User();
        mData.add(p);
        return mData;
    }

    List<User> mCopyUsers = new ArrayList<>();

    private List<User> getmCopyUsers() {
        mCopyUsers = new ArrayList<>();
        User p = new User();
        mCopyUsers.add(p);
        return mCopyUsers;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_time_layout:
                showDataDialog("开始时间", MyDataPickerDialog.TYPE_START_TIME);
                break;
            case R.id.end_time_layout:
                showDataDialog("结束时间", MyDataPickerDialog.TYPE_END_TIME);
                break;
//            case R.id.record_tv:
//                MyListActivity.startActivity(this, LeaveListFragment.TYPE_LEAVE_APP_LIST);
//                break;
            case R.id.commit_btn:
                commitData();
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
            mApplyWorkerEntity.start_time = date;
            start_time_tv.setText(date);
        } else if (type == MyDataPickerDialog.TYPE_END_TIME) {
            mApplyWorkerEntity.end_time = date;
            end_time_tv.setText(date);
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
            } else if (requestCode == COPY_CONTACTS_REQUEST) {
                try {
                    User user = (User) data.getSerializableExtra(ContactsFragment.KEY_CONTACTS);
                    if (mCopyUsers.size() >= 1) {
                        mCopyUsers.add(mCopyUsers.size() - 1, user);
                    } else {
                        mCopyUsers.add(user);
                    }
                    copyPeopleAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void commitData() {
        if (TextUtils.isEmpty(start_time_tv.getText().toString())) {
            showToast("请选择开始时间");
            return;
        }
        mApplyWorkerEntity.start_time = start_time_tv.getText().toString() +
                " 00:00:00";
        if (TextUtils.isEmpty(end_time_tv.getText().toString())) {
            showToast("请选择结束时间");
            return;
        }
        mApplyWorkerEntity.end_time = end_time_tv.getText().toString() + " 00:00:00";
        if (TextUtils.isEmpty(mBinding.reasonTv.getText().toString())) {
            showToast("请输入事由");
            return;
        }
        mApplyWorkerEntity.reason = mBinding.reasonTv.getText().toString();
        if (TextUtils.isEmpty(mBinding.personNumTv.getText().toString())) {
            showToast("请输入预计人数");
            return;
        }
        mApplyWorkerEntity.num = mBinding.personNumTv.getText().toString();
        if (TextUtils.isEmpty(mBinding.personInfoTv.getText().toString())) {
            showToast("请输入借用人员具体情况");
            return;
        }
        mApplyWorkerEntity.detail = mBinding.personInfoTv.getText().toString();
        if (TextUtils.isEmpty(mBinding.workDetailTv.getText().toString())) {
            showToast("请输入工作内容");
            return;
        }

        mApplyWorkerEntity.content = mBinding.workDetailTv.getText().toString();
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
        mApplyWorkerEntity.audit_name = names.toString();
        mApplyWorkerEntity.audit_uid = ids.toString();

        if (mCopyUsers.size() > 1) {
            StringBuilder cc_user_names = new StringBuilder();
            StringBuilder cc_user_ids = new StringBuilder();
            for (int i = 0; i < mCopyUsers.size() - 1; i++) {
                User user = mCopyUsers.get(i);
                if (mCopyUsers.size() - 2 == i) {
                    cc_user_names.append(user.user_name);
                    cc_user_ids.append(user.id);
                } else {
                    cc_user_names.append(user.user_name + ",");
                    cc_user_ids.append(user.id + ",");
                }
            }
            mApplyWorkerEntity.cc_user_names = cc_user_names.toString();
            mApplyWorkerEntity.cc_user_ids = cc_user_ids.toString();
        }
        SaveApplyWorkerRequest request = new SaveApplyWorkerRequest(mApplyWorkerEntity, this);
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), SaveApplyWorkerRequest.SAVE_APPLY_WORKER_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case SaveApplyWorkerRequest.SAVE_APPLY_WORKER_REQUEST:
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
