package com.shishoureport.system.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONArray;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.CarMangeEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.ApproveCarManageListRequest;
import com.shishoureport.system.ui.activity.ApproveCarManageActivity;
import com.shishoureport.system.ui.adapter.CarmanageListAdapter;
import com.shishoureport.system.utils.ListUtils;
import com.shishoureport.system.utils.MySharepreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/1.
 * copyright easybiz.
 */

public class ApproveCarmanageListFragment extends AbsLoadMoreFragment implements AdapterView.OnItemClickListener {
    private PullToRefreshListView mListView;

    public static ApproveCarmanageListFragment getInstance() {
        ApproveCarmanageListFragment fragment = new ApproveCarmanageListFragment();
        return fragment;
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
        mData = new ArrayList<CarMangeEntity>();
        mAdapter = new CarmanageListAdapter(getActivity(), mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mPager.reset();
        loadData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CarMangeEntity entity = (CarMangeEntity) mData.get(position - 1);
        ApproveCarManageActivity.startActivity(this, entity.id);
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
            case ApproveCarManageListRequest.APPROVE_CARMANAGE_LIST_REQUEST:
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
        List<CarMangeEntity> data = JSONArray.parseArray(json, CarMangeEntity.class);
        if (ListUtils.isEmpty(data)) {
            if (isVisible()) {
                showToast(R.string.no_data_tip);
            }
        }
        appendData(data, start);
    }

    public void loadData() {
        User user = MySharepreference.getInstance(getActivity()).getUser();
        ApproveCarManageListRequest request = new ApproveCarManageListRequest(user.id, mPager.getCount(), mPager.getPage());
        httpGetRequest(request.getRequestUrl(), ApproveCarManageListRequest.APPROVE_CARMANAGE_LIST_REQUEST);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ApproveCarManageActivity.REQUEST_CODE) {
                clearData();
                mListView.setRefreshing();
            }
        }
    }
}