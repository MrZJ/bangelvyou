package com.shishoureport.system.ui.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONArray;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.OverTimeEntity;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.GetOverTimeListRequest;
import com.shishoureport.system.request.GetOverTimeListRequest2;
import com.shishoureport.system.ui.adapter.OverTimeAdapter;
import com.shishoureport.system.utils.ListUtils;
import com.shishoureport.system.utils.MySharepreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/1.
 * copyright easybiz.
 */

public class OverTimeListFragment extends AbsLoadMoreFragment implements AdapterView.OnItemClickListener {
    private PullToRefreshListView mListView;
    public static final int TYPE_ALL = 1;
    public static final int TYPE_PERSON = 2;
    private int mType;

    public static OverTimeListFragment getInstance(int type) {
        OverTimeListFragment fragment = new OverTimeListFragment();
        fragment.mType = type;
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
        mData = new ArrayList<OverTimeEntity>();
        mAdapter = new OverTimeAdapter(getActivity(), mData, false);
        mListView.setAdapter(mAdapter);
        mPager.reset();
        loadData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
            case GetOverTimeListRequest.GET_OVER_TIME_LIST_REQUEST:
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
        List<OverTimeEntity> data = JSONArray.parseArray(json, OverTimeEntity.class);
        if (ListUtils.isEmpty(data)) {
            if (isVisible()) {
                showToast(R.string.no_data_tip);
            }
        }
        appendData(data, start);
    }

    public void loadData() {
        User user = MySharepreference.getInstance(getActivity()).getUser();
        if (mType == TYPE_ALL) {
            GetOverTimeListRequest request = new GetOverTimeListRequest(user.id, mPager.getCount(), mPager.getPage());
            httpGetRequest(request.getRequestUrl(), GetOverTimeListRequest.GET_OVER_TIME_LIST_REQUEST);
        } else {
            GetOverTimeListRequest2 request = new GetOverTimeListRequest2(user.id, mPager.getCount(), mPager.getPage());
            httpGetRequest(request.getRequestUrl(), GetOverTimeListRequest.GET_OVER_TIME_LIST_REQUEST);
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
