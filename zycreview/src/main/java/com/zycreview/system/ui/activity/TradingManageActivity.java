package com.zycreview.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.zycreview.system.R;
import com.zycreview.system.adapter.TradingAdapter;
import com.zycreview.system.api.TradApi;
import com.zycreview.system.entity.EntpEntity;
import com.zycreview.system.entity.FieldErrors;
import com.zycreview.system.entity.Pager;
import com.zycreview.system.entity.TradingEntity;
import com.zycreview.system.entity.TradingList;
import com.zycreview.system.entity.UserInfoEntity;
import com.zycreview.system.utils.StringUtil;
import com.zycreview.system.utils.TopClickUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 交易单管理
 * 正在生成
 */
public class TradingManageActivity extends AbsLoadMoreActivity<ListView, TradingEntity> implements TradingAdapter.OnCheckListener, OnDateSetListener, View.OnClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private EditText searchWord;
    private Button commintSearch;
    private String keyWord, type, startDateString, endDateString;
    private Long start, end;
    private TextView startDate;
    private TextView endDate;
    private HashMap<String, String> checkEd;
    private List<EntpEntity> entpInfos;//采购企业信息
    private List<UserInfoEntity> userInfos;//采购人信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading_manage);
        initView();
        setAdapter();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkEd = new HashMap<>();
        commintSearch.setClickable(true);
        ((TradingAdapter) mAdapter).clearMap();
        clearData();
        loadData();
    }

    private void initView() {
        initTopView();
        setTitle("交易单生成");
        setLeftBackButton();
        setTopRightTitle("提交", TopClickUtil.TOP_ADD_TRADING);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.trading_list);
        View searchView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.head_search, null);
        TextView title = (TextView) searchView.findViewById(R.id.tv_head_search_title);
        title.setText("药材名称:");
        searchWord = (EditText) searchView.findViewById(R.id.edt_head_search_keyword);
        TextView dateTitle = (TextView) searchView.findViewById(R.id.tv_head_search_date);
        View dateSearchView = searchView.findViewById(R.id.linear_head_search_date);
        dateSearchView.setVisibility(View.GONE);
        dateTitle.setText("有效期:");
        dateTitle.setVisibility(View.GONE);
        startDate = (TextView) searchView.findViewById(R.id.tv_head_search_start);
        endDate = (TextView) searchView.findViewById(R.id.tv_head_search_end);
        commintSearch = (Button) searchView.findViewById(R.id.btn_head_search_sure);
        mPullToRefreshListView.getRefreshableView().addHeaderView(searchView);
        initViewDateDialog(this);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        ((TradingAdapter) mAdapter).setOnCheckListener(this);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        commintSearch.setOnClickListener(this);
    }

    private void setAdapter() {
        mAdapter = new TradingAdapter(null, this);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(TradApi.getTradingList(configEntity.key, mPager.getPage() + "", Pager.rows + "", keyWord, startDateString, endDateString), TradApi.API_TRADING_LIST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case TradApi.API_TRADING_LIST:
                tradingHander(json);
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        switch (action) {
            case TradApi.API_TRADING_LIST:
                commintSearch.setClickable(true);
                break;
        }
    }

    private void tradingHander(String json) {
        final long start = System.currentTimeMillis();
        if (!StringUtil.isEmpty(json)) {
            TradingList tradList = JSON.parseObject(json, TradingList.class);
            if (null != tradList) {
                List<TradingEntity> infos = tradList.list;
                if (mPager.getPage() == 0) {
                    entpInfos = tradList.entp_list;
                    userInfos = tradList.user_list;
                }
                appendData(infos, start, commintSearch);
            }
        }
    }


    @Override
    public void addTrading(String id, String price, String outWeight) {
        if (!StringUtil.isEmpty(price)) {
            checkEd.put(id, id + "/" + price + "/" + outWeight);
        }
    }

    @Override
    public void removeTrading(String id) {
        checkEd.remove(id);
    }

    public void addTrading() {
        StringBuffer checheckedString = new StringBuffer();
        Iterator iter = checkEd.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            String val = checkEd.get(key);
            checheckedString.append(val + ",");
        }
        if (StringUtil.isEmpty(checheckedString.toString())) {
            Toast.makeText(this, "请添加交易单", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, TradingAddActivity.class);
            intent.putExtra("trading", checheckedString.toString());
            intent.putExtra("entps", (Serializable) entpInfos);
            intent.putExtra("userInfos", (Serializable) userInfos);
            startActivity(intent);
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date d = new Date(millseconds);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        if (type.equals("start")) {
            if (end != null && millseconds > end) {
                Toast.makeText(getApplicationContext(), "开始时间不能晚于结束时间", Toast.LENGTH_SHORT).show();
            } else {
                start = millseconds;
                startDate.setText(sf.format(d));
                startDateString = sf.format(d);
            }
        } else {
            if (start != null && millseconds < start) {
                Toast.makeText(getApplicationContext(), "结束时间不能早于开始时间", Toast.LENGTH_SHORT).show();
            } else {
                end = millseconds;
                endDate.setText(sf.format(d));
                endDateString = sf.format(d);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_search_start:
                type = "start";
                if (!dialog.isAdded()) {
                    dialog.show(getSupportFragmentManager(), "all");
                }
                break;
            case R.id.tv_head_search_end:
                type = "end";
                if (!dialog.isAdded()) {
                    dialog.show(getSupportFragmentManager(), "all");
                }
                break;
            case R.id.btn_head_search_sure:
                if (!StringUtil.isEmpty(searchWord.getText().toString().trim())) {
                    keyWord = searchWord.getText().toString().trim();
                } else {
                    keyWord = null;
                }
                commintSearch.setClickable(false);
                checkEd = new HashMap<>();
                ((TradingAdapter) mAdapter).clearMap();
                clearData();
                loadData();
                break;
        }
    }
}
