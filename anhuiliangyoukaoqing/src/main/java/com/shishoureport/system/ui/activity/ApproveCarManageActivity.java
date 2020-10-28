package com.shishoureport.system.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.BaseDataEntity;
import com.shishoureport.system.entity.CarMangeEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.ApproveCarManageDetailListRequest;
import com.shishoureport.system.request.ApproveCarManageRequest;
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

public class ApproveCarManageActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MyDataPickerDialog.OnButtonClick, MyBaseEntityListDialog.OnListItemClick {
    public static final int REQUEST_CODE = 102;
    private HorizontalListView mApproveListView, copy_listview;
    private TextView car_num_et, day_num_et, patner_et, start_place_et, target_place_et, reason_et, num_et, work_start_time_tv, end_time_tv, remark_et, audit_suggestion_et;
    private String mId;
    private CarMangeEntity mEntity;

    public static void startActivity(Fragment context, String id) {
        Intent intent = new Intent(context.getContext(), ApproveCarManageActivity.class);
        intent.putExtra("id", id);
        context.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_approve_car_manage;
    }

    public void initView() {
        mId = getIntent().getStringExtra("id");
        if (StringUtil.isEmpty(mId)) {
            finish();
            return;
        }
        mApproveListView = (HorizontalListView) findViewById(R.id.approve_listview);
        copy_listview = (HorizontalListView) findViewById(R.id.copy_listview);
        car_num_et = (TextView) findViewById(R.id.car_num_et);
        day_num_et = (TextView) findViewById(R.id.day_num_et);
        patner_et = (TextView) findViewById(R.id.patner_et);
        start_place_et = (TextView) findViewById(R.id.start_place_et);
        target_place_et = (TextView) findViewById(R.id.target_place_et);
        work_start_time_tv = (TextView) findViewById(R.id.work_start_time_tv);
        end_time_tv = (TextView) findViewById(R.id.end_time_tv);
        reason_et = (TextView) findViewById(R.id.reason_et);
        remark_et = (TextView) findViewById(R.id.remark_et);
        num_et = (TextView) findViewById(R.id.num_et);
        audit_suggestion_et = (TextView) findViewById(R.id.audit_suggestion_et);
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        findViewById(R.id.ok_btn).setOnClickListener(this);
        findViewById(R.id.cancel_btn).setOnClickListener(this);
        title_tv.setText("派车审批");
        User user = MySharepreference.getInstance(this).getUser();
        ApproveCarManageDetailListRequest request = new ApproveCarManageDetailListRequest(mId, user.id);
        httpGetRequest(request.getRequestUrl(), ApproveCarManageDetailListRequest.APPROVE_CAR_MANAGE_DETAIL_LIST_REQUEST);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_btn:
                commitData("0");
                break;
            case R.id.cancel_btn:
                commitData("1");
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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


    private void commitData(String isPass) {
        String audit_suggestion = audit_suggestion_et.getText().toString();
        if ("0".equals(isPass) && StringUtil.isEmpty(audit_suggestion)) {
            showToast("请填写审批意见");
            return;
        }
        User user = MySharepreference.getInstance(this).getUser();
        ApproveCarManageRequest request = new ApproveCarManageRequest(user.id, isPass, audit_suggestion, mEntity.id);
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), ApproveCarManageRequest.APPROVE_CAR_MANAGE_REQUEST);
        showDialog();
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        closeDialog();
        switch (action) {
            case ApproveCarManageDetailListRequest.APPROVE_CAR_MANAGE_DETAIL_LIST_REQUEST:
                mEntity = JSONObject.parseObject(json, CarMangeEntity.class);
                setData();
                break;
            case ApproveCarManageRequest.APPROVE_CAR_MANAGE_REQUEST:
                handleRequest(json);
                break;
        }
    }

    public void setData() {
        if (mEntity == null) {
            return;
        }
        car_num_et.setText(mEntity.bus_number);
        work_start_time_tv.setText(TimeDateUtil.dateTime(mEntity.start_time));
        end_time_tv.setText(TimeDateUtil.dateTime(mEntity.end_time));
        day_num_et.setText(mEntity.days);
        patner_et.setText(mEntity.peer_person);
        start_place_et.setText(mEntity.start_place);
        target_place_et.setText(mEntity.end_place);
        reason_et.setText(mEntity.use_car_desc);
        num_et.setText(mEntity.number);
        remark_et.setText(mEntity.rmark);
        if (!ListUtils.isEmpty(mEntity.attendanceAuditList)) {
            mApproveListView.setAdapter(new ApprovePeopleListAdapter(this, mEntity.attendanceAuditList));
        } else {
            mApproveListView.setVisibility(View.GONE);
        }
        if (!ListUtils.isEmpty(mEntity.attendanceCcList)) {
            copy_listview.setAdapter(new CopyPeopleListAdapter(this, mEntity.attendanceCcList));
        } else {
            copy_listview.setVisibility(View.GONE);
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
        setResult(Activity.RESULT_OK);
        finish();
    }
}
