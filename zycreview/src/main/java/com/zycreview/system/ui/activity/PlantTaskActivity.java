package com.zycreview.system.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.zycreview.system.R;
import com.zycreview.system.adapter.PlantTaskAdapter;
import com.zycreview.system.api.PlantTaskApi;
import com.zycreview.system.entity.FieldErrors;
import com.zycreview.system.entity.PlantTaskEntity;
import com.zycreview.system.entity.PlantTaskList;
import com.zycreview.system.entity.UserInfoEntity;
import com.zycreview.system.utils.CacheObjUtils;
import com.zycreview.system.utils.ListUtils;
import com.zycreview.system.utils.StringUtil;
import com.zycreview.system.utils.TopClickUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 种植任务界面和种植历史界面
 */
public class PlantTaskActivity extends AbsLoadMoreActivity<ListView,PlantTaskEntity> implements OnDateSetListener,View.OnClickListener,PlantTaskAdapter.OnDeleteListener {

    private EditText searchWord;
    private TextView startDate;
    private TextView endDate;
    private Button btQuery;
    private PullToRefreshListView mPullToRefreshListView;
    private String type;
    private String plantType;
    private int position;
    private ArrayList<UserInfoEntity> userInfoEntitys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_task);
        plantType = getIntent().getStringExtra("plantType");
        initView();
        setAdapter();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btQuery.setClickable(true);
        ((PlantTaskAdapter)mAdapter).clearMap();
        clearData();
        mPager.setPage(0);
        loadData();
    }

    private void initView() {
        initTopView();
        if (plantType.equals("管理")) {
            setTopRightTitle("添加", TopClickUtil.TOP_ADD_PLANT);
            setTitle("种植任务");
        } else {
            setTitle("种植历史");
        }
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.plant_task_list);
        View searchView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.head_search, null);
        searchWord = (EditText) searchView.findViewById(R.id.edt_head_search_keyword);
        startDate = (TextView) searchView.findViewById(R.id.tv_head_search_start);
        endDate = (TextView) searchView.findViewById(R.id.tv_head_search_end);
        btQuery = (Button) searchView.findViewById(R.id.btn_head_search_sure);
        searchWord.setHint("种植任务关键字");
        mPullToRefreshListView.getRefreshableView().addHeaderView(searchView);
        initViewDateDialog(this);
    }

    public void initListener() {
        super.initListener();
        initSideBarListener();
        ((PlantTaskAdapter)mAdapter).setOnDeleteClickListener(this);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        btQuery.setOnClickListener(this);
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.btn_head_search_sure://查询
                btQuery.setClickable(false);
                ((PlantTaskAdapter)mAdapter).clearMap();
                clearData();
                mPager.setPage(0);
                loadData();
                break;
        }
    }

    @Override
    public void deleteItem(String id,int position) {
        httpGetRequest(PlantTaskApi.getPlantTaskDeteleUrl(id,configEntity.key),PlantTaskApi.API_PLANTHISTORY_DETELE);
        this.position = position;
    }

    private void setAdapter(){
        mAdapter = new PlantTaskAdapter(null,this,plantType);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        if (plantType.equals("管理")) {
            httpGetRequest(PlantTaskApi.getPlantTaskUrl("" + mPager.getPage(),
                    searchWord.getText().toString(),
                    startDate.getText().toString(),
                    endDate.getText().toString(),
                    configEntity.key),
                    PlantTaskApi.API_PLANTTASK);
        } else {
            httpGetRequest(PlantTaskApi.getPlantHistoryUrl("" + mPager.getPage(),
                    searchWord.getText().toString(),
                    startDate.getText().toString(),
                    endDate.getText().toString(),
                    configEntity.key),
                    PlantTaskApi.API_PLANTHISTORY);
        }
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case PlantTaskApi.API_PLANTTASK:
            case PlantTaskApi.API_PLANTHISTORY:
                plantTaskHander(json);
                break;
            case PlantTaskApi.API_PLANTHISTORY_DETELE:
                mData.remove(position);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        switch (action) {
            case PlantTaskApi.API_PLANTTASK:
            case PlantTaskApi.API_PLANTHISTORY:
                btQuery.setClickable(true);
                break;
        }
    }

    private void plantTaskHander(String json){
        final long start = System.currentTimeMillis();
        if(!StringUtil.isEmpty(json)){
            PlantTaskList plantTaskList = JSON.parseObject(json, PlantTaskList.class);
            if(null != plantTaskList){
                List<PlantTaskEntity> infos = plantTaskList.list;
                userInfoEntitys = (ArrayList<UserInfoEntity>) plantTaskList.userInfoList;
                if (!ListUtils.isEmpty(userInfoEntitys)) {
                    try {
                        CacheObjUtils.saveObj(this,"person",userInfoEntitys);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    CacheObjUtils.clearObj(this,"person");
                }
                appendData(infos,start,btQuery);
            }
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
