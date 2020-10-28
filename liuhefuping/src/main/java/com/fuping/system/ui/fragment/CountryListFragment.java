package com.fuping.system.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fuping.system.R;
import com.fuping.system.adapter.CountryAdapter;
import com.fuping.system.entity.CountryDataEntity;
import com.fuping.system.entity.CountryEntity;
import com.fuping.system.request.CountryListRequest;
import com.fuping.system.ui.activity.CountryDuChaDetailActivity;
import com.fuping.system.utils.ListUtils;
import com.fuping.system.utils.StringUtil;
import com.fuping.system.utils.TimeDateUtil;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;


/**
 * Created by jianzhang.
 * on 2017/10/18.
 * copyright easybiz.
 */

public class CountryListFragment extends AbsLoadMoreFragment implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener {
    public static final int TYPE_HOME_LIST = 1;
    public static final int TYPE_DETAIL_LIST = 2;
    private PullToRefreshListView listview;
    private View img_top_goback, search_iv;
    private TextView title_tv, search_et;
    private int mType = -1;
    private String name_like;
    private String town_p_index;// 镇的区划代码 (当从镇点进去必须带这个，若是从统计图那里点过来的为空）
    private String inspection_state;//（主要用于统计图点过来的时候用，从镇级点进去可为空，督查状态，0未督查，1已督查，没有达到脱贫标准，2督查结束达到脱贫标准）;

    public static CountryListFragment getInstance(int type, String name_like, String town_p_index, String inspection_state) {
        CountryListFragment fragment = new CountryListFragment();
        fragment.setmData(type, name_like, town_p_index, inspection_state);
        return fragment;
    }

    private void setmData(int type, String name_like, String town_p_index, String inspection_state) {
        this.mType = type;
        this.name_like = name_like;
        this.town_p_index = town_p_index;
        this.inspection_state = inspection_state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country_list, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        listview = (PullToRefreshListView) view.findViewById(R.id.listview);

        img_top_goback = view.findViewById(R.id.img_top_goback);
        search_iv = view.findViewById(R.id.search_iv);
        img_top_goback.setOnClickListener(this);
        search_et = (TextView) view.findViewById(R.id.search_et);
        search_et.addTextChangedListener(this);
        search_et.setHint("请输入村名过滤");
        if (!StringUtil.isEmpty(name_like)) {
            search_et.setText(name_like);
        }
        search_iv.setOnClickListener(this);
        title_tv = (TextView) view.findViewById(R.id.top_title);
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText("贫困村");

        listview.setOnRefreshListener(this);
        listview.setOnItemClickListener(this);
        mData = new ArrayList<>();
        mAdapter = new CountryAdapter(getActivity(), mData);
        listview.setAdapter(mAdapter);
        loadData();
    }

    @Override
    protected PullToRefreshAdapterViewBase getRefreshView() {
        return listview;
    }

    @Override
    protected void loadData() {
        CountryListRequest request = new CountryListRequest(mPager.getPage(), name_like, configEntity.key, town_p_index, inspection_state);
        httpGetRequest(request.getRequestUrl(), CountryListRequest.COUNTRY_LIST_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case CountryListRequest.COUNTRY_LIST_REQUEST:
                handleRequest(json);
                break;
        }
    }

    private void handleRequest(String json) {
        try {
            CountryDataEntity countryDataEntity = JSONObject.parseObject(json, CountryDataEntity.class);
            if (countryDataEntity != null && !ListUtils.isEmpty(countryDataEntity.inspectionVillageList)) {
                appendData(countryDataEntity.inspectionVillageList, TimeDateUtil.time());
            } else {
                showToast("没有更多了");
                listview.onRefreshComplete();
            }
        } catch (Exception e) {
            listview.onRefreshComplete();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == img_top_goback) {
            getActivity().finish();
        } else if (v == search_iv) {
            if (StringUtil.isEmpty(search_et.getText().toString())) {
                showToast("请输入搜索内容");
                return;
            }
            clearData();
            name_like = search_et.getText().toString();
            loadData();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == null || StringUtil.isEmpty(s.toString())) {
            name_like = null;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            CountryEntity entity = (CountryEntity) mData.get(position - 1);
            CountryDuChaDetailActivity.startActivity(getActivity(), entity.village_id, entity.is_poor_village);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
