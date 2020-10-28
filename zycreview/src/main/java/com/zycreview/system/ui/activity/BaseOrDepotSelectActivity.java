package com.zycreview.system.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zycreview.system.R;
import com.zycreview.system.adapter.BaseAndDepotAdapter;
import com.zycreview.system.entity.BaseAndDepotEntity;

/**
 * 基地或仓库选择界面
 */
public class BaseOrDepotSelectActivity extends AbsLoadMoreActivity<ListView,BaseAndDepotEntity> implements View.OnClickListener{

    private PullToRefreshListView mPullToRefreshListView;
    private EditText searchWord;
    private Button commintSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_or_depot_select);
        intiView();
        initListener();
        loadData();
    }

    private void intiView(){
        initTopView();
        setTitle("基地选择");
        setLeftBackButton();
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.base_or_depot_list);
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
        commintSearch.setOnClickListener(this);
    }

    private void serAdapter(){
        mAdapter = new BaseAndDepotAdapter(null,this);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {

    }
}
