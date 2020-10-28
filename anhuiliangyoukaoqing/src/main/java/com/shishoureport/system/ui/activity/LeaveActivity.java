package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.BaseDataEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.LeaveAppEntity;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.SaveLeaveAppRequest;
import com.shishoureport.system.ui.adapter.AddPeopleAdapter;
import com.shishoureport.system.ui.fragment.ContactsFragment;
import com.shishoureport.system.ui.fragment.LeaveListFragment;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.StringUtil;
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

public class LeaveActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MyDataPickerDialog.OnButtonClick, MyBaseEntityListDialog.OnListItemClick {
    public static int CONTACTS_REQUEST = 1001;
    public static int COPY_CONTACTS_REQUEST = 1201;
    private HorizontalListView mApproveListView, copy_listview;
    private View leave_type_layout, start_time_layout, end_time_layout, record_tv, commit_btn;
    private EditText time_lengh_et, day_length, reason_et, tips_tiaoxiu_et;
    private TextView leave_type_tv, start_time_tv, end_time_tv;
    private AddPeopleAdapter addPeopleAdapter, copyPeopleAdapter;
    private LeaveAppEntity mLeaveAppEntity = new LeaveAppEntity();
    private BaseDataEntity mBaseDataEntity;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LeaveActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_leave;
    }

    public void initView() {
        mApproveListView = (HorizontalListView) findViewById(R.id.approve_listview);
        addPeopleAdapter = new AddPeopleAdapter(this, getData());
        mApproveListView.setAdapter(addPeopleAdapter);
        copy_listview = (HorizontalListView) findViewById(R.id.copy_listview);
        copyPeopleAdapter = new AddPeopleAdapter(this, getmCopyUsers());
        copy_listview.setAdapter(copyPeopleAdapter);
        copy_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (position == (mCopyUsers.size() - 1)) {
                        MyListActivity.startActivityForResult(LeaveActivity.this, MyListActivity.TYPE_CONTACTS_LIST, copyPeopleAdapter.getIds(), COPY_CONTACTS_REQUEST);
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
        time_lengh_et = (EditText) findViewById(R.id.time_lengh_et);
        day_length = (EditText) findViewById(R.id.day_length);
        reason_et = (EditText) findViewById(R.id.reason_et);
        leave_type_tv = (TextView) findViewById(R.id.leave_type_tv);
        start_time_tv = (TextView) findViewById(R.id.start_time_tv);
        end_time_tv = (TextView) findViewById(R.id.end_time_tv);
        tips_tiaoxiu_et = (EditText) findViewById(R.id.tips_tiaoxiu_et);
        record_tv = findViewById(R.id.record_tv);
        record_tv.setOnClickListener(this);
        leave_type_layout.setOnClickListener(this);
        start_time_layout.setOnClickListener(this);
        end_time_layout.setOnClickListener(this);
        commit_btn = findViewById(R.id.commit_btn);
        commit_btn.setOnClickListener(this);
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("请假报告单");
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
            case R.id.leave_type_layout:
                showListDialog();
                break;
            case R.id.start_time_layout:
                showDataDialog("开始时间", MyDataPickerDialog.TYPE_START_TIME);
                break;
            case R.id.end_time_layout:
                showDataDialog("结束时间", MyDataPickerDialog.TYPE_END_TIME);
                break;
            case R.id.record_tv:
                MyListActivity.startActivity(this, LeaveListFragment.TYPE_LEAVE_APP_LIST);
                break;
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

    private void showListDialog() {
        List<BaseDataEntity> list = JSONArray.parseArray(MySharepreference.getInstance(this).getString(MySharepreference.ENTITY90LIST), BaseDataEntity.class);
        MyBaseEntityListDialog dialog = new MyBaseEntityListDialog(this, list, this);
        dialog.show();
    }

    private void showDataDialog(String title, int type) {
        MyDataPickerDialog dataPickerDialog = new MyDataPickerDialog(MyDataPickerDialog.YEAR_MONTH, this, title, this, type);
        dataPickerDialog.show();
    }

    @Override
    public void onOkClick(String date, int type, int pos) {
        if (type == MyDataPickerDialog.TYPE_START_TIME) {
            mLeaveAppEntity.start_time = date;
            start_time_tv.setText(date);
        } else if (type == MyDataPickerDialog.TYPE_END_TIME) {
            mLeaveAppEntity.end_time = date;
            end_time_tv.setText(date);
        }
    }

    @Override
    public void onCancelClick(int type, int pos) {

    }

    @Override
    public void onItemclick(BaseDataEntity entity) {
        mBaseDataEntity = entity;
        leave_type_tv.setText(mBaseDataEntity.type_name);
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
        if (mBaseDataEntity == null || StringUtil.isEmpty(mBaseDataEntity.id)) {
            showToast("请选择请假类型");
            return;
        }
        if (mLeaveAppEntity.start_time == null) {
            showToast("请选择开始时间");
            return;
        }
        if (mLeaveAppEntity.end_time == null) {
            showToast("请选择结束时间");
            return;
        }
        /*if (StringUtil.isEmpty(time_lengh_et.getText().toString())) {
            showToast("请输入时长");
            return;
        }*/
        if (StringUtil.isEmpty(day_length.getText().toString())) {
            showToast("请输入请假天数");
            return;
        }
        if (StringUtil.isEmpty(reason_et.getText().toString())) {
            showToast("请输入请假事由");
            return;
        }
        if (mData.size() == 1) {
            showToast("请选择审批人");
            return;
        }
        mLeaveAppEntity.hours = time_lengh_et.getText().toString() + "";
        mLeaveAppEntity.days = day_length.getText().toString();
        mLeaveAppEntity.reson = reason_et.getText().toString();
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
        mLeaveAppEntity.user_names = names.toString();
        mLeaveAppEntity.user_ids = ids.toString();
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
            mLeaveAppEntity.cc_user_names = cc_user_names.toString();
            mLeaveAppEntity.cc_user_ids = cc_user_ids.toString();
        }
        mLeaveAppEntity.leave_type = mBaseDataEntity.id;
        mLeaveAppEntity.leave_type_name = mBaseDataEntity.type_name;
        mLeaveAppEntity.overtime = "" + tips_tiaoxiu_et.getText().toString();
        SaveLeaveAppRequest request = new SaveLeaveAppRequest(mLeaveAppEntity, this);
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), SaveLeaveAppRequest.SAVA_LEAVE_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case SaveLeaveAppRequest.SAVA_LEAVE_REQUEST:
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
