package com.shishoureport.system.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.AttendanceLeave;
import com.shishoureport.system.entity.BaseDataEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.LeaveAppDetail;
import com.shishoureport.system.request.LeaveAppDetailRequest;
import com.shishoureport.system.request.SaveAuditRequest;
import com.shishoureport.system.ui.adapter.ApprovePeopleListAdapter;
import com.shishoureport.system.ui.adapter.CopyPeopleListAdapter;
import com.shishoureport.system.utils.ListUtils;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.StringUtil;
import com.shishoureport.system.utils.TimeDateUtil;
import com.shishoureport.system.wibget.HorizontalListView;
import com.shishoureport.system.wibget.MyBaseEntityListDialog;
import com.shishoureport.system.wibget.MyDataPickerDialog;

/**
 * Created by jianzhang.
 * on 2017/9/4.
 * copyright easybiz.
 */

public class LeaveAppApproveActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MyDataPickerDialog.OnButtonClick, MyBaseEntityListDialog.OnListItemClick {
    public static final String KEY_LEAVE_APP = "key_leave_app";
    private View ok_btn, cancel_btn;
    private HorizontalListView mApproveListView, copy_listview;
    private TextView time_lengh_et, day_length, reason_et, tips_tiaoxiu_et, audit_suggestion_et;
    private TextView leave_type_tv, start_time_tv, end_time_tv;
    private AttendanceLeave mLeaveAppInfo;

    public static void startActivityForResult(Activity context, int requestcode, AttendanceLeave leave) {
        Intent intent = new Intent(context, LeaveAppApproveActivity.class);
        intent.putExtra(KEY_LEAVE_APP, leave);
        context.startActivityForResult(intent, requestcode);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_app_lea_app;
    }

    public void initView() {
        mLeaveAppInfo = (AttendanceLeave) getIntent().getSerializableExtra(KEY_LEAVE_APP);
        time_lengh_et = (TextView) findViewById(R.id.time_lengh_et);
        day_length = (TextView) findViewById(R.id.day_length);
        mApproveListView = (HorizontalListView) findViewById(R.id.approve_listview);
        copy_listview = (HorizontalListView) findViewById(R.id.copy_listview);
        reason_et = (TextView) findViewById(R.id.reason_et);
        leave_type_tv = (TextView) findViewById(R.id.leave_type_tv);
        start_time_tv = (TextView) findViewById(R.id.start_time_tv);
        end_time_tv = (TextView) findViewById(R.id.end_time_tv);
        tips_tiaoxiu_et = (TextView) findViewById(R.id.tips_tiaoxiu_et);
        audit_suggestion_et = (TextView) findViewById(R.id.audit_suggestion_et);
        ok_btn = findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(this);
        cancel_btn = findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(this);
        loadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_btn://批准请假
                commitData(SaveAuditRequest.PASS);
                break;
            case R.id.cancel_btn://不批准请假
                commitData(SaveAuditRequest.NOT_PASS);
                break;
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onOkClick(String date, int type, int pos) {
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
    }


    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case LeaveAppDetailRequest.LEAVE_APP_REQUEST:
                handleRequest(json);
                break;
            case SaveAuditRequest.SAVA_AUDIT_REQUEST:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        switch (action) {
            case LeaveAppDetailRequest.LEAVE_APP_REQUEST:
                showToast("请求数据失败，请重试");
                break;
            case SaveAuditRequest.SAVA_AUDIT_REQUEST:
                showToast("提交失败");
                break;
        }

    }

    private void handleRequest(String json) {
        LeaveAppDetail detail = JSONObject.parseObject(json, LeaveAppDetail.class);
        if (detail != null) {
            time_lengh_et.setText(detail.hours);
            day_length.setText(detail.days);
            reason_et.setText(detail.reson);
            tips_tiaoxiu_et.setText(detail.overtime);
            leave_type_tv.setText(detail.leave_type_name);
            start_time_tv.setText(TimeDateUtil.dateTime(detail.start_time));
            end_time_tv.setText(TimeDateUtil.dateTime(detail.end_time));
            if (!ListUtils.isEmpty(detail.attendanceAuditList)) {
                mApproveListView.setAdapter(new ApprovePeopleListAdapter(this, detail.attendanceAuditList));
            } else {
                mApproveListView.setVisibility(View.GONE);
            }
            if (!ListUtils.isEmpty(detail.attendanceCcList)) {
                copy_listview.setAdapter(new CopyPeopleListAdapter(this, detail.attendanceCcList));
            } else {
                copy_listview.setVisibility(View.GONE);
            }
        } else {
            time_lengh_et.setText("");
            day_length.setText("");
            reason_et.setText("");
            tips_tiaoxiu_et.setText("");
            leave_type_tv.setText("");
            start_time_tv.setText("");
            end_time_tv.setText("");
        }
    }

    private void loadData() {
        LeaveAppDetailRequest request = new LeaveAppDetailRequest(mLeaveAppInfo.id);
        httpGetRequest(request.getRequestUrl(), LeaveAppDetailRequest.LEAVE_APP_REQUEST);
    }

    private void commitData(String is_pass) {
        if ("0".equals(is_pass) && StringUtil.isEmpty(audit_suggestion_et.getText().toString())) {
            showToast("请填写审批意见");
            return;
        }
        SaveAuditRequest request = new SaveAuditRequest(MySharepreference.getInstance(this).getUser().id, is_pass, mLeaveAppInfo.id, audit_suggestion_et.getText().toString());
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), SaveAuditRequest.SAVA_AUDIT_REQUEST);
    }
}
