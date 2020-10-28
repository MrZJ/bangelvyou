package com.shishoureport.system.ui.fragment;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.AttandenceCountMap;
import com.shishoureport.system.entity.AttendanceItem;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.ParentItem;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.GetAttandenceCountRequest;
import com.shishoureport.system.ui.adapter.CountExpandableAdapter;
import com.shishoureport.system.utils.FrescoHelper;
import com.shishoureport.system.utils.ListUtils;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.wibget.YearMonthPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by jianzhang.
 * on 2017/8/29.
 * copyright easybiz.
 */

public class CountFragment extends BaseFragment implements View.OnClickListener, YearMonthPickerDialog.OnButtonClick {
    private ExpandableListView mExpandabeListView;
    private TextView name_tv, position_tv, time_tv;
    private SimpleDraweeView photo_sd;
    private User user;
    private int mYear, mMonth;
    private Calendar c;
    private List<ParentItem> parentList = new ArrayList<ParentItem>();
    private List<List<AttendanceItem>> childList = new ArrayList<>();
    private CountExpandableAdapter mAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_count;
    }

    protected void initView(View view) {
        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        user = MySharepreference.getInstance(getActivity()).getUser();
        if (user == null) {
            getActivity().finish();
            return;
        }
        mExpandabeListView = (ExpandableListView) view.findViewById(R.id.expand_listview);
        mExpandabeListView.setGroupIndicator(null);
        mExpandabeListView.setChildIndicator(null);
        mAdapter = new CountExpandableAdapter(getActivity(), parentList, childList);
        mExpandabeListView.setAdapter(mAdapter);
        photo_sd = (SimpleDraweeView) view.findViewById(R.id.photo_sd);
        name_tv = (TextView) view.findViewById(R.id.name_tv);
        position_tv = (TextView) view.findViewById(R.id.position_tv);
        time_tv = (TextView) view.findViewById(R.id.time_tv);
        time_tv.setOnClickListener(this);
        FrescoHelper.loadImage(user.positions, photo_sd);
        name_tv.setText(user.user_name);
        position_tv.setText(user.dept_name);
        time_tv.setText(mYear + "年" + mMonth + "月");
        loadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_tv:
                YearMonthPickerDialog dialog = new YearMonthPickerDialog(getActivity(), "选择时间", this);
                dialog.show();
                break;
        }
    }

    public void onOkClick(String date, int year, int month) {
        time_tv.setText(date);
        mMonth = month;
        mYear = year;
        loadData();
    }

    @Override
    public void onCancelClick() {

    }

    public void loadData() {
        GetAttandenceCountRequest request = new GetAttandenceCountRequest(user.id, mYear, mMonth);
        httpGetRequest(request.getRequestUrl(), GetAttandenceCountRequest.GET_ATTANDENCE_COUNT_REQUEST);
    }


    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case GetAttandenceCountRequest.GET_ATTANDENCE_COUNT_REQUEST:
                handlerRequest(json);
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        parentList.clear();
        childList.clear();
        mAdapter.notifyDataSetChanged();
    }

    private void handlerRequest(String json) {
        parentList.clear();
        childList.clear();
        AttandenceCountMap map = JSONObject.parseObject(json, AttandenceCountMap.class);
        if (map == null || map.map == null) {
            showToast("没有数据");
            mAdapter.notifyDataSetChanged();
            return;
        }
        if (!ListUtils.isEmpty(map.map.attendanceSheetAttendanceList)) {
            parentList.add(new ParentItem("出勤天数", map.attendance_days + "天"));
            childList.add(map.map.attendanceSheetAttendanceList);
        }
        if (!ListUtils.isEmpty(map.map.attendanceSheetLateList)) {
            parentList.add(new ParentItem("迟到", map.late_count + "次"));
            childList.add(map.map.attendanceSheetLateList);
        }
        if (!ListUtils.isEmpty(map.map.attendanceSheetLeaveList)) {
            parentList.add(new ParentItem("早退", map.leave_count + "次"));
            childList.add(map.map.attendanceSheetLeaveList);
        }
        if (!ListUtils.isEmpty(map.map.attendanceSheetMissingList)) {
            parentList.add(new ParentItem("缺卡", map.missing_card_count + "次"));
            childList.add(map.map.attendanceSheetMissingList);
        }
        if (!ListUtils.isEmpty(map.map.attendanceSheetOutworkList)) {
            parentList.add(new ParentItem("外勤", map.outwork_count + "次"));
            childList.add(map.map.attendanceSheetOutworkList);
        }
        mAdapter.notifyDataSetChanged();
    }
}
