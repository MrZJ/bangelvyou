package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.SaveBusTraRequest;
import com.shishoureport.system.ui.adapter.AddPeopleAdapter;
import com.shishoureport.system.ui.fragment.ContactsFragment;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.StringUtil;
import com.shishoureport.system.wibget.HorizontalListView;
import com.shishoureport.system.wibget.MyDataPickerDialog;
import com.shishoureport.system.wibget.MyListDialog;

import java.util.ArrayList;
import java.util.List;

import static com.shishoureport.system.ui.activity.LeaveActivity.CONTACTS_REQUEST;
import static com.shishoureport.system.ui.activity.LeaveActivity.COPY_CONTACTS_REQUEST;
import static com.shishoureport.system.ui.fragment.BusTraListFragment.TYPE_ALL_BUS_TRA_LIST;

/**
 * Created by jianzhang.
 * on 2017/9/5.
 * copyright easybiz.
 */

public class BusinessTravelActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, MyListDialog.OnListItemClick, MyDataPickerDialog.OnButtonClick {
    private HorizontalListView mApproveListView, copy_listview;
    private AddPeopleAdapter addPeopleAdapter, copyPeopleAdapter;
    private TextView title_tv, leave_type_tv, travel_place_et, end_time_tv, start_time_tv, time_lengh_et, plane_reason_et,
            travel_reason_et, paterner_et, get_money_et, chager_man_et, monitor_et, monitor_tv;
    private EditText leave_time_et;
    private View leave_type_layout, record_tv, commit_btn, end_time_layout, start_time_layout, start_go_layout;
    private String startTime, endTime, travelType;

    private List<User> mData = new ArrayList<>();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BusinessTravelActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_business_travel;
    }

    public void initView() {
        mApproveListView = (HorizontalListView) findViewById(R.id.approve_listview);
        addPeopleAdapter = new AddPeopleAdapter(this, getData());
        mApproveListView.setAdapter(addPeopleAdapter);
        mApproveListView.setOnItemClickListener(this);
        monitor_tv = (TextView) findViewById(R.id.monitor_tv);
        copy_listview = (HorizontalListView) findViewById(R.id.copy_listview);
        findViewById(R.id.company_layout).setVisibility(View.GONE);
        copyPeopleAdapter = new AddPeopleAdapter(this, getmCopyUsers());
        copy_listview.setAdapter(copyPeopleAdapter);
        copy_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (position == (mCopyUsers.size() - 1)) {
                        MyListActivity.startActivityForResult(BusinessTravelActivity.this, MyListActivity.TYPE_CONTACTS_LIST, copyPeopleAdapter.getIds(), COPY_CONTACTS_REQUEST);
                    } else {
                        mCopyUsers.remove(position);
                        copyPeopleAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                }
            }
        });
        findViewById(R.id.add_travel_layout).setOnClickListener(this);
        leave_type_layout = findViewById(R.id.leave_type_layout);
        leave_type_layout.setOnClickListener(this);
        end_time_layout = findViewById(R.id.end_time_layout);
        end_time_layout.setOnClickListener(this);
        start_time_layout = findViewById(R.id.start_time_layout);
        start_time_layout.setOnClickListener(this);
        leave_type_tv = (TextView) findViewById(R.id.leave_type_tv);
        record_tv = findViewById(R.id.record_tv);
        leave_time_et = (EditText) findViewById(R.id.leave_time_et);
//        leave_time_et.setEnabled(false);
        leave_time_et.setFocusable(false);
        leave_time_et.setHint("请选择出发时间");
        leave_time_et.setOnClickListener(this);
        travel_place_et = (TextView) findViewById(R.id.travel_place_et);
        end_time_tv = (TextView) findViewById(R.id.end_time_tv);
        start_time_tv = (TextView) findViewById(R.id.start_time_tv);
        time_lengh_et = (TextView) findViewById(R.id.time_lengh_et);
        plane_reason_et = (TextView) findViewById(R.id.plane_reason_et);
        travel_reason_et = (TextView) findViewById(R.id.travel_reason_et);
        paterner_et = (TextView) findViewById(R.id.paterner_et);
        get_money_et = (TextView) findViewById(R.id.get_money_et);
        chager_man_et = (TextView) findViewById(R.id.chager_man_et);
        monitor_et = (TextView) findViewById(R.id.monitor_et);
        start_go_layout = findViewById(R.id.start_go_layout);
        monitor_tv.setText("出差线路");
        start_go_layout.setOnClickListener(this);
        record_tv.setOnClickListener(this);
        title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("出差报告单");
        commit_btn = findViewById(R.id.commit_btn);
        commit_btn.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leave_type_layout:
                showListDialog();
                break;
            case R.id.record_tv:
                MyListActivity.startActivity(this, TYPE_ALL_BUS_TRA_LIST);
                break;
            case R.id.start_time_layout:
                showTimeDialog("开始时间", MyDataPickerDialog.TYPE_START_TIME);
                break;
            case R.id.end_time_layout:
                showTimeDialog("结束时间", MyDataPickerDialog.TYPE_END_TIME);
                break;
            case R.id.commit_btn:
                commitData();
                break;
            case R.id.start_go_layout:
            case R.id.leave_time_et:
                showTimeDialog("出发时间", MyDataPickerDialog.TYPE_GO_TIME);
                break;
        }
    }

    private void commitData() {
        if (StringUtil.isEmpty(travel_place_et.getText().toString())) {
            showToast("请输入出差地点");
            return;
        }
        if (StringUtil.isEmpty(startTime)) {
            showToast("请选择预计出差开始时间");
            return;
        }
        if (StringUtil.isEmpty(endTime)) {
            showToast("请选择预计出差结束时间");
            return;
        } /*else if (StringUtil.isEmpty(travelType)) {//删除该需求
            showToast("请选择出行工具");
            return;
        }*/ else if (StringUtil.isEmpty(travel_reason_et.getText().toString())) {
            showToast("请输入出差事由");
            return;
        }/* else if (StringUtil.isEmpty(time_lengh_et.getText().toString())) {
            showToast("请输入出差时间");
            return;
        } */ /*else if (StringUtil.isEmpty(paterner_et.getText().toString())) {
            showToast("请输入同行人");
            return;
        }*/
        if (StringUtil.isEmpty(leave_time_et.getText().toString())) {
            showToast("请选择出发时间");
            return;
        }
      /*  if (StringUtil.isEmpty(get_money_et.getText().toString())) {
            showToast("请输入暂支差旅费");
            return;
        } *//*else if (StringUtil.isEmpty(chager_man_et.getText().toString())) {
            showToast("请输入单位负责人");
            return;
        } else*/
        /*if (StringUtil.isEmpty(monitor_et.getText().toString())) {
            showToast("请输入出差线路");
            return;
        }*/
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
        SaveBusTraRequest request = new SaveBusTraRequest(MySharepreference.getInstance(this).getUser().id,
                names.toString(), ids.toString(), travel_place_et.getText().toString(), startTime, endTime, paterner_et.getText().toString(),
                travel_reason_et.getText().toString(), plane_reason_et.getText().toString(), leave_time_et.getText().toString(), get_money_et.getText().toString()
                , chager_man_et.getText().toString(), monitor_et.getText().toString(), cc_user_names.toString(), cc_user_ids.toString());
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), SaveBusTraRequest.SAVE_BUS_TRA_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case SaveBusTraRequest.SAVE_BUS_TRA_REQUEST:
                showToast("提交成功");
                finish();
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        showToast("提交失败，请重试");
    }

    private void showListDialog() {
        String[] arr = getResources().getStringArray(R.array.business_travel_ways);
        MyListDialog dialog = new MyListDialog(this, arr, this);
        dialog.show();
    }

    @Override
    public void onItemclick(String str) {
        travelType = str;
        leave_type_tv.setText(str);
    }

    private void showTimeDialog(String titlt, int type) {
        MyDataPickerDialog dataPickerDialog = new MyDataPickerDialog(MyDataPickerDialog.YEAR_MONTH, this, titlt, this, type);
        dataPickerDialog.show();
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

    @Override
    public void onOkClick(String date, int type, int pos) {
        if (type == MyDataPickerDialog.TYPE_START_TIME) {
            start_time_tv.setText(date);
            startTime = date;
        } else if (type == MyDataPickerDialog.TYPE_END_TIME) {
            end_time_tv.setText(date);
            endTime = date;
        } else if (type == MyDataPickerDialog.TYPE_GO_TIME) {
            leave_time_et.setText(date);
        }
    }

    @Override
    public void onCancelClick(int type, int pos) {

    }
}