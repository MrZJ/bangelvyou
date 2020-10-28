package com.fuping.system.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONObject;
import com.fuping.system.R;
import com.fuping.system.adapter.TownAdapter;
import com.fuping.system.entity.PeoplePoorTownEntity;
import com.fuping.system.entity.TownEntity;
import com.fuping.system.request.CountryTownListRequest;
import com.fuping.system.request.PeopleTownListRequest;
import com.fuping.system.ui.activity.DuChaActivity;
import com.fuping.system.ui.activity.MyFragmentActivity;
import com.fuping.system.utils.ListUtils;
import com.fuping.system.utils.TimeDateUtil;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import static com.fuping.system.ui.activity.MyFragmentActivity.TYPE_TONGJI_COUNTRY_LIST;
import static com.fuping.system.ui.activity.MyFragmentActivity.TYPE_TONGJI_PEOPLE_COUNTRY_LIST;

/**
 * Created by jianzhang.
 * on 2017/10/18.
 * copyright easybiz.
 */

public class TownListFragment extends AbsLoadMoreFragment implements AdapterView.OnItemClickListener {
    private PullToRefreshListView listview;
    private int mType = -1;
    private String town_p_name_like;

    public static TownListFragment getInstance(int type) {
        TownListFragment fragment = new TownListFragment();
        fragment.setmType(type);
        return fragment;
    }

    private void setmType(int type) {
        this.mType = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_town_list, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        listview = (PullToRefreshListView) view.findViewById(R.id.listview);
        listview.setOnRefreshListener(this);
        mData = new ArrayList<>();
        mAdapter = new TownAdapter(getActivity(), mData);
        listview.setAdapter(mAdapter);
        listview.setOnItemClickListener(this);
        loadData();
    }

    @Override
    protected PullToRefreshAdapterViewBase getRefreshView() {
        return listview;
    }

    @Override
    protected void loadData() {
        if (mType == DuChaActivity.TYPE_COUNTRY) {
            CountryTownListRequest request = new CountryTownListRequest(mPager.getPage(), town_p_name_like, configEntity.key);
            httpGetRequest(request.getRequestUrl(), CountryTownListRequest.COUNTRY_TOWN_LIST_REQUEST);
        } else {
            PeopleTownListRequest request = new PeopleTownListRequest(mPager.getPage(), town_p_name_like, configEntity.key);
            httpGetRequest(request.getRequestUrl(), PeopleTownListRequest.PEOPLE_TOWN_LIST_REQUEST);
        }
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case PeopleTownListRequest.PEOPLE_TOWN_LIST_REQUEST:
            case CountryTownListRequest.COUNTRY_TOWN_LIST_REQUEST:
                handRequest(json);
                break;
        }
    }

    private void handRequest(String json) {
        PeoplePoorTownEntity entity = JSONObject.parseObject(json, PeoplePoorTownEntity.class);
        if (entity != null) {
            if (!ListUtils.isEmpty(entity.inspectionTownPoorerList) && mType == DuChaActivity.TYPE_PEOPLE) {
                appendData(entity.inspectionTownPoorerList, TimeDateUtil.time());
            } else if (!ListUtils.isEmpty(entity.inspectionTownVillageList) && mType == DuChaActivity.TYPE_COUNTRY) {
                appendData(entity.inspectionTownVillageList, TimeDateUtil.time());
            } else {
                showToast("没有更多了");
                listview.onRefreshComplete();
            }
        } else {
            showToast("没有更多了");
            listview.onRefreshComplete();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            if (mType == DuChaActivity.TYPE_COUNTRY) {
                TownEntity entity = (TownEntity) mData.get(position - 1);
                MyFragmentActivity.startActivity(getActivity(), TYPE_TONGJI_COUNTRY_LIST, null, entity.town_p_index, null);
            } else {
                TownEntity entity = (TownEntity) mData.get(position - 1);
                MyFragmentActivity.startActivity(getActivity(), TYPE_TONGJI_PEOPLE_COUNTRY_LIST, null, entity.town_p_index, null);
            }
        } catch (Exception e) {

        }
    }

    public void onSearch(String town_p_name_like) {
        clearData();
        this.town_p_name_like = town_p_name_like;
        loadData();
    }

    public void setSearchData(String town_p_name_like) {
        this.town_p_name_like = town_p_name_like;
    }
}
