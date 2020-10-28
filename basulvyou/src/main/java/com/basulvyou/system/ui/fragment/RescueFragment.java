package com.basulvyou.system.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.AlarmRescueAdapter;
import com.basulvyou.system.api.CarpoolingApi;
import com.basulvyou.system.entity.CarpoolingInfoEntity;
import com.basulvyou.system.entity.CarpoolingList;
import com.basulvyou.system.ui.activity.RescueDetailActivity;
import com.basulvyou.system.util.ListUtils;

import java.util.List;

/**
 * 报警救援和厕所信息列表信息显示页面
 */
public class RescueFragment extends BaseFragment {

    private RecyclerView rescueRecycler;
    private String rescueTypeId;
    private String rescueType;

    public static Fragment getInstance(Bundle bundle) {
        RescueFragment fragment = new RescueFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rescue, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rescueType = getArguments().getString("type");
        rescueTypeId = getArguments().getString("typeId");
        initView(view);
        loadData();
    }

    private void initView(View view){
        rescueRecycler = (RecyclerView) view.findViewById(R.id.rec_alarm_rescue_list);
        //创建线性布局
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(RescueFragment.this.getActivity());
        //垂直方向
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        //给RecyclerView设置布局管理器
        rescueRecycler.setLayoutManager(mLayoutManager);
    }

    protected void loadData() {
        httpGetRequest(CarpoolingApi.getAlarmRescueList(rescueTypeId), CarpoolingApi.API_GET_CARPOOLING_INFO_LIST);
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action)
        {
            case CarpoolingApi.API_GET_CARPOOLING_INFO_LIST:
                carpoolingHander(json);
            default:
                break;
        }
    }

    /**
     * 处理列表信息
     * @param json
     */
    private void carpoolingHander(String json){
        CarpoolingList carpoolingList = JSON.parseObject(json, CarpoolingList.class);
        List<CarpoolingInfoEntity> carpoolingInfoEntities =null;
        if(null !=carpoolingList){
            carpoolingInfoEntities = carpoolingList.goods_list;
            if(!ListUtils.isEmpty(carpoolingInfoEntities)){
                setAdapter(carpoolingInfoEntities);
            }
        }
    }

    private void  setAdapter(final List<CarpoolingInfoEntity> carpoolingInfoEntities){
        AlarmRescueAdapter adapter = new AlarmRescueAdapter(carpoolingInfoEntities, getActivity());
        rescueRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new AlarmRescueAdapter.OnRecyclerViewItemClickListener() {

            @Override
            public void onItemClick(int postion) {
                CarpoolingInfoEntity entity = carpoolingInfoEntities.get(postion);
                Intent rescueDetailIntent = new Intent(getActivity(), RescueDetailActivity.class);
                rescueDetailIntent.putExtra("type",rescueType);
                rescueDetailIntent.putExtra("data", entity);
                startActivity(rescueDetailIntent);
            }
        });
    }

}
