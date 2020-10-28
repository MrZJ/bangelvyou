package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.BaseDataEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.CarmanageRequest;
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

public class CarManageActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MyDataPickerDialog.OnButtonClick, MyBaseEntityListDialog.OnListItemClick {
    public static int CONTACTS_REQUEST = 1001;
    private HorizontalListView mApproveListView, copy_listview;
    private AddPeopleAdapter addPeopleAdapter, copyPeopleAdapter;
    private TextView day_length, patner_et, start_place_et, target_place_et, reason_et, num_et, work_start_time_tv, end_time_tv, remark_et;
    private Spinner car_num_et;
    private List<User> mData = new ArrayList<>();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CarManageActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_car_manage;
    }

    public void initView() {
        mApproveListView = (HorizontalListView) findViewById(R.id.approve_listview);
        addPeopleAdapter = new AddPeopleAdapter(this, getData());
        car_num_et = (Spinner) findViewById(R.id.car_num_et);
        day_length = (TextView) findViewById(R.id.day_num_et);
        patner_et = (TextView) findViewById(R.id.patner_et);
        start_place_et = (TextView) findViewById(R.id.start_place_et);
        target_place_et = (TextView) findViewById(R.id.target_place_et);
        work_start_time_tv = (TextView) findViewById(R.id.work_start_time_tv);
        findViewById(R.id.start_time_layout1).setOnClickListener(this);
        findViewById(R.id.end_time_layout).setOnClickListener(this);
        findViewById(R.id.commit_btn).setOnClickListener(this);
        findViewById(R.id.record_tv).setOnClickListener(this);
        end_time_tv = (TextView) findViewById(R.id.end_time_tv);
        reason_et = (TextView) findViewById(R.id.reason_et);
        remark_et = (TextView) findViewById(R.id.remark_et);
        num_et = (TextView) findViewById(R.id.num_et);
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
                        MyListActivity.startActivityForResult(CarManageActivity.this, MyListActivity.TYPE_CONTACTS_LIST, copyPeopleAdapter.getIds(), COPY_CONTACTS_REQUEST);
                    } else {
                        mCopyUsers.remove(position);
                        copyPeopleAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                }
            }
        });
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("派车管理报告单");
        car_num_et.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getCarNumList()));
    }


    private List<User> getData() {
        mData = new ArrayList<>();
        User p = new User();
        String jsonString = MySharepreference.getInstance(this).getString(MySharepreference.ENTITY_USER);
        List<User> list = JSONArray.parseArray(jsonString, User.class);
        mData.addAll(list);
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
                MyListActivity.startActivity(this, MyListActivity.TYPE_CAR_MANAGE_LIST);
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


    @Override
    public void onOkClick(String date, int type, int pos) {
        if (type == MyDataPickerDialog.TYPE_START_TIME) {
            work_start_time_tv.setText(date);
        } else if (type == MyDataPickerDialog.TYPE_END_TIME) {
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
        String car_num = (String) car_num_et.getSelectedItem();
        String work_start_time = work_start_time_tv.getText().toString();
        String end_time = end_time_tv.getText().toString();
        String day_lenths = day_length.getText().toString();
        String patener = patner_et.getText().toString() + "";
        String start_place = start_place_et.getText().toString();
        String target_place = target_place_et.getText().toString();
        String reason = reason_et.getText().toString();
        String num = num_et.getText().toString();
        List<User> users = addPeopleAdapter.getData();
        if (ListUtils.isEmpty(users) || users.size() == 1) {
            showToast("请选择审批人");
            return;
        }
        if (StringUtil.isEmpty(car_num)) {
            showToast("请输入车牌号");
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
        if (StringUtil.isEmpty(day_lenths)) {
            showToast("请输入天数");
            return;
        }
       /* if (StringUtil.isEmpty(patener)) {
            showToast("请输入同行人");
            return;
        }*/
        if (StringUtil.isEmpty(start_place)) {
            showToast("请输入出发地");
            return;
        }
        if (StringUtil.isEmpty(target_place)) {
            showToast("请输入目的地");
            return;
        }
        if (StringUtil.isEmpty(reason)) {
            showToast("请输入用车原因");
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
        CarmanageRequest request = new CarmanageRequest(car_num, work_start_time, end_time, day_lenths, patener, start_place, target_place, reason, num,
                user_ids.toString(), user_names.toString(), cc_user_ids.toString(), cc_user_names.toString(), user_id, remark_et.getText().toString());
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), CarmanageRequest.CAR_MANAGE_REQUEST);
        showDialog();
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        closeDialog();
        switch (action) {
            case CarmanageRequest.CAR_MANAGE_REQUEST:
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

    private List<String> getCarNumList() {
        List<String> list = new ArrayList<>();
        String jsonString = MySharepreference.getInstance(this).getString(MySharepreference.ENTITY500LIST);
        List<BaseDataEntity> data = JSONArray.parseArray(jsonString, BaseDataEntity.class);
        if (!ListUtils.isEmpty(data)) {
            for (int i = 0; i < data.size(); i++) {
                BaseDataEntity dataEntity = data.get(i);
                list.add(dataEntity.type_name);
            }
        }
        return list;
    }
}
