package com.shishoureport.system.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.AproveOverTimeEntity;
import com.shishoureport.system.entity.BaseDataEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.ApproveOverTimeDetailRequest;
import com.shishoureport.system.request.ApproveOverTimeRequest;
import com.shishoureport.system.ui.adapter.ApprovePeopleListAdapter;
import com.shishoureport.system.ui.adapter.CopyPeopleListAdapter;
import com.shishoureport.system.ui.adapter.WorkOverTimeDetailAdapter;
import com.shishoureport.system.utils.ListUtils;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.StringUtil;
import com.shishoureport.system.utils.TimeDateUtil;
import com.shishoureport.system.wibget.HorizontalListView;
import com.shishoureport.system.wibget.MyBaseEntityListDialog;

/**
 * Created by jianzhang.
 * on 2017/9/4.
 * copyright easybiz.
 */

public class ApproveWorkOverTimeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MyBaseEntityListDialog.OnListItemClick {
    public static final int REQUEST_CODE = 102;
    private HorizontalListView mApproveListView, copy_listview;
    private ListView listview;
    private TextView start_time_tv, over_time_reason_tv, audit_suggestion_et;
    private String mId;
    private AproveOverTimeEntity mEntity;

    public static void startActivity(Fragment context, String id) {
        Intent intent = new Intent(context.getContext(), ApproveWorkOverTimeActivity.class);
        intent.putExtra("id", id);
        context.startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_approve_work_over_time;
    }

    public void initView() {
        mId = getIntent().getStringExtra("id");
        mApproveListView = (HorizontalListView) findViewById(R.id.approve_listview);
        listview = (ListView) findViewById(R.id.listview);
        start_time_tv = (TextView) findViewById(R.id.start_time_tv);
        over_time_reason_tv = (TextView) findViewById(R.id.over_time_reason_tv);
        audit_suggestion_et = (TextView) findViewById(R.id.audit_suggestion_et);
        findViewById(R.id.cancel_btn).setOnClickListener(this);
        findViewById(R.id.ok_btn).setOnClickListener(this);
        mApproveListView.setOnItemClickListener(this);
        copy_listview = (HorizontalListView) findViewById(R.id.copy_listview);
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("加班单审批");
        ApproveOverTimeDetailRequest request = new ApproveOverTimeDetailRequest(mId);
        httpGetRequest(request.getRequestUrl(), ApproveOverTimeDetailRequest.APPROVE_OVERTIME_DETAIL_REQUEST);
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
    public void onItemclick(BaseDataEntity entity) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void commitData(String isPass) {
        String audit_suggestion = audit_suggestion_et.getText().toString();
        if ("0".equals(isPass) && StringUtil.isEmpty(audit_suggestion)) {
            showToast("请填写审批意见");
            return;
        }
        User user = MySharepreference.getInstance(this).getUser();
        ApproveOverTimeRequest request = new ApproveOverTimeRequest(user.id, isPass, audit_suggestion, mEntity.id);
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), ApproveOverTimeRequest.APPROVE_OVERTIME_REQUEST);
        showDialog();
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        closeDialog();
        switch (action) {
            case ApproveOverTimeRequest.APPROVE_OVERTIME_REQUEST:
                handleRequest(json);
                break;
            case ApproveOverTimeDetailRequest.APPROVE_OVERTIME_DETAIL_REQUEST:
                mEntity = JSONObject.parseObject(json, AproveOverTimeEntity.class);
                setData();
                break;
        }
    }

    private void setData() {
        if (mEntity == null) return;
        start_time_tv.setText(TimeDateUtil.dateTime(mEntity.overtime_date));
        over_time_reason_tv.setText(mEntity.overtime_reason);
        listview.setAdapter(new WorkOverTimeDetailAdapter(this, mEntity.attendanceOvertimeDetailList));
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
