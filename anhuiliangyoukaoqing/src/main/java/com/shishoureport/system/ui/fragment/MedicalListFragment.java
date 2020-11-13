package com.shishoureport.system.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONArray;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.AuditEntity;
import com.shishoureport.system.entity.MedicalEntity;
import com.shishoureport.system.request.MedicalListRequest;
import com.shishoureport.system.ui.adapter.MedicalListAdapter;
import com.shishoureport.system.utils.ListUtils;
import com.shishoureport.system.utils.TimeDateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/7.
 * copyright easybiz.
 */

public class MedicalListFragment extends AbsLoadMoreFragment implements AdapterView.OnItemClickListener {
    public static final String KEY_MEDICAL = "key_medical";
    private PullToRefreshListView mListView;
    private boolean isReserve;

    public static MedicalListFragment getInstance(boolean isReserve) {
        MedicalListFragment fragment = new MedicalListFragment();
        fragment.isReserve = isReserve;
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_medical_list;
    }

    @Override
    protected void initView(View v) {
        mListView = (PullToRefreshListView) v.findViewById(R.id.listview);
        mData = new ArrayList<AuditEntity>();
        mAdapter = new MedicalListAdapter(getActivity(), mData, isReserve);
        mListView.setOnRefreshListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mPager.reset();
        loadData();
    }

    @Override
    protected PullToRefreshAdapterViewBase getRefreshView() {
        return mListView;
    }

    @Override
    public void loadData() {
        MedicalListRequest request1 = new MedicalListRequest(isReserve);
        httpGetRequest(request1.getRequestUrl(), MedicalListRequest.MEDICAL_LIST_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case MedicalListRequest.MEDICAL_LIST_REQUEST:
                datahandle(json);
                break;
        }
    }

    private void datahandle(String json) {
        List<MedicalEntity> list = JSONArray.parseArray(json, MedicalEntity.class);
        if (ListUtils.isEmpty(list) && isVisible()) {
            showToast(R.string.no_data_tip);
        }
        appendData(list, TimeDateUtil.millisTime());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra(KEY_MEDICAL, (MedicalEntity) mData.get(position - 1));
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
