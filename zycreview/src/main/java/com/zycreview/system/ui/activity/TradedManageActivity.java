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
import com.zycreview.system.adapter.TradedAdapter;
import com.zycreview.system.api.TradApi;
import com.zycreview.system.entity.FieldErrors;
import com.zycreview.system.entity.Pager;
import com.zycreview.system.entity.TestPerson;
import com.zycreview.system.entity.TradedEntity;
import com.zycreview.system.entity.TradedList;
import com.zycreview.system.utils.ListUtils;
import com.zycreview.system.utils.StringUtil;
import com.zycreview.system.utils.TopClickUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 交易单管理界面
 * 已生成
 */
public class TradedManageActivity extends AbsLoadMoreActivity<ListView, TradedEntity> implements View.OnClickListener, OnDateSetListener, TradedAdapter.OnTradedBottomCheckListener {

    public static final String TEST_PERSION = "test_persion";
    private PullToRefreshListView mPullToRefreshListView;
    private EditText searchWord;
    private Button commintSearch;
    private String keyWord, type, startDateString, endDateString;
    private Long start, end;
    private TextView startDate;
    private TextView endDate;
    private ArrayList<TestPerson> testPersonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traded_manage);
        initView();
        setAdapter();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((TradedAdapter) mAdapter).clearMap();
        clearData();
        loadData();
    }

    private void initView() {
        initTopView();
        setTitle("交易单管理");
        setLeftBackButton();
        setTopRightTitle("添加", TopClickUtil.TOP_TRADING);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.traded_list);
        View searchView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.head_search, null);
        TextView title = (TextView) searchView.findViewById(R.id.tv_head_search_title);
        title.setText("采购企业名称:");
        searchWord = (EditText) searchView.findViewById(R.id.edt_head_search_keyword);
        TextView dateTitle = (TextView) searchView.findViewById(R.id.tv_head_search_date);
        dateTitle.setText("交易时间:");
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
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        commintSearch.setOnClickListener(this);
        ((TradedAdapter) mAdapter).setOnTradedBottomClickListener(this);
    }

    private void setAdapter() {
        mAdapter = new TradedAdapter(null, this);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(TradApi.getTradedList(configEntity.key, mPager.getPage() + "", Pager.rows + "", keyWord, startDateString, endDateString), TradApi.API_TRADED_LIST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case TradApi.API_TRADED_LIST:
                tradedHander(json);
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        switch (action) {
            case TradApi.API_TRADED_LIST:
                commintSearch.setClickable(true);
                break;
        }
    }

    private void tradedHander(String json) {
        final long start = System.currentTimeMillis();
        if (!StringUtil.isEmpty(json)) {
            TradedList tradList = JSON.parseObject(json, TradedList.class);
            testPersonList = tradList.user_list;
            if (null != tradList) {
                if (!ListUtils.isEmpty(tradList.list)) {
                    List<TradedEntity> infos = tradList.list;
                    appendData(infos, start, commintSearch);
                } else {
                    Toast.makeText(this, "没有数据了", Toast.LENGTH_SHORT).show();
                }
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
                ((TradedAdapter) mAdapter).clearMap();
                clearData();
                loadData();
                break;
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
    public void checkTradedDetail(TradedEntity entity) {
        Intent updateIntent = new Intent(this, TradedDetailOrChangeStateActivity.class);
        updateIntent.putExtra("type", "detail");
        updateIntent.putExtra("entity", entity);
        updateIntent.putExtra(TEST_PERSION, testPersonList);
        startActivity(updateIntent);
    }

    @Override
    public void updateTraded(TradedEntity entity) {
        Intent updateIntent = new Intent(this, TradedDetailOrChangeStateActivity.class);
        updateIntent.putExtra("type", "update");
        updateIntent.putExtra("entity", entity);
        updateIntent.putExtra(TEST_PERSION, testPersonList);
        startActivity(updateIntent);
    }

    @Override
    public void tradedDurgs(String id) {
        if (!StringUtil.isEmpty(id)) {
            Intent intent = new Intent(this, TradedDrugsActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }
}
