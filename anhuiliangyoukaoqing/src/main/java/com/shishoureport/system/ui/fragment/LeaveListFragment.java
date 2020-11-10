package com.shishoureport.system.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONArray;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.AttendanceLeave;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.AttendanceLeaveListRequest;
import com.shishoureport.system.request.GetAuditListRequest;
import com.shishoureport.system.ui.activity.LeaveAppApproveActivity;
import com.shishoureport.system.ui.adapter.LeaveAdapter;
import com.shishoureport.system.utils.ListUtils;
import com.shishoureport.system.utils.MySharepreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/1.
 * copyright easybiz.
 */

public class LeaveListFragment extends AbsLoadMoreFragment implements AdapterView.OnItemClickListener {
    private PullToRefreshListView mListView;
    public static final int TYPE_LEAVE_APP_WAITE_APPROVE = 0;
    public static final int TYPE_LEAVE_APP_HAVE_APPROVE = 1;
    public static final int TYPE_I_SENDED = 2;
    public static final int TYPE_I_COPYED = 3;
    public static final int TYPE_LEAVE_APP_LIST = 5;
    public static final int TYPE_BUS_TRA_HAVE_APPROVE = 7;
    public static final int APPROVE_LEAVE_REQUEST_CODE = 10000;
    private int type = -1;

    public static LeaveListFragment getInstance(int type) {
        LeaveListFragment fragment = new LeaveListFragment();
        fragment.setType(type);
        return fragment;
    }

    private void setType(int type) {
        this.type = type;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_leave_list;
    }


    @Override
    protected void initView(View view) {
        mListView = (PullToRefreshListView) view.findViewById(R.id.listview);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView.setOnRefreshListener(this);
        mData = new ArrayList<AttendanceLeave>();
        mAdapter = new LeaveAdapter(getActivity(), mData, type);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mPager.reset();
        loadData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (TYPE_LEAVE_APP_LIST == type) {
//            LeaveAppDetailActivity.startActivity(getActivity());
        } else if (TYPE_LEAVE_APP_WAITE_APPROVE == type) {
            AttendanceLeave leave = (AttendanceLeave) mData.get(position - 1);
            LeaveAppApproveActivity.startActivityForResult(getActivity(), APPROVE_LEAVE_REQUEST_CODE, leave);
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        mListView.onRefreshComplete();
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case AttendanceLeaveListRequest.ATT_LEA_REQUEST:
            case GetAuditListRequest.GET_AUDIO_LIST_REQUEST:
                handlerequest(json);
                break;
        }
    }

    @Override
    protected PullToRefreshAdapterViewBase getRefreshView() {
        return mListView;
    }

    private void handlerequest(String json) {
        final long start = System.currentTimeMillis();
        List<AttendanceLeave> data = JSONArray.parseArray(json, AttendanceLeave.class);
        if (ListUtils.isEmpty(data)) {
            if (isVisible()){
                showToast(R.string.no_data_tip);
            }
        }
        appendData(data, start);
    }

    public void loadData() {
        User user = MySharepreference.getInstance(getActivity()).getUser();
        if (TYPE_LEAVE_APP_LIST == type) {
            AttendanceLeaveListRequest request = new AttendanceLeaveListRequest(user.id, mPager.getCount(), mPager.getPage());
            httpGetRequest(request.getRequestUrl(), AttendanceLeaveListRequest.ATT_LEA_REQUEST);
        } else if (TYPE_LEAVE_APP_WAITE_APPROVE == type) {
            GetAuditListRequest request = new GetAuditListRequest(user.id, mPager.getCount(), mPager.getPage());
            httpGetRequest(request.getRequestUrl(), GetAuditListRequest.GET_AUDIO_LIST_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == APPROVE_LEAVE_REQUEST_CODE) {
                if (mListView != null) {
                    mListView.setRefreshing(true);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
