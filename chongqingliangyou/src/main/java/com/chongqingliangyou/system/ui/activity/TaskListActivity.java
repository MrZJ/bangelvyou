package com.chongqingliangyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.chongqingliangyou.system.R;
import com.chongqingliangyou.system.adapter.TaskAdapter;
import com.chongqingliangyou.system.api.TaskApi;
import com.chongqingliangyou.system.entity.TaskEntity;
import com.chongqingliangyou.system.entity.TaskList;
import com.chongqingliangyou.system.util.StringUtil;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 任务列表页
 * 重点
 * 日常
 * 已完成
 */
public class TaskListActivity extends AbsLoadMoreActivity<ListView, TaskEntity> implements View.OnClickListener,AdapterView.OnItemClickListener{

    private PullToRefreshListView mPullToRefreshListView;
    private String taskType;
    private EditText search;
    private String keyWord = "";
    private String lastKeyWord = "";
    private  List<TaskEntity> taskEntityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        taskType = getIntent().getStringExtra("type");
        initView();
        initListener();
        setAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearData();
        showLoading("努力加载中...", true);
        loadData();
    }

    private void initView(){
        initTopView();
        if (taskType.equals("stress")){
            setTitle("重点待办任务");
        } else if (taskType.equals("daily")){
            setTitle("日常待办任务");
        } else{
            setTitle("已完成任务");
        }
        setLeftBackButton();
        search = (EditText) findViewById(R.id.edt_task_search);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.list_task);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);
        search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edt_task_search:
                if(!StringUtil.isEmpty(search.getText().toString().trim())){
                    keyWord = search.getText().toString().trim();
                    if(!keyWord.equals(lastKeyWord)){
                        lastKeyWord = keyWord;
                        clearData();
                        showLoading("努力加载中...", true);
                        loadData();
                    }
                }else{
                    keyWord = "";
                    lastKeyWord = "";
                }
                break;
        }
    }

    private void setAdapter(){
        mAdapter = new TaskAdapter(null,TaskListActivity.this);
        mPullToRefreshListView.getRefreshableView().setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        if (taskType.equals("stress")){
           httpGetRequest(TaskApi.getTaskListUrl("2",mPager.rows + "",mPager.getPage() + "",null,keyWord,configEntity.key),TaskApi.API_TASK_LIST);
        } else if (taskType.equals("daily")){
            httpGetRequest(TaskApi.getTaskListUrl("1", mPager.rows + "", mPager.getPage() + "", null, keyWord,configEntity.key), TaskApi.API_TASK_LIST);
        } else {
            ((TaskAdapter) mAdapter).setShowChangeBtn(false);
            httpGetRequest(TaskApi.getTaskListUrl(null, mPager.rows + "", mPager.getPage() + "", "30", keyWord, configEntity.key), TaskApi.API_TASK_LIST);
        }
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action)
        {
            case  TaskApi.API_TASK_LIST:
                taskHander(json);
                break;
            default:
                break;
        }
    }

    /**
     * 整理评论数据
     * @param json
     */
    private void taskHander(String json){
        final long start = System.currentTimeMillis();
        TaskList taskList = JSON.parseObject(json, TaskList.class);
        if(null != taskList){
            taskEntityList = taskList.datasList;
            appendData(taskEntityList, start);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position -= 1;
        if(mAdapter != null && mAdapter.getCount() > position){
            TaskEntity taskEntity = mAdapter.getItem(position);
            if(!StringUtil.isEmpty(taskEntity.id)){
                Intent taskDetailIntent = new Intent(this,TaskDetailActivity.class);
                taskDetailIntent.putExtra("id",taskEntity.id);
                startActivity(taskDetailIntent);
            }
        }
    }

}
