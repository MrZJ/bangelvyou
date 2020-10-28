package com.zycreview.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.zycreview.system.R;
import com.zycreview.system.adapter.FertilizerOrPesticideAdapter;
import com.zycreview.system.api.FertilizerOrPesticideApi;
import com.zycreview.system.entity.FertilizerOrPesticideEntity;
import com.zycreview.system.entity.FertilizerOrPesticideList;
import com.zycreview.system.entity.FieldErrors;
import com.zycreview.system.utils.StringUtil;
import com.zycreview.system.utils.TopClickUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 农药或者施肥搜索列表界面
 */
public class FertilizerOrPesticideActivity extends AbsLoadMoreActivity<ListView,FertilizerOrPesticideEntity> implements View.OnClickListener,OnDateSetListener,FertilizerOrPesticideAdapter.OnDeleteListener {

    private PullToRefreshListView mPullToRefreshListView;
    private String type;//是农药还是施肥界面
    private String plantType;//是种植任务还是种植历史
    private String job_no_in;
    private String job_no;
    private String typeDate;//时间是开始还是结束
    private TextView tv_head_search_date;
    private TextView startDate;
    private TextView endDate;
    private Button btQuery;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifei_or_nongyao);
        type = getIntent().getStringExtra("type");
        plantType = getIntent().getStringExtra("plantType");
        job_no_in = getIntent().getStringExtra("job_no_in");
        job_no = getIntent().getStringExtra("job_no");
        initView();
        setAdapter();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btQuery.setClickable(true);
        clearData();
        loadData();
    }

    private void initView() {
        initTopView();
        if (plantType.equals("管理")) {
            if (type.equals("施肥")) {
                setTopRightTitle("添加", TopClickUtil.TOP_ADD_FERTI);
            } else {
                setTopRightTitle("添加", TopClickUtil.TOP_ADD_PESTI);
            }
        }
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.plant_task_list);
        View searchView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.head_date_search, null);
        tv_head_search_date = (TextView) searchView.findViewById(R.id.tv_head_search_date);
        startDate = (TextView) searchView.findViewById(R.id.tv_head_search_start);
        endDate = (TextView) searchView.findViewById(R.id.tv_head_search_end);
        btQuery = (Button) searchView.findViewById(R.id.bt_head_search);
        if (type.equals("施肥")) {
            setTitle("施肥管理");
            tv_head_search_date.setText("施肥时间");
        } else {
            setTitle("喷药管理");
            tv_head_search_date.setText("喷药时间");
        }
        mPullToRefreshListView.getRefreshableView().addHeaderView(searchView);
        initViewDateDialog(this);
    }

    public void initListener() {
        super.initListener();
        initSideBarListener();
        ((FertilizerOrPesticideAdapter)mAdapter).setOnDeleteClickListener(this);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        btQuery.setOnClickListener(this);
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
    }

    private void setAdapter(){
        mAdapter = new FertilizerOrPesticideAdapter(null,this,type,plantType);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        if (plantType.equals("管理")) {
            if (type.equals("施肥")) {
                httpGetRequest(FertilizerOrPesticideApi.getFertilizerTaskUrl("" + mPager.getPage(), startDate.getText().toString(), endDate.getText().toString(), configEntity.key, job_no_in), FertilizerOrPesticideApi.API_F);
            } else {
                httpGetRequest(FertilizerOrPesticideApi.getPesticideTaskUrl("" + mPager.getPage(), startDate.getText().toString(), endDate.getText().toString(), configEntity.key, job_no_in), FertilizerOrPesticideApi.API_P);
            }
        } else {
            if (type.equals("施肥")) {
                httpGetRequest(FertilizerOrPesticideApi.getFertilizerHistoryUrl("" + mPager.getPage(), startDate.getText().toString(), endDate.getText().toString(), configEntity.key, job_no_in), FertilizerOrPesticideApi.API_F_H);
            } else {
                httpGetRequest(FertilizerOrPesticideApi.getPesticideHistoryUrl("" + mPager.getPage(), startDate.getText().toString(), endDate.getText().toString(), configEntity.key, job_no_in), FertilizerOrPesticideApi.API_P_H);
            }
        }
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case FertilizerOrPesticideApi.API_F:
            case FertilizerOrPesticideApi.API_P:
            case FertilizerOrPesticideApi.API_F_H:
            case FertilizerOrPesticideApi.API_P_H:
                hander(json);
                break;
            case FertilizerOrPesticideApi.API_F_DELETE:
            case FertilizerOrPesticideApi.API_P_DELETE:
                mData.remove(position);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        switch (action) {
            case FertilizerOrPesticideApi.API_F:
            case FertilizerOrPesticideApi.API_P:
            case FertilizerOrPesticideApi.API_F_H:
            case FertilizerOrPesticideApi.API_P_H:
                btQuery.setClickable(true);
                break;
            case FertilizerOrPesticideApi.API_F_DELETE:
            case FertilizerOrPesticideApi.API_P_DELETE:
                Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void hander(String json){
        final long start = System.currentTimeMillis();
        if(!StringUtil.isEmpty(json)){
            FertilizerOrPesticideList list = JSON.parseObject(json, FertilizerOrPesticideList.class);
            if(null != list){
                List<FertilizerOrPesticideEntity> infos = list.list;
                appendData(infos,start,btQuery);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_search_start:
                typeDate = "start";
                if(!dialog.isAdded()){
                    dialog.show(getSupportFragmentManager(),"all");
                }
                break;
            case R.id.tv_head_search_end:
                typeDate = "end";
                if(!dialog.isAdded()){
                    dialog.show(getSupportFragmentManager(),"all");
                }
                break;
            case R.id.bt_head_search://查询
                btQuery.setClickable(false);
                clearData();
                loadData();
                break;
        }
    }

    public void fertilizerIntent(){
        Intent fertilizerManageIntent = new Intent(this, FertilizerManageActivity.class);
        fertilizerManageIntent.putExtra("job_no_in", job_no_in);
        fertilizerManageIntent.putExtra("job_no", job_no);
        startActivity(fertilizerManageIntent);
    }

    public void pesticideIntent() {
        Intent pesticideManageIntent = new Intent(this, PesticideManageActivity.class);
        pesticideManageIntent.putExtra("job_no_in", job_no_in);
        pesticideManageIntent.putExtra("job_no", job_no);
        startActivity(pesticideManageIntent);
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date d = new Date(millseconds);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        if(typeDate.equals("start")){
            startDate.setText(sf.format(d));
        }else{
            endDate.setText(sf.format(d));
        }
    }

    @Override
    public void deleteItem(String id, int position) {
        this.position = position;
        if (type.equals("施肥")){
            httpGetRequest(FertilizerOrPesticideApi.getFertilizerDeteleUrl(id,configEntity.key),FertilizerOrPesticideApi.API_F_DELETE);
        } else {
            httpGetRequest(FertilizerOrPesticideApi.getPesticideDeteleUrl(id,configEntity.key),FertilizerOrPesticideApi.API_P_DELETE);
        }
    }
}
