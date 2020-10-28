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
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.zycreview.system.R;
import com.zycreview.system.adapter.SubPackageAdapter;
import com.zycreview.system.entity.SubPackageEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 分包管理
 * 列表数据显示
 */
public class SubPackageActivity extends AbsLoadMoreActivity<ListView,SubPackageEntity> implements View.OnClickListener,OnDateSetListener{

    private PullToRefreshListView mPullToRefreshListView;
    private EditText searchWord;
    private Button commintSearch;
    private TextView startDate;
    private TextView endDate;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_package);
        initView();
        initListener();
        setAdapter();
        loadData();
    }

    private void initView(){
        initTopView();
        setTitle("药材分包");
        setLeftBackButton();
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.sub_package_list);
        View searchView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.head_search, null);
        TextView title = (TextView) searchView.findViewById(R.id.tv_head_search_title);
        title.setText("药材名称:");
        searchWord = (EditText) searchView.findViewById(R.id.edt_head_search_keyword);
        TextView dateTitle = (TextView) searchView.findViewById(R.id.tv_head_search_date);
        dateTitle.setText("入库时间:");
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
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
    }

    private void setAdapter(){
        mAdapter = new SubPackageAdapter(null,this);
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
        switch (v.getId()){
            case R.id.tv_head_search_start:
                type = "start";
                dialog.show(getSupportFragmentManager(),"all");
                break;
            case R.id.tv_head_search_end:
                type = "end";
                dialog.show(getSupportFragmentManager(),"all");
                break;
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date d = new Date(millseconds);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        if(type.equals("start")){
            startDate.setText(sf.format(d));
        }else{
            endDate.setText(sf.format(d));
        }
    }
}
