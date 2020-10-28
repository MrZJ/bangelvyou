package com.shishoureport.system.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONArray;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.AuditEntity;
import com.shishoureport.system.request.AttendanceAuditListRequest;
import com.shishoureport.system.ui.adapter.AuditListAdapter;
import com.shishoureport.system.utils.ListUtils;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.TimeDateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/7.
 * copyright easybiz.
 */

public class AuditListFragment extends AbsLoadMoreFragment implements AdapterView.OnItemClickListener {
    private PullToRefreshListView mListView;
    private String mod_id;

    public static AuditListFragment getInstance(String mod_id) {
        AuditListFragment fragment = new AuditListFragment();
        fragment.setMod_id(mod_id);
        return fragment;
    }

    private void setMod_id(String mod_id) {
        this.mod_id = mod_id;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_audit_list;
    }

    @Override
    protected void initView(View v) {
        mListView = (PullToRefreshListView) v.findViewById(R.id.listview);
        mData = new ArrayList<AuditEntity>();
        mAdapter = new AuditListAdapter(getActivity(), mData, mod_id);
        mListView.setOnRefreshListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mPager.reset();
        loadData();
    }

    @Override
    protected PullToRefreshAdapterViewBase getRefreshView() {
        return mListView;
    }

    @Override
    public void loadData() {
        AttendanceAuditListRequest request1 = new AttendanceAuditListRequest(MySharepreference.getInstance(getActivity()).getUser().id, mod_id, mPager.getPage(), mPager.getCount());
        httpGetRequest(request1.getRequestUrl(), AttendanceAuditListRequest.ATTENDANCE_AUDIT_LIST_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case AttendanceAuditListRequest.ATTENDANCE_AUDIT_LIST_REQUEST:
                datahandle(json);
                break;
        }
    }

    private void datahandle(String json) {
        List<AuditEntity> list = JSONArray.parseArray(json, AuditEntity.class);
        if (ListUtils.isEmpty(list) && isVisible()) {
            showToast(R.string.no_data_tip);
        }
        appendData(list, TimeDateUtil.millisTime());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("jianzhang", "onDestroy AuditListFragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("jianzhang", "onDestroyView AuditListFragment");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("jianzhang", "onStop AuditListFragment");
    }
}
