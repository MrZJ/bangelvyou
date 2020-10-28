package com.basulvyou.system.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.LogisticAdapter;
import com.basulvyou.system.api.CarpoolingApi;
import com.basulvyou.system.entity.CarpoolingInfoEntity;
import com.basulvyou.system.entity.CarpoolingList;
import com.basulvyou.system.ui.activity.CarpoolingDetailActivity;
import com.basulvyou.system.util.StringUtil;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 物流信息、拼车信息
 * 我的发布和我的拼单分类信息
 */
public class MyCarpoolFragment extends AbsLoadMoreFragment<ListView, CarpoolingInfoEntity> implements AdapterView.OnItemClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private List<CarpoolingInfoEntity> carpoolingInfoEntities;
    private String orderType;//区分物流还是评测
    private String type;//区分发布还是预定

    public static Fragment getInstance(Bundle bundle) {
        MyCarpoolFragment fragment = new MyCarpoolFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_carpool, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        orderType = getArguments().getString("orderType");
        type = getArguments().getString("type");
        initListener();
        setAdapter();
        loadData();
    }

    private void initView(View v) {
        mPullToRefreshListView = (PullToRefreshListView) v.findViewById(R.id.my_carpooling_list);
    }


    private void initListener() {
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);
    }

    private void setAdapter() {
        mAdapter = new LogisticAdapter(null, getActivity(), LogisticAdapter.TYPE_2);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        if (!StringUtil.isEmpty(type) && type.equals("yd")) {
            httpGetRequest(CarpoolingApi.getCarpoolingOrderYdList(configEntity.key, mPager.rows + "", mPager.getPage() + "", orderType), CarpoolingApi.API_CAPOOLING_ORDER_LIST);
        } else {
            httpGetRequest(CarpoolingApi.getCarpoolingOrderFbList(configEntity.key, mPager.rows + "", mPager.getPage() + "", orderType), CarpoolingApi.API_CAPOOLING_ORDER_LIST);
        }
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case CarpoolingApi.API_CAPOOLING_ORDER_LIST:
                carpoolHander(json);
                break;
            default:
                break;
        }
    }

    /**
     * 整理评论数据
     *
     * @param json
     */
    private void carpoolHander(String json) {
        final long start = System.currentTimeMillis();
        CarpoolingList carpoolingList = JSON.parseObject(json, CarpoolingList.class);
        if (null != carpoolingList) {
            carpoolingInfoEntities = carpoolingList.goods_list;
            appendData(carpoolingInfoEntities, start);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter != null && mAdapter.getCount() > (position - 1)) {
            CarpoolingInfoEntity location = mAdapter.getItem(position - 1);
            if (location != null) {
                Intent intent = new Intent(getActivity(), CarpoolingDetailActivity.class);
                intent.putExtra("carpoolingInfo", location);
                intent.putExtra("type", type);
                intent.putExtra("orderType", orderType);
                startActivity(intent);
            }
        }
    }
}
