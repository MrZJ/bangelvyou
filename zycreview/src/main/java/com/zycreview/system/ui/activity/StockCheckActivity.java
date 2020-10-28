package com.zycreview.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zycreview.system.R;
import com.zycreview.system.adapter.StockCheckAdapter;
import com.zycreview.system.api.StockApi;
import com.zycreview.system.entity.FieldErrors;
import com.zycreview.system.entity.Pager;
import com.zycreview.system.entity.StockCheckEntity;
import com.zycreview.system.entity.StockCheckList;
import com.zycreview.system.utils.ListUtils;
import com.zycreview.system.utils.StringUtil;

import java.util.List;

/**
 * 库存查询
 */
public class StockCheckActivity extends AbsLoadMoreActivity<ListView, StockCheckEntity> implements AdapterView.OnItemClickListener, View.OnClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private EditText searchWord;
    private Button commintSearch;
    private String keyWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_check);
        initView();
        initListener();
        setAdapter();
        loadData();
    }

    private void initView() {
        initTopView();
        setTitle("库存查询");
        setLeftBackButton();
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.stock_check_list);
        View searchView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.head_search, null);
        TextView title = (TextView) searchView.findViewById(R.id.tv_head_search_title);
        title.setText("药材名称:");
        searchWord = (EditText) searchView.findViewById(R.id.edt_head_search_keyword);
        TextView dateTitle = (TextView) searchView.findViewById(R.id.tv_head_search_date);
        dateTitle.setVisibility(View.GONE);
        View linearDate = searchView.findViewById(R.id.linear_head_search_date);
        linearDate.setVisibility(View.GONE);
        commintSearch = (Button) searchView.findViewById(R.id.btn_head_search_sure);
        mPullToRefreshListView.getRefreshableView().addHeaderView(searchView);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);
        commintSearch.setOnClickListener(this);
    }

    private void setAdapter() {
        mAdapter = new StockCheckAdapter(null, this);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(StockApi.getStockList(configEntity.key, mPager.getPage() + "", Pager.rows + "", keyWord), StockApi.API_STOCK_CHECK_LIST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case StockApi.API_STOCK_CHECK_LIST:
                stockHander(json);
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        switch (action) {
            case StockApi.API_STOCK_CHECK_LIST:
                commintSearch.setClickable(true);
                break;
        }
    }

    private void stockHander(String json) {
        final long start = System.currentTimeMillis();
        if (!StringUtil.isEmpty(json)) {
            StockCheckList stockList = JSON.parseObject(json, StockCheckList.class);
            if (null != stockList) {
                List<StockCheckEntity> infos = stockList.list;
                if (!ListUtils.isEmpty(infos)) {
                    appendData(infos, start, commintSearch);
                } else {
                    Toast.makeText(this, "没有数据了", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (mAdapter != null && mAdapter.getCount() > (position - 2)) {
            StockCheckEntity entity = mAdapter.getItem(position - 2);
            Intent detailIntent = new Intent(getApplicationContext(), StockCheckDetailActivity.class);
            detailIntent.putExtra("entity", entity);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_head_search_sure:
                if (!StringUtil.isEmpty(searchWord.getText().toString().trim())) {
                    keyWord = searchWord.getText().toString().trim();
                } else {
                    keyWord = null;
                }
                commintSearch.setClickable(true);
                clearData();
                loadData();
                break;
        }
    }
}
