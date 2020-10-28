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
import com.shishoureport.system.entity.BusTraEntity;
import com.shishoureport.system.request.AttanceTravelListRequest;
import com.shishoureport.system.request.BusTraWaiteApproveRequest;
import com.shishoureport.system.ui.activity.BusTraApproveActivity;
import com.shishoureport.system.ui.adapter.BusTraListAdapter;
import com.shishoureport.system.utils.ListUtils;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.TimeDateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/10.
 * copyright easybiz.
 */

public class BusTraListFragment extends AbsLoadMoreFragment implements AdapterView.OnItemClickListener {
    public static final int REQUEST_CODE = 10000;
    public static final int TYPE_ALL_BUS_TRA_LIST = 4;
    public static final int TYPE_BUS_TRA_WAITE_APPROVE = 6;
    private PullToRefreshListView mListView;
    private int mType = -1;

    public static BusTraListFragment getInstance(int type) {
        BusTraListFragment fragment = new BusTraListFragment();
        fragment.setType(type);
        return fragment;
    }

    private void setType(int type) {
        mType = type;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_leave_list;
    }

    @Override
    protected void initView(View v) {
        mListView = (PullToRefreshListView) v.findViewById(R.id.listview);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView.setOnRefreshListener(this);
        mData = new ArrayList<BusTraEntity>();
        mAdapter = new BusTraListAdapter(getActivity(), mData, mType);
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
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case AttanceTravelListRequest.ATTANCE_TRAVEL_LIST_REQUEST:
            case BusTraWaiteApproveRequest.BUS_TRA_WAITE_APPROVE_REQUEST:
                handlerRequest(json);
                break;
        }
    }

    private void handlerRequest(String json) {
        List<BusTraEntity> list = JSONArray.parseArray(json, BusTraEntity.class);
        if (ListUtils.isEmpty(list) && isVisible()) {
            showToast(R.string.no_data_tip);
        }
        appendData(list, TimeDateUtil.time());
    }

    @Override
    public void loadData() {
        if (TYPE_ALL_BUS_TRA_LIST == mType) {
            AttanceTravelListRequest request = new AttanceTravelListRequest(MySharepreference.getInstance(getActivity()).getUser().id, mPager.getCount(), mPager.getPage());
            httpGetRequest(request.getRequestUrl(), AttanceTravelListRequest.ATTANCE_TRAVEL_LIST_REQUEST);
        } else if (mType == TYPE_BUS_TRA_WAITE_APPROVE) {
            BusTraWaiteApproveRequest request = new BusTraWaiteApproveRequest(MySharepreference.getInstance(getActivity()).getUser().id, mPager.getCount(), mPager.getPage());
            httpGetRequest(request.getRequestUrl(), BusTraWaiteApproveRequest.BUS_TRA_WAITE_APPROVE_REQUEST);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            BusTraEntity entity = (BusTraEntity) mData.get(position - 1);
            if (TYPE_BUS_TRA_WAITE_APPROVE == mType) {
                if (entity != null && BusTraEntity.AUDIT_PASS != entity.audit_state) {
                    BusTraApproveActivity.startActivityForResuslt(getActivity(), entity, REQUEST_CODE);
                } /*else {
                    BusTraDetailDetailActivity.startActivity(getActivity());
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                Log.e("jianzhang", "setRefreshing");
                if (mListView != null) {
                    mListView.setRefreshing(true);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("jianzhang", "onDestroy BusTraListFragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("jianzhang", "onDestroyView BusTraListFragment");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("jianzhang", "onStop BusTraListFragment");
    }
}
