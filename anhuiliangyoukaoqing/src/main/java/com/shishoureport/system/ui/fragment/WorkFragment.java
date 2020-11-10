package com.shishoureport.system.ui.fragment;

import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.HomeCountData;
import com.shishoureport.system.request.HomeCountRequest;
import com.shishoureport.system.ui.activity.ApplyPurchaseActivity;
import com.shishoureport.system.ui.activity.ApplyUseActivity;
import com.shishoureport.system.ui.activity.ApplyWorkerActivity;
import com.shishoureport.system.ui.activity.ApproveActivity;
import com.shishoureport.system.ui.activity.AttendanceActivity;
import com.shishoureport.system.ui.activity.BusinessTravelActivity;
import com.shishoureport.system.ui.activity.CarManageActivity;
import com.shishoureport.system.ui.activity.HaveApprovedActivity;
import com.shishoureport.system.ui.activity.LeaveActivity;
import com.shishoureport.system.ui.activity.MyListActivity;
import com.shishoureport.system.ui.activity.PersonnalWorkOverTimeActivity;
import com.shishoureport.system.ui.activity.ScanActivity;
import com.shishoureport.system.ui.activity.WorkOverTimeActivity;
import com.shishoureport.system.ui.adapter.WorkAdapter;
import com.shishoureport.system.utils.MySharepreference;

/**
 * Created by jianzhang.
 * on 2017/5/25.
 * copyright easybiz.
 */

public class WorkFragment extends BaseFragment implements View.OnClickListener, WorkAdapter.WorkItemClick {
    private GridView mGridView;
    private FileFragmet fileFragmet;
    private TextView read_count_tv, work_count_tv, solved_tv;
    private boolean is_can_punch;

    public static WorkFragment getInstance(FileFragmet fileFragmet) {
        WorkFragment fragment = new WorkFragment();
        fragment.fileFragmet = fileFragmet;
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_work;
    }

    @Override
    protected void initView(View v) {
        hideRightSide();
        setLeftBackButton(false);
        setTopTitle("首页");
        mGridView = (GridView) v.findViewById(R.id.gv);
        mGridView.setAdapter(new WorkAdapter(getActivity(), this));
        read_count_tv = (TextView) v.findViewById(R.id.read_count_tv);
        work_count_tv = (TextView) v.findViewById(R.id.work_count_tv);
        solved_tv = (TextView) v.findViewById(R.id.solved_tv);
        solved_tv.setOnClickListener(this);
        read_count_tv.setOnClickListener(this);
        work_count_tv.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.read_count_tv:
                ApproveActivity.startActivity(getActivity());
                break;
            case R.id.work_count_tv:
                loadData();
                MyListActivity.startActivity(getActivity(), MyListActivity.TYPE_ATTANDENCE_COUNT);
                break;
            case R.id.solved_tv:
                MyListActivity.startActivity(getActivity(), MyListActivity.TYPE_FILE_SOLVED_LIST);
                break;
        }

    }

    @Override
    public void onItemClick(int pos) {
        switch (pos) {
            case 0:
                if (is_can_punch) {
                    AttendanceActivity.startActivity(getActivity());
                } else {
                    showToast("您没有打卡权限！");
                }
                break;
            case 1:
                LeaveActivity.startActivity(getActivity());
                break;
            case 2:
                BusinessTravelActivity.startActivity(getActivity());
                break;
            case 3:
                HaveApprovedActivity.startActivity(getActivity());
                break;
            case 4:
                WorkOverTimeActivity.startActivity(getActivity());
                break;
            case 5:
                PersonnalWorkOverTimeActivity.startActivity(getContext());
                break;
            case 6:
                CarManageActivity.startActivity(getContext());
                break;
            case 7:
                ScanActivity.startActivity(getActivity());
                break;
            case 8:
                ApplyWorkerActivity.startActivity(getContext());
                break;
            case 9:
                ApplyPurchaseActivity.startActivity(getContext());
                break;
            case 10:
                ApplyUseActivity.startActivity(getActivity());
                break;
        }
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case HomeCountRequest.HOME_COUNT_REQUEST:
                HomeCountData data = JSONObject.parseObject(json, HomeCountData.class);
                if (data != null && data.map != null) {
                    read_count_tv.setText(data.map.stayAudit);
                    work_count_tv.setText(data.map.countLeave);
                    solved_tv.setText(data.map.countTask);
                    if ("1".equals(data.map.is_can_punch)) {
                        is_can_punch = true;
                    } else {
                        is_can_punch = false;
                    }
                    if (fileFragmet != null) {
                        if ("1".equals(data.map.is_can_audit)) {
                            fileFragmet.setCheckVisbal(View.VISIBLE);
                        } else {
                            fileFragmet.setCheckVisbal(View.GONE);
                        }
                    }
                }
                break;
        }
    }

    public void loadData() {
        HomeCountRequest request = new HomeCountRequest(MySharepreference.getInstance(getActivity()).getUser().id);
        httpGetRequest(request.getRequestUrl(), HomeCountRequest.HOME_COUNT_REQUEST);
    }
}
