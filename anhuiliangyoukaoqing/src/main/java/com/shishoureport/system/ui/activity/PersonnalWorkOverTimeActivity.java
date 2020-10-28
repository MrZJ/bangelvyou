package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.entity.BaseDataEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.PersonalSaveOverTimeRequest;
import com.shishoureport.system.ui.adapter.AddPeopleAdapter;
import com.shishoureport.system.ui.fragment.ContactsFragment;
import com.shishoureport.system.utils.ListUtils;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.StringUtil;
import com.shishoureport.system.utils.TimeDateUtil;
import com.shishoureport.system.wibget.HorizontalListView;
import com.shishoureport.system.wibget.MyBaseEntityListDialog;
import com.shishoureport.system.wibget.MyDataPickerDialog;

import java.util.ArrayList;
import java.util.List;

import static com.shishoureport.system.ui.activity.LeaveActivity.COPY_CONTACTS_REQUEST;

/**
 * Created by jianzhang.
 * on 2017/9/4.
 * copyright easybiz.
 */

public class PersonnalWorkOverTimeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MyDataPickerDialog.OnButtonClick, MyBaseEntityListDialog.OnListItemClick {
    public static int CONTACTS_REQUEST = 1001;
    public static int ADD_TASK_REQUEST = 122;
    private HorizontalListView mApproveListView, copy_listview;
    private AddPeopleAdapter addPeopleAdapter, copyPeopleAdapter;
    private View start_time_layout, start_time_layout1, end_time_layout, commit_btn, record_tv;
    private TextView start_time_tv, over_time_reason_tv, work_start_time_tv, end_time_tv, time_lengh_et;
    private List<User> mData = new ArrayList<>();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PersonnalWorkOverTimeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_personnal_work_over_time;
    }

    public void initView() {
        mApproveListView = (HorizontalListView) findViewById(R.id.approve_listview);
        start_time_layout = findViewById(R.id.start_time_layout);
        start_time_tv = (TextView) findViewById(R.id.start_time_tv);
        start_time_layout1 = findViewById(R.id.start_time_layout1);
        start_time_layout1.setOnClickListener(this);
        end_time_layout = findViewById(R.id.end_time_layout);
        end_time_layout.setOnClickListener(this);
        time_lengh_et = (TextView) findViewById(R.id.time_lengh_et);
        commit_btn = findViewById(R.id.commit_btn);
        record_tv = findViewById(R.id.record_tv);
        work_start_time_tv = (TextView) findViewById(R.id.work_start_time_tv);
        record_tv.setOnClickListener(this);
        over_time_reason_tv = (TextView) findViewById(R.id.over_time_reason_tv);
        end_time_tv = (TextView) findViewById(R.id.end_time_tv);
        commit_btn.setOnClickListener(this);
        start_time_layout.setOnClickListener(this);
        addPeopleAdapter = new AddPeopleAdapter(this, getData());
        mApproveListView.setAdapter(addPeopleAdapter);
        mApproveListView.setOnItemClickListener(this);
        copy_listview = (HorizontalListView) findViewById(R.id.copy_listview);
        copyPeopleAdapter = new AddPeopleAdapter(this, getmCopyUsers());
        copy_listview.setAdapter(copyPeopleAdapter);
        copy_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (position == (mCopyUsers.size() - 1)) {
                        MyListActivity.startActivityForResult(PersonnalWorkOverTimeActivity.this, MyListActivity.TYPE_CONTACTS_LIST, copyPeopleAdapter.getIds(), COPY_CONTACTS_REQUEST);
                    } else {
                        mCopyUsers.remove(position);
                        copyPeopleAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                }
            }
        });
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("个人加班报告单");
    }


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
                MyDataPickerDialog dataPickerDialog = new MyDataPickerDialog(MyDataPickerDialog.YEAR_MONTH, this, "加班日期", this, MyDataPickerDialog.TYPE_GO_TIME);
                dataPickerDialog.show();
                break;
            case R.id.record_tv:
                MyListActivity.startActivity(this, MyListActivity.TYPE_PERSONAL_OVER_TIME_LIST);
                break;
            case R.id.commit_btn:
                commitData();
                break;
            case R.id.start_time_layout1:
                showDataDialog("开始时间", MyDataPickerDialog.TYPE_START_TIME);
                break;
            case R.id.end_time_layout:
                showDataDialog("结束时间", MyDataPickerDialog.TYPE_END_TIME);
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
            e.printStackTrace();
        }
    }

    private void showDataDialog(String title, int type) {
        MyDataPickerDialog dataPickerDialog = new MyDataPickerDialog(this, title, this, type);
        dataPickerDialog.show();
    }

    private boolean isTimeSel;

    @Override
    public void onOkClick(String date, int type, int pos) {
        if (type == MyDataPickerDialog.TYPE_GO_TIME) {
            isTimeSel = true;
            start_time_tv.setText(date);
        } else if (type == MyDataPickerDialog.TYPE_START_TIME) {
            String end_time = end_time_tv.getText().toString();
            if (!StringUtil.isEmpty(end_time)) {
                String time_lenth = TimeDateUtil.getTimeLength(date, end_time);
                if (Double.valueOf(time_lenth) <= 0) {
                    showToast("开始时间请小于结束时间");
                    return;
                }
                time_lengh_et.setText(time_lenth);
                work_start_time_tv.setText(date);
            } else {
                work_start_time_tv.setText(date);
            }
        } else if (type == MyDataPickerDialog.TYPE_END_TIME) {
            String start_time = work_start_time_tv.getText().toString();
            if (!StringUtil.isEmpty(start_time)) {
                String time_lenth = TimeDateUtil.getTimeLength(start_time, date);
                if (Double.valueOf(time_lenth) <= 0) {
                    showToast("结束时间请大于开始时间");
                    return;
                }
                end_time_tv.setText(date);
                time_lengh_et.setText(time_lenth);
            } else {
                end_time_tv.setText(date);
            }
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
        List<User> users = addPeopleAdapter.getData();
        String start_time = start_time_tv.getText().toString();
        String work_start_time = work_start_time_tv.getText().toString();
        String end_time = end_time_tv.getText().toString();
        if (ListUtils.isEmpty(users) || users.size() == 1) {
            showToast("请选择审批人");
            return;
        }
        if (StringUtil.isEmpty(over_time_reason_tv.getText().toString())) {
            showToast("请输入加班原因");
            return;
        }
        if (StringUtil.isEmpty(start_time)) {
            showToast("请选择加班时间");
            return;
        }
        if (StringUtil.isEmpty(work_start_time)) {
            showToast("请选择开始时间");
            return;
        }
        if (StringUtil.isEmpty(end_time)) {
            showToast("请选择结束时间");
            return;
        }
        String time_lenth = TimeDateUtil.getTimeLength(work_start_time, end_time);
        if (Double.valueOf(time_lenth) <= 0) {
            showToast("结束时间请大于开始时间");
            return;
        }
        StringBuilder user_ids = new StringBuilder();
        StringBuilder user_names = new StringBuilder();
        for (int i = 0; i < users.size() - 1; i++) {
            User user = users.get(i);
            if (i != users.size() - 2) {
                user_ids.append(user.id).append(",");
                user_names.append(user.real_name).append(",");
            } else {
                user_ids.append(user.id);
                user_names.append(user.real_name);
            }
        }
        User user = MySharepreference.getInstance(this).getUser();
        String user_id = user.id;
        StringBuilder cc_user_names = new StringBuilder();
        StringBuilder cc_user_ids = new StringBuilder();
        for (int i = 0; i < mCopyUsers.size() - 1; i++) {
            User user1 = mCopyUsers.get(i);
            if (mCopyUsers.size() - 2 == i) {
                cc_user_names.append(user1.user_name);
                cc_user_ids.append(user1.id);
            } else {
                cc_user_names.append(user1.user_name + ",");
                cc_user_ids.append(user1.id + ",");
            }
        }
        PersonalSaveOverTimeRequest request = new PersonalSaveOverTimeRequest(work_start_time, end_time, user_ids.toString(),
                user_names.toString(), cc_user_ids.toString(), cc_user_names.toString(), user_id, start_time, time_lenth, over_time_reason_tv.getText().toString());
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), PersonalSaveOverTimeRequest.PERSONAL_SAVE_OVERTIME_REQUEST);
        showDialog();
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        closeDialog();
        switch (action) {
            case PersonalSaveOverTimeRequest.PERSONAL_SAVE_OVERTIME_REQUEST:
                handleRequest(json);
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        closeDialog();
        showToast("提交失败，请重试");
    }

    private void handleRequest(String json) {
        showToast("提交成功");
        finish();
    }
}
