package com.shishoureport.system.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.entity.WorkOverTimeEntity;
import com.shishoureport.system.request.GetMenberListRequest;
import com.shishoureport.system.utils.ListUtils;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.TimeDateUtil;
import com.shishoureport.system.wibget.MyDataPickerDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/4.
 * copyright easybiz.
 */

public class AddWorkOverTimeActivity extends BaseActivity implements View.OnClickListener, MyDataPickerDialog.OnButtonClick {
    public static final String OBJECT = "object";
    private TextView start_time_tv, end_time_tv;
    private View start_time_layout, end_time_layout, commit_btn;
    private Spinner name_sp;
    private boolean isStartSel, isEndSel;

    public static void startActivityForResult(Activity context, int requestCode) {
        Intent intent = new Intent(context, AddWorkOverTimeActivity.class);
        context.startActivityForResult(intent, requestCode);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_add_work_over_time;
    }

    public void initView() {
        start_time_layout = findViewById(R.id.start_time_layout);
        end_time_layout = findViewById(R.id.end_time_layout);
        commit_btn = findViewById(R.id.commit_btn);
        name_sp = (Spinner) findViewById(R.id.name_sp);
        start_time_tv = (TextView) findViewById(R.id.start_time_tv);
        end_time_tv = (TextView) findViewById(R.id.end_time_tv);
        start_time_layout.setOnClickListener(this);
        end_time_layout.setOnClickListener(this);
        commit_btn.setOnClickListener(this);
        loadData();
    }

    private void loadData() {
        User user = MySharepreference.getInstance(this).getUser();
        if (user != null) {
            GetMenberListRequest request = new GetMenberListRequest(user.dept_id);
            httpGetRequest(request.getRequestUrl(), GetMenberListRequest.GET_MENBER_LISTREQUEST);
        }
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
            case R.id.commit_btn:
                commitData();
                break;
        }

    }


    private void showDataDialog(String title, int type) {
        MyDataPickerDialog dataPickerDialog = new MyDataPickerDialog(this, title, this, type);
        dataPickerDialog.show();
    }

    @Override
    public void onOkClick(String date, int type, int pos) {
        if (type == MyDataPickerDialog.TYPE_START_TIME) {
            isStartSel = true;
            start_time_tv.setText(date);
        } else if (type == MyDataPickerDialog.TYPE_END_TIME) {
            isEndSel = true;
            end_time_tv.setText(date);
        }
    }

    @Override
    public void onCancelClick(int type, int pos) {

    }

    private void commitData() {
        int sel = name_sp.getSelectedItemPosition();
        if (sel == -1) {
            showToast("请选择加班人员");
            return;
        }
        if (!isStartSel) {
            showToast("请选择开始时间");
            return;
        }
        if (!isEndSel) {
            showToast("请选择结束时间");
            return;
        }
        WorkOverTimeEntity entity = new WorkOverTimeEntity();
        String startTime = start_time_tv.getText().toString();
        String endTime = end_time_tv.getText().toString();
        entity.time_lenth = TimeDateUtil.getTimeLength(startTime, endTime);
        if (Double.valueOf(entity.time_lenth) <= 0) {
            showToast("结束时间请大于开始时间");
            return;
        }
        entity.time = startTime + "~" + endTime;
        entity.startTime = startTime;
        entity.endTime = endTime;
        User user = list.get(sel);
        entity.user_id = user.id;
        entity.name = user.real_name;
        Intent intent = new Intent();
        intent.putExtra(OBJECT, entity);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case GetMenberListRequest.GET_MENBER_LISTREQUEST:
                handRequest(json);
                break;
        }
    }

    public List<User> list;

    private void handRequest(String data) {
        try {
            list = JSONObject.parseArray(data, User.class);
            if (!ListUtils.isEmpty(list)) {
                List<String> names = new ArrayList<>();
                for (User user : list) {
                    names.add(user.real_name);
                }
                name_sp.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
    }
}
