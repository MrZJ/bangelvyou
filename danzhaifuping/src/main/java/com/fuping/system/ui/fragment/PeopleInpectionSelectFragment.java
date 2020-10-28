package com.fuping.system.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fuping.system.R;
import com.fuping.system.adapter.AutoCompleteAdapter;
import com.fuping.system.adapter.PeopleSearchAdapter;
import com.fuping.system.adapter.PeopleSearchHistoryAdapter;
import com.fuping.system.entity.PeopleEntity;
import com.fuping.system.entity.PeoplePoorTownEntity;
import com.fuping.system.entity.SearchEntity;
import com.fuping.system.request.PeopleBaseRequest;
import com.fuping.system.request.PeopleSearchRequest;
import com.fuping.system.ui.activity.PeopleDuChaDetailActivity;
import com.fuping.system.utils.ListUtils;
import com.fuping.system.utils.TimeDateUtil;
import com.fuping.system.wibget.AdvancedAutoCompleteTextView;
import com.fuping.system.wibget.MyArrayList;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/10/27.
 * copyright easybiz.
 */

public class PeopleInpectionSelectFragment extends AbsLoadMoreFragment implements View.OnClickListener, AdapterView.OnItemClickListener, AutoCompleteAdapter.OnItemClick {
    private int mType = -1;
    private PullToRefreshListView listview;
    private TextView tips;
    private GridView searchGridView;
    private PeopleSearchHistoryAdapter mSearchAdapter;
    private AutoCompleteAdapter mAutoComAdapter;
    private AdvancedAutoCompleteTextView autoCompleteTextView;
    private MyArrayList<SearchEntity> search_history = new MyArrayList();
    private View search_iv;
    private CheckBox search_state_cb;

    public static PeopleInpectionSelectFragment getInstance(int type) {
        PeopleInpectionSelectFragment fragment = new PeopleInpectionSelectFragment();
        fragment.setmType(type);
        return fragment;
    }

    @Override
    protected PullToRefreshAdapterViewBase getRefreshView() {
        return listview;
    }

    private void setmType(int type) {
        this.mType = type;
    }

    @Override
    protected void loadData() {
        if (mSearchAdapter != null && !ListUtils.isEmpty(mSearchAdapter.getData())) {
            StringBuilder search_names = new StringBuilder();
            List<SearchEntity> data = mSearchAdapter.getData();
            for (int i = 0; i < data.size(); i++) {
                SearchEntity entity = data.get(i);
                search_names.append(entity.poorSearch_name).append(",");
            }
            String search_state = "";
            if (search_state_cb.isChecked()) {
                search_state = "0";
            } else {
                search_state = "1";
            }
            PeopleSearchRequest request = new PeopleSearchRequest(mPager.getPage(), configEntity.key, search_state, search_names.toString());
            httpGetRequest(request.getRequestUrl(), PeopleSearchRequest.PEOPLE_SEARCH_REQUEST);
        } else {
            showToast("请选择搜索项");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspection_select, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        listview = (PullToRefreshListView) view.findViewById(R.id.listview);
        mData = new ArrayList<PeopleEntity>();
        mAdapter = new PeopleSearchAdapter(getActivity(), mData);
        listview.setOnRefreshListener(this);
        listview.setOnItemClickListener(this);
        listview.setAdapter(mAdapter);
        tips = (TextView) view.findViewById(R.id.tips);
        searchGridView = (GridView) view.findViewById(R.id.search_gricview);
        search_iv = view.findViewById(R.id.search_iv);
        search_state_cb = (CheckBox) view.findViewById(R.id.search_state_cb);
        search_iv.setOnClickListener(this);
        autoCompleteTextView = (AdvancedAutoCompleteTextView) view.findViewById(R.id.search_et);
        autoCompleteTextView.setThreshold(0);
        mSearchAdapter = new PeopleSearchHistoryAdapter(search_history, getActivity());
        searchGridView.setAdapter(mSearchAdapter);
        getBaseData();
    }

    @Override
    public void onClick(View v) {
        if (v == search_iv) {
            clearData();
            loadData();
            autoCompleteTextView.hidden(getActivity());
        }
    }

    private void getBaseData() {
        PeopleBaseRequest request = new PeopleBaseRequest();
        httpGetRequest(request.getRequestUrl(), PeopleBaseRequest.PEOPLE_BASE_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case PeopleBaseRequest.PEOPLE_BASE_REQUEST:
                handleRequest(json);
                break;
            case PeopleSearchRequest.PEOPLE_SEARCH_REQUEST:
                handleRequest1(json);
                break;
        }
    }

    private void handleRequest(String json) {
        try {
            PeoplePoorTownEntity entity = JSONObject.parseObject(json, PeoplePoorTownEntity.class);
            if (entity != null && !ListUtils.isEmpty(entity.poorSearch_list)) {
                mAutoComAdapter = new AutoCompleteAdapter(getActivity(), entity.poorSearch_list, this, AutoCompleteAdapter.PEOPLE);
                autoCompleteTextView.setAdapter(mAutoComAdapter);
            } else {
                showToast("数据获取失败，请重试");
                getActivity().finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRequest1(String json) {
        try {
            PeoplePoorTownEntity entity = JSONObject.parseObject(json, PeoplePoorTownEntity.class);
            if (entity != null && !ListUtils.isEmpty(entity.inspectionPoorerList)) {
                appendData(entity.inspectionPoorerList, TimeDateUtil.time());
                tips.setText(entity.recordCount);
            } else {
                if (entity != null) {
                    tips.setText(entity.recordCount);
                }
                showToast("没有更多了");
                listview.onRefreshComplete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            tips.setText("0");
            listview.onRefreshComplete();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            PeopleEntity entity = (PeopleEntity) mData.get(position - 1);
            PeopleDuChaDetailActivity.startActivity(getActivity(), entity.poor_id);
        } catch (Exception e) {
        }
    }

    @Override
    public void onItemClick(SearchEntity entity) {
        autoCompleteTextView.hideList();
        mSearchAdapter.addData(entity);
        autoCompleteTextView.setText(entity.poorSearch_desc);
    }
}
