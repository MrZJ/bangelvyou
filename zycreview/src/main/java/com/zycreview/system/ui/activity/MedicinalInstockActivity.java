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
import com.zycreview.system.adapter.MedicinalInstockAdapter;
import com.zycreview.system.api.StockApi;
import com.zycreview.system.entity.FieldErrors;
import com.zycreview.system.entity.MedicInstockEntity;
import com.zycreview.system.entity.MedicList;
import com.zycreview.system.entity.Pager;
import com.zycreview.system.utils.ListUtils;
import com.zycreview.system.utils.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 药材入库
 */
public class MedicinalInstockActivity extends AbsLoadMoreActivity<ListView,MedicInstockEntity> implements View.OnClickListener,OnDateSetListener,MedicinalInstockAdapter.OnInstockClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private EditText searchWord;
    private TextView startDate;
    private TextView endDate;
    private String keyWord, type, startDateString, endDateString;
    private Long start, end;
    private Button commintSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicinal_instock);
        initView();
        setAdapter();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        commintSearch.setClickable(true);
        clearData();
        loadData();
    }

    private void initView(){
        initTopView();
        setTitle("入库管理");
        setLeftBackButton();
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.medicinal_instock_list);
        View searchView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.head_search, null);
        TextView title = (TextView) searchView.findViewById(R.id.tv_head_search_title);
        title.setText("药材名称:");
        searchWord = (EditText) searchView.findViewById(R.id.edt_head_search_keyword);
        TextView dateTitle = (TextView) searchView.findViewById(R.id.tv_head_search_date);
        dateTitle.setText("采收时间:");
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
        ((MedicinalInstockAdapter)mAdapter).setOnInstockClickListener(this);
    }

    private void setAdapter(){
        mAdapter = new MedicinalInstockAdapter(null,this);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(StockApi.getInstockManageList(configEntity.key, mPager.getPage() + "", Pager.rows + "", keyWord, startDateString, endDateString), StockApi.API_INSTOCK_MANAGE_LIST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case StockApi.API_INSTOCK_MANAGE_LIST:
                instockHander(json);
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        switch (action){
            case StockApi.API_INSTOCK_MANAGE_LIST:
                commintSearch.setClickable(true);
                break;
        }
    }

    private void instockHander(String json){
        final long start = System.currentTimeMillis();
        if(!StringUtil.isEmpty(json)){
            MedicList instockList = JSON.parseObject(json, MedicList.class);
            if(null != instockList){
                List<MedicInstockEntity> infos = instockList.list;
                if(ListUtils.isEmpty(infos) && mPager.getPage() ==0 ){
                    Toast.makeText(this,"暂无数据",Toast.LENGTH_SHORT).show();
                }
                appendData(infos, start, commintSearch);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_head_search_start:
                type = "start";
                if(!dialog.isAdded()){
                    dialog.show(getSupportFragmentManager(),"all");
                }
                break;
            case R.id.tv_head_search_end:
                type = "end";
                if(!dialog.isAdded()){
                    dialog.show(getSupportFragmentManager(),"all");
                }
                break;
            case R.id.btn_head_search_sure:
                if(!StringUtil.isEmpty(searchWord.getText().toString().trim())){
                    keyWord = searchWord.getText().toString().trim();
                }else{
                    keyWord = null;
                }
                commintSearch.setClickable(false);
                clearData();
                loadData();
                break;
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date d = new Date(millseconds);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        if(type.equals("start")){
            if(end != null && millseconds > end){
                Toast.makeText(getApplicationContext(), "开始时间不能晚于结束时间", Toast.LENGTH_SHORT).show();
            }else{
                start = millseconds;
                startDate.setText(sf.format(d));
                startDateString = sf.format(d);
            }
        }else{
            if(start != null && millseconds < start){
                Toast.makeText(getApplicationContext(),"结束时间不能早于开始时间",Toast.LENGTH_SHORT).show();
            }else{
                end = millseconds;
                endDate.setText(sf.format(d));
                endDateString = sf.format(d);
            }
        }
    }

    @Override
    public void ClickInstock(String id) {
        Intent intent = new Intent(this,InstockDetailActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("type","instock");
        startActivity(intent);
    }
}
