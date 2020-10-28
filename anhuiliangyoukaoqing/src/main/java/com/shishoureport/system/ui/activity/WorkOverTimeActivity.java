package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.entity.BaseDataEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.entity.WorkOverTimeEntity;
import com.shishoureport.system.request.SaveOverTimeRequest;
import com.shishoureport.system.ui.adapter.AddPeopleAdapter;
import com.shishoureport.system.ui.adapter.WorkOverTimeAdapter;
import com.shishoureport.system.ui.fragment.ContactsFragment;
import com.shishoureport.system.utils.ListUtils;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.StringUtil;
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

public class WorkOverTimeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MyDataPickerDialog.OnButtonClick, MyBaseEntityListDialog.OnListItemClick {
    public static int CONTACTS_REQUEST = 1001;
    public static int ADD_TASK_REQUEST = 122;
    private HorizontalListView mApproveListView, copy_listview;
    private AddPeopleAdapter addPeopleAdapter, copyPeopleAdapter;
    private WorkOverTimeAdapter workOverTimeAdapter;
    private ListView listview;
    private View add_task_tv, start_time_layout, commit_btn, record_tv;
    private TextView start_time_tv, over_time_reason_tv;
    private List<User> mData = new ArrayList<>();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, WorkOverTimeActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_work_over_time;
    }

    public void initView() {
        mApproveListView = (HorizontalListView) findViewById(R.id.approve_listview);
        listview = (ListView) findViewById(R.id.listview);
        add_task_tv = findViewById(R.id.add_task_tv);
        start_time_layout = findViewById(R.id.start_time_layout);
        start_time_tv = (TextView) findViewById(R.id.start_time_tv);
        commit_btn = findViewById(R.id.commit_btn);
        record_tv = findViewById(R.id.record_tv);
        record_tv.setOnClickListener(this);
        over_time_reason_tv = (TextView) findViewById(R.id.over_time_reason_tv);
        commit_btn.setOnClickListener(this);
        start_time_layout.setOnClickListener(this);
        add_task_tv.setOnClickListener(this);
        workOverTimeAdapter = new WorkOverTimeAdapter(this, new ArrayList<WorkOverTimeEntity>());
        listview.setAdapter(workOverTimeAdapter);
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
                        MyListActivity.startActivityForResult(WorkOverTimeActivity.this, MyListActivity.TYPE_CONTACTS_LIST, copyPeopleAdapter.getIds(),COPY_CONTACTS_REQUEST);
                    } else {
                        mCopyUsers.remove(position);
                        copyPeopleAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                }
            }
        });
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("集体加班报告单");
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
                showDataDialog("加班日期", MyDataPickerDialog.TYPE_START_TIME);
                break;
            case R.id.record_tv:
                MyListActivity.startActivity(this, MyListActivity.TYPE_OVER_TIME_LIST);
                break;
            case R.id.commit_btn:
                commitData();
                break;
            case R.id.add_task_tv:
                AddWorkOverTimeActivity.startActivityForResult(this, ADD_TASK_REQUEST);
                break;
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            if (position == (mData.size() - 1)) {
                MyListActivity.startActivityForResult(this, MyListActivity.TYPE_CONTACTS_LIST,addPeopleAdapter.getIds(), CONTACTS_REQUEST);
            } else {
                mData.remove(position);
                addPeopleAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDataDialog(String title, int type) {
        MyDataPickerDialog dataPickerDialog = new MyDataPickerDialog(MyDataPickerDialog.YEAR_MONTH, this, title, this, type);
        dataPickerDialog.show();
    }

    private boolean isTimeSel;

    @Override
    public void onOkClick(String date, int type, int pos) {
        isTimeSel = true;
        start_time_tv.setText(date);
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
            } else if (requestCode == ADD_TASK_REQUEST) {
                try {
                    WorkOverTimeEntity entity = (WorkOverTimeEntity) data.getSerializableExtra(AddWorkOverTimeActivity.OBJECT);
                    workOverTimeAdapter.addData(entity);
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
        if (ListUtils.isEmpty(users) || users.size() == 1) {
            showToast("请选择审批人");
            return;
        }
        List<WorkOverTimeEntity> workOverTimeEntities = workOverTimeAdapter.getData();
        if (ListUtils.isEmpty(workOverTimeEntities)) {
            showToast("请添加加班人员");
            return;
        }
        if (StringUtil.isEmpty(over_time_reason_tv.getText().toString())) {
            showToast("请输入加班原因");
            return;
        }
        if (!isTimeSel) {
            showToast("请选择加班时间");
            return;
        }
        StringBuilder user_apply_ids = new StringBuilder();
        StringBuilder user_apply_names = new StringBuilder();
        StringBuilder user_apply_start_times = new StringBuilder();
        StringBuilder user_apply_end_times = new StringBuilder();
        StringBuilder user_apply_hours = new StringBuilder();
        for (int i = 0; i < workOverTimeEntities.size(); i++) {
            WorkOverTimeEntity entity = workOverTimeEntities.get(i);
            if (i != workOverTimeEntities.size() - 1) {
                user_apply_ids.append(entity.user_id).append(",");
                user_apply_names.append(entity.name).append(",");
                user_apply_start_times.append(entity.startTime).append(",");
                user_apply_end_times.append(entity.endTime).append(",");
                user_apply_hours.append(entity.time_lenth).append(",");
            } else {
                user_apply_ids.append(entity.user_id);
                user_apply_names.append(entity.name);
                user_apply_start_times.append(entity.startTime);
                user_apply_end_times.append(entity.endTime);
                user_apply_hours.append(entity.time_lenth);
            }
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
        SaveOverTimeRequest request = new SaveOverTimeRequest(user_id, user_apply_ids.toString(),
                user_apply_names.toString(), user_apply_start_times.toString(), user_apply_end_times.toString(),
                user_apply_hours.toString(), user_ids.toString(), user_names.toString(), user.dept_id, start_time_tv.getText().toString(),
                over_time_reason_tv.getText().toString(), cc_user_names.toString(), cc_user_ids.toString());
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), SaveOverTimeRequest.SAVE_OVERTIME_REQUEST);
        showDialog();
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        closeDialog();
        switch (action) {
            case SaveOverTimeRequest.SAVE_OVERTIME_REQUEST:
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
