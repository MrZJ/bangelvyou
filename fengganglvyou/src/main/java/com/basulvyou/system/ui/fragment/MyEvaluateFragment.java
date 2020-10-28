package com.basulvyou.system.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.EvaluateAdapter;
import com.basulvyou.system.api.CarpoolingApi;
import com.basulvyou.system.entity.CommentItem;
import com.basulvyou.system.entity.CommentRes;
import com.basulvyou.system.entity.FieldErrors;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import static com.basulvyou.system.api.CarpoolingApi.ACTION_GET_RELEASE_EVALUATE;

/**
 * Created by jianzhang.
 * on 2017/6/8.
 * copyright easybiz.
 */

public class MyEvaluateFragment extends AbsLoadMoreFragment<ListView, CommentItem> implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static final int RELEASED_EVALUATE = 1;
    public static final int RECEIVED_EVALUATE = 2;
    public static final String KEY_TYPE = "type";
    private PullToRefreshListView mPullToRefreshListView;
    private TextView good_count_tv, ok_count_tv, bad_count_tv;
    private RadioButton good_rbtn, ok_rbtn, bad_rbtn;
    private View good_count_layout, ok_count_layout, bad_count_layout;
    private ProgressBar progressBar;
    private String comm_score = "5";
    private int pos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_evaluate, null);
        pos = getArguments().getInt(KEY_TYPE);
        intView(view);
        return view;
    }

    public MyEvaluateFragment getInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        setArguments(bundle);
        return this;
    }

    private void intView(View view) {
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.evaluate_list);
        good_count_tv = (TextView) view.findViewById(R.id.good_count_tv);
        good_count_tv.setOnClickListener(this);
        good_count_layout = view.findViewById(R.id.good_count_layout);
        good_count_layout.setOnClickListener(this);
        ok_count_tv = (TextView) view.findViewById(R.id.ok_count_tv);
        ok_count_tv.setOnClickListener(this);
        ok_count_layout = view.findViewById(R.id.ok_count_layout);
        ok_count_layout.setOnClickListener(this);
        bad_count_tv = (TextView) view.findViewById(R.id.bad_count_tv);
        bad_count_tv.setOnClickListener(this);
        bad_count_layout = view.findViewById(R.id.bad_count_layout);
        bad_count_layout.setOnClickListener(this);
        good_rbtn = (RadioButton) view.findViewById(R.id.good_rbtn);
        good_rbtn.setOnClickListener(this);
        ok_rbtn = (RadioButton) view.findViewById(R.id.ok_rbtn);
        ok_rbtn.setOnClickListener(this);
        bad_rbtn = (RadioButton) view.findViewById(R.id.bad_rbtn);
        bad_rbtn.setOnClickListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        setAdapter();
        loadData();
    }

    private void setAdapter() {
        mAdapter = new EvaluateAdapter(null, getActivity());
        mPullToRefreshListView.setAdapter(mAdapter);
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setOnItemClickListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        showProgress();
        httpGetRequest(CarpoolingApi.getReleasedEvaluateList(comm_score, configEntity.key, mPager.getPage() + "", mPager.rows + "", pos), ACTION_GET_RELEASE_EVALUATE);
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        dismissProgress();
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        dismissProgress();
        switch (action) {
            case ACTION_GET_RELEASE_EVALUATE:
                resHander(json);
                break;
        }
    }

    /**
     * 处理我的订单信息
     *
     * @param json
     */
    private void resHander(String json) {
        List<CommentItem> commentItemList = new ArrayList<>();
        final long start = System.currentTimeMillis();
        CommentRes commentRes = JSON.parseObject(json, CommentRes.class);
        if (commentRes != null) {
            commentItemList = commentRes.comment_list;
            good_count_tv.setText(commentRes.counthp);
            ok_count_tv.setText(commentRes.countzp);
            bad_count_tv.setText(commentRes.countcp);
        }
        appendData(commentItemList, start);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.good_count_tv:
            case R.id.good_count_layout:
            case R.id.good_rbtn:
                setButtonStatusAndRefresh(0);
                break;
            case R.id.ok_count_layout:
            case R.id.ok_count_tv:
            case R.id.ok_rbtn:
                setButtonStatusAndRefresh(1);
                break;
            case R.id.bad_count_layout:
            case R.id.bad_count_tv:
            case R.id.bad_rbtn:
                setButtonStatusAndRefresh(2);
                break;
        }
    }

    private void showProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void dismissProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setButtonStatusAndRefresh(int p) {
        switch (p) {
            case 0:
                good_rbtn.setChecked(true);
                good_rbtn.setSelected(true);
                ok_rbtn.setChecked(false);
                ok_rbtn.setSelected(false);
                bad_rbtn.setChecked(false);
                bad_rbtn.setSelected(false);
                bad_rbtn.setFocusable(false);
                good_rbtn.setFocusable(false);
                ok_rbtn.setFocusable(false);
                comm_score = "5";
                break;
            case 1:
                good_rbtn.setChecked(false);
                good_rbtn.setSelected(false);
                ok_rbtn.setChecked(true);
                ok_rbtn.setSelected(true);
                bad_rbtn.setChecked(false);
                bad_rbtn.setSelected(false);
                bad_rbtn.setFocusable(false);
                good_rbtn.setFocusable(false);
                ok_rbtn.setFocusable(false);
                comm_score = "3";
                break;
            case 2:
                good_rbtn.setChecked(false);
                good_rbtn.setSelected(false);
                ok_rbtn.setChecked(false);
                ok_rbtn.setSelected(false);
                bad_rbtn.setChecked(true);
                bad_rbtn.setSelected(true);
                bad_rbtn.setFocusable(false);
                good_rbtn.setFocusable(false);
                ok_rbtn.setFocusable(false);
                comm_score = "1";
                break;
        }
        if (mAdapter != null && mAdapter instanceof EvaluateAdapter) {
            EvaluateAdapter adapter = (EvaluateAdapter) mAdapter;
            adapter.setComm_score(comm_score);
        }
        clearData();
        onPullUpToRefresh(mPullToRefreshListView);
    }
}
