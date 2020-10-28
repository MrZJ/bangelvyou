package com.fuping.system.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONObject;
import com.fuping.system.R;
import com.fuping.system.adapter.InstructionAdapter;
import com.fuping.system.entity.InstructionEntity;
import com.fuping.system.entity.InstructionListEntity;
import com.fuping.system.request.InstructionListRequest;
import com.fuping.system.ui.activity.InstructionDetailActivity;
import com.fuping.system.utils.ListUtils;
import com.fuping.system.utils.TimeDateUtil;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * Created by jianzhang.
 * on 2017/10/11.
 * copyright easybiz.
 */

public class InstructionFragment extends AbsLoadMoreFragment implements AdapterView.OnItemClickListener {
    public static final int IS_REPLAY = 1;
    public static final int NOT_REPLAY = 0;
    private PullToRefreshListView mListview;
    private String mId;
    private int mType = -1;

    public static InstructionFragment getInstance(int type, String id) {
        InstructionFragment fragment = new InstructionFragment();
        fragment.setmType(type, id);
        return fragment;
    }

    private void setmType(int type, String id) {
        mType = type;
        mId = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instruction, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mListview = (PullToRefreshListView) view.findViewById(R.id.listview);
        mData = new ArrayList();
        mAdapter = new InstructionAdapter(getActivity(), mData);
        mListview.setOnRefreshListener(this);
        mListview.setAdapter(mAdapter);
        mListview.setOnItemClickListener(this);
        loadData();
    }

    @Override
    protected PullToRefreshAdapterViewBase getRefreshView() {
        return mListview;
    }

    @Override
    protected void loadData() {
        InstructionListRequest request = new InstructionListRequest(mPager.getPage(), mType, mId);
        httpGetRequest(request.getRequestUrl(), InstructionListRequest.INS_LIST_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case InstructionListRequest.INS_LIST_REQUEST:
                handleRequest(json);
                break;
        }
    }

    private void handleRequest(String json) {
        try {
            InstructionListEntity entity = JSONObject.parseObject(json, InstructionListEntity.class);
            if (entity != null && !ListUtils.isEmpty(entity.instructionsinfoList)) {
                appendData(entity.instructionsinfoList, TimeDateUtil.time());
            } else {
                mListview.onRefreshComplete();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            InstructionEntity entity = (InstructionEntity) mData.get(position - 1);
            InstructionDetailActivity.startActivityForResult(getActivity(), entity, mType);
        } catch (Exception e) {

        }
    }

    public void onRefreshData() {
        clearData();
        loadData();
    }
}
