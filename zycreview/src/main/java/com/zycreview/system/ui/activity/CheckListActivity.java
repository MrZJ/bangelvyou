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
import com.zycreview.system.adapter.CheckManageAdapter;
import com.zycreview.system.api.CheckManageApi;
import com.zycreview.system.entity.CheckTaskList;
import com.zycreview.system.entity.FieldErrors;
import com.zycreview.system.utils.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 检验管理界面和已检药材界面
 */
public class CheckListActivity extends AbsLoadMoreActivity<ListView,CheckTaskList.CheckTaskEntity> implements OnDateSetListener,View.OnClickListener {

    private EditText searchWord;
    private TextView startDate;
    private TextView endDate;
    private TextView tv_head_search_title;
    private TextView tv_head_search_date;
    private Button btQuery;
    private PullToRefreshListView mPullToRefreshListView;
    private String type;
    private String checkType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        checkType = getIntent().getStringExtra("checkType");
        initView();
        initListener();
        setAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btQuery.setClickable(true);
        clearData();
        mPager.setPage(0);
        loadData();
    }

    private void initView() {
        initTopView();
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.check_list);
        View searchView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.head_search, null);
        tv_head_search_title = (TextView) searchView.findViewById(R.id.tv_head_search_title);
        tv_head_search_date = (TextView) searchView.findViewById(R.id.tv_head_search_date);
        searchWord = (EditText) searchView.findViewById(R.id.edt_head_search_keyword);
        startDate = (TextView) searchView.findViewById(R.id.tv_head_search_start);
        endDate = (TextView) searchView.findViewById(R.id.tv_head_search_end);
        btQuery = (Button) searchView.findViewById(R.id.btn_head_search_sure);
        if (checkType.equals("检验管理")) {
            searchWord.setHint("药材名称关键字");
            setTitle("检验管理");
            tv_head_search_title.setText("药材名称");
            tv_head_search_date.setText("采收时间");
        } else {
            searchWord.setHint("检验名称关键字");
            setTitle("已检药材");
            tv_head_search_title.setText("检验名称");
            tv_head_search_date.setText("检验时间");
        }
        mPullToRefreshListView.getRefreshableView().addHeaderView(searchView);
        initViewDateDialog(this);
    }

    public void initListener() {
        super.initListener();
        initSideBarListener();
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
                clearData();
                mPager.setPage(0);
                loadData();
                break;
        }
    }

    private void setAdapter(){
        mAdapter = new CheckManageAdapter(null, this, checkType);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        if (checkType.equals("检验管理")) {
            httpPostRequest(CheckManageApi.getCheckManageListUrl(),
                    getRequestParams(),
                    CheckManageApi.API_CHECKMANAGE_LIST);
        } else {
            httpPostRequest(CheckManageApi.getCheckSeeListUrl(),
                    getRequestParams2(),
                    CheckManageApi.API_CHECKSEE_LIST);
        }
    }

    /**
     * 获取登录参数
     *
     * @return
     */
    private HashMap<String,String> getRequestParams(){
        HashMap<String,String> params = new HashMap<>();
        params.put("key",configEntity.key);
        params.put("page","6");
        params.put("mobileLogin","true");
        params.put("mod_id","5005000100");
        if (!StringUtil.isEmpty(searchWord.getText().toString()))
        params.put("drugs_name", searchWord.getText().toString());
        if (!startDate.getText().toString().equals("年/月/日"))
        params.put("st_receive_date", startDate.getText().toString());
        if (!endDate.getText().toString().equals("年/月/日"))
        params.put("en_receive_date", endDate.getText().toString());
        params.put("curpage", mPager.getPage()+"");
        return params;
    }
    /**
     * 获取登录参数
     *
     * @return
     */
    private HashMap<String,String> getRequestParams2(){
        HashMap<String,String> params = new HashMap<>();
        params.put("key",configEntity.key);
        params.put("page","6");
        params.put("mobileLogin","true");
        params.put("mod_id","5005000100");
        if (!StringUtil.isEmpty(searchWord.getText().toString()))
            params.put("name", searchWord.getText().toString());
        if (!startDate.getText().toString().equals("年/月/日"))
            params.put("st_date", startDate.getText().toString());
        if (!endDate.getText().toString().equals("年/月/日"))
            params.put("en_date", endDate.getText().toString());
        params.put("curpage", mPager.getPage()+"");
        return params;
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case CheckManageApi.API_CHECKMANAGE_LIST:
            case CheckManageApi.API_CHECKSEE_LIST:
                checkTaskHander(json);
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        switch (action) {
            case CheckManageApi.API_CHECKMANAGE_LIST:
            case CheckManageApi.API_CHECKSEE_LIST:
                btQuery.setClickable(true);
                break;
        }
    }


    private void checkTaskHander(String json){
        final long start = System.currentTimeMillis();
        if(!StringUtil.isEmpty(json)){
            CheckTaskList checkTaskList = JSON.parseObject(json, CheckTaskList.class);
            if(null != checkTaskList){
                List<CheckTaskList.CheckTaskEntity> infos = checkTaskList.list;
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
