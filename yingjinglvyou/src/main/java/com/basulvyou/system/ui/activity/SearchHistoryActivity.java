package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.TagAdapter;
import com.basulvyou.system.listener.OnTagClickListener;
import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.wibget.FlowTagLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索过渡页面
 * 显示热门搜索和搜索历史
 */
public class SearchHistoryActivity extends BaseActivity implements View.OnClickListener{

    private FlowTagLayout hotWordView,hisWordView;
    private TagAdapter hotTagAdapter,hisTagAdapter;
    private TextView clearHis;//清空历史记录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);
        initView();
        setAdapter();
        initListener();
    }

    private  void initView(){
        initTopView();
        setLeftBackButton();
        hotWordView = (FlowTagLayout) findViewById(R.id.hot_word);
        hisWordView = (FlowTagLayout) findViewById(R.id.his_word);
        clearHis = (TextView) findViewById(R.id.tv_search_history_clear_his);
    }

    private void setAdapter(){
        hotTagAdapter = new TagAdapter<>(this);
        hisTagAdapter = new TagAdapter<>(this);
        hotWordView.setAdapter(hotTagAdapter);
        hisWordView.setAdapter(hisTagAdapter);
        hotWordView.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onItemClick(FlowTagLayout parent, View view, int position) {
                Intent searchIntent = new Intent(SearchHistoryActivity.this, SearchResultActivity.class);
                searchIntent.putExtra("keyWord", parent.getAdapter().getItem(position).toString());
                startActivity(searchIntent);
            }
        });
        hisWordView.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onItemClick(FlowTagLayout parent, View view, int position) {
                Intent searchIntent = new Intent(SearchHistoryActivity.this,SearchResultActivity.class);
                searchIntent.putExtra("keyWord",parent.getAdapter().getItem(position).toString());
                startActivity(searchIntent);
            }
        });
        String hotWords[] = {"好吃的","特色","早点","酒店","特产", "门票","好玩的","好评","有名", "手工艺品","酒"};
        List<String> hotList = new ArrayList<>();
        for (int i = 0; i < hotWords.length ; i++) {
            hotList.add(hotWords[i]);
        }
        hotTagAdapter.onlyAddAll(hotList);
        configEntity = ConfigUtil.loadConfig(this);
        if(null != configEntity.searchHistory && !"".equals(configEntity.searchHistory)){
            List<String>  hisList = new ArrayList<>();
            String hisWords[] = configEntity.searchHistory.split(",");
            for (int i = 0; i <  hisWords.length; i++) {
                hisList.add(hisWords[i]);
            }
            hisTagAdapter.onlyAddAll(hisList);
        }
    }

    private void loadData(){

    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){

        }
    }

    /**
     * 处理搜索热门数据
     * @param json
     */
    private void searchhander(String json){
        String hotwrod = JSON.parseObject("hotword").toString();

    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        clearHis.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        configEntity.searchHistory = "";
        ConfigUtil.saveConfig(this,configEntity);
        hisWordView.setVisibility(View.GONE);
    }
}
