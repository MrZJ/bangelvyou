package com.shishoureport.system.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.BusTraEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.request.ApproveBusTraRequest;
import com.shishoureport.system.request.BusTraDetailRequest;
import com.shishoureport.system.ui.adapter.ApprovePeopleListAdapter;
import com.shishoureport.system.ui.adapter.CopyPeopleListAdapter;
import com.shishoureport.system.utils.ListUtils;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.StringUtil;
import com.shishoureport.system.utils.TimeDateUtil;
import com.shishoureport.system.wibget.HorizontalListView;

import static com.shishoureport.system.ui.fragment.BusTraListFragment.TYPE_ALL_BUS_TRA_LIST;

/**
 * Created by jianzhang.
 * on 2017/9/5.
 * copyright easybiz.
 */

public class BusTraApproveActivity extends BaseActivity implements View.OnClickListener {
    public static final String KEY_BUS_TRA = "key_bus_tra";
    private TextView title_tv, leave_type_tv, travel_place_et, end_time_tv, start_time_tv,
            time_lengh_et, plane_reason_et, travel_reason_et, audit_suggestion_et, time_lengh_tip;
    private View leave_type_layout, record_tv, end_time_layout, start_time_layout, cancel_btn, ok_btn;
    private HorizontalListView mApproveListView, copy_listview;
    private BusTraEntity mEntity;

    public static void startActivityForResuslt(Activity context, BusTraEntity entity, int requestCode) {
        Intent intent = new Intent(context, BusTraApproveActivity.class);
        intent.putExtra(KEY_BUS_TRA, entity);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_approve_business_travel;
    }

    public void initView() {
        mEntity = (BusTraEntity) getIntent().getSerializableExtra(KEY_BUS_TRA);
        if (mEntity == null || mEntity.id == null) {
            return;
        }
        mApproveListView = (HorizontalListView) findViewById(R.id.approve_listview);
        copy_listview = (HorizontalListView) findViewById(R.id.copy_listview);
        findViewById(R.id.add_travel_layout).setOnClickListener(this);
        title_tv = (TextView) findViewById(R.id.title_tv);
        leave_type_layout = findViewById(R.id.leave_type_layout);
        leave_type_layout.setOnClickListener(this);
        end_time_layout = findViewById(R.id.end_time_layout);
        end_time_layout.setOnClickListener(this);
        start_time_layout = findViewById(R.id.start_time_layout);
        start_time_layout.setOnClickListener(this);
        leave_type_tv = (TextView) findViewById(R.id.leave_type_tv);
        record_tv = findViewById(R.id.record_tv);
        travel_place_et = (TextView) findViewById(R.id.travel_place_et);
        end_time_tv = (TextView) findViewById(R.id.end_time_tv);
        start_time_tv = (TextView) findViewById(R.id.start_time_tv);
        time_lengh_et = (TextView) findViewById(R.id.time_lengh_et);
        plane_reason_et = (TextView) findViewById(R.id.plane_reason_et);
        travel_reason_et = (TextView) findViewById(R.id.travel_reason_et);
        audit_suggestion_et = (TextView) findViewById(R.id.audit_suggestion_et);
        time_lengh_tip = (TextView) findViewById(R.id.time_lengh_tip);
        time_lengh_tip.setText("同行人");
        record_tv.setOnClickListener(this);
        title_tv.setText("出差报告单");
        cancel_btn = findViewById(R.id.cancel_btn);
        ok_btn = findViewById(R.id.ok_btn);
        cancel_btn.setOnClickListener(this);
        ok_btn.setOnClickListener(this);
        loadData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_tv:
                MyListActivity.startActivity(this, TYPE_ALL_BUS_TRA_LIST);
                break;
            case R.id.cancel_btn:
                commitData(ApproveBusTraRequest.NOT_PASS);
                break;
            case R.id.ok_btn:
                commitData(ApproveBusTraRequest.IS_PASS);
                break;
        }
    }


    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case BusTraDetailRequest.BUS_TRA_DETAIL_REQUEST:
                handLoadData(json);
                break;
            case ApproveBusTraRequest.APPROVE_BUS_TRA_REQUEST:
                showToast("提交成功");
                setResult(Activity.RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        switch (action) {
            case BusTraDetailRequest.BUS_TRA_DETAIL_REQUEST:
                showToast("获取数据失败，请重试");
                finish();
                break;
            case ApproveBusTraRequest.APPROVE_BUS_TRA_REQUEST:
                showToast("提交失败，请重试");
                break;
        }
    }

    private void loadData() {
        BusTraDetailRequest request = new BusTraDetailRequest(mEntity.id);
        httpGetRequest(request.getRequestUrl(), BusTraDetailRequest.BUS_TRA_DETAIL_REQUEST);
    }

    private void handLoadData(String json) {
        BusTraEntity entity = JSONObject.parseObject(json, BusTraEntity.class);
        if (entity != null) {
            travel_place_et.setText(entity.p_name);
            start_time_tv.setText(TimeDateUtil.dateTime(entity.start_time));
            end_time_tv.setText(TimeDateUtil.dateTime(entity.end_time));
            time_lengh_et.setText(entity.fellow_people);
            leave_type_tv.setText(entity.temp_travel_money);
            plane_reason_et.setText(entity.rmark);
            travel_reason_et.setText(entity.business_purpose);
            if (!ListUtils.isEmpty(entity.attendanceAuditList)) {
                mApproveListView.setAdapter(new ApprovePeopleListAdapter(this, entity.attendanceAuditList));
            } else {
                mApproveListView.setVisibility(View.GONE);
            }
            if (!ListUtils.isEmpty(entity.attendanceCcList)) {
                copy_listview.setAdapter(new CopyPeopleListAdapter(this, entity.attendanceCcList));
            } else {
                copy_listview.setVisibility(View.GONE);
            }
        }
    }

    private void commitData(String is_pass) {
        if ("0".equals(is_pass) && StringUtil.isEmpty(audit_suggestion_et.getText().toString())) {
            showToast("请填写审批意见");
            return;
        }
        ApproveBusTraRequest request = new ApproveBusTraRequest(MySharepreference.getInstance(this).getUser().id, is_pass, mEntity.id, audit_suggestion_et.getText().toString());
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), ApproveBusTraRequest.APPROVE_BUS_TRA_REQUEST);
    }
}