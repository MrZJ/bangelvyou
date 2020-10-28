package com.shishoureport.system.ui.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONArray;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.TaskEntity;
import com.shishoureport.system.request.BaseRequest;
import com.shishoureport.system.request.GetAuditTaskListRequest;
import com.shishoureport.system.request.GetSendedFileTaskListRequest;
import com.shishoureport.system.request.GetSolvedTaskListRequest;
import com.shishoureport.system.request.GetWaiteSolvedTaskListRequest;
import com.shishoureport.system.ui.activity.FileDetailActivity;
import com.shishoureport.system.ui.adapter.FileAdapter;
import com.shishoureport.system.utils.ListUtils;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.TimeDateUtil;

import java.util.ArrayList;
import java.util.List;

import static com.shishoureport.system.request.GetWaiteSolvedTaskListRequest.GET_RECEIVED_FILE_TASK_LIST_REQUEST;

/**
 * Created by jianzhang.
 * on 2017/9/1.
 * copyright easybiz.
 */

public class FileListFragment extends AbsLoadMoreFragment implements AdapterView.OnItemClickListener {
    public static final int TYPE_WTAITE_AUDIT = -1;
    public static final int TYPE_FILE_SEND = 0;
    public static final int TYPE_WAITE_SOLVED = 1;
    public static final int TYPE_FILE_REC = 2;
    public static final String ACTIPN_REFRESH_LIST = "refresh";
    private PullToRefreshListView mListView;
    private int type;

    public static FileListFragment getInstance(int type) {
        FileListFragment fragment = new FileListFragment();
        fragment.setType(type);
        return fragment;
    }

    private void setType(int type) {
        this.type = type;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_file_list;
    }


    protected void initView(View view) {
        mListView = (PullToRefreshListView) view.findViewById(R.id.listview);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView.setOnRefreshListener(this);
        mData = new ArrayList<TaskEntity>();
        mAdapter = new FileAdapter(getActivity(), mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        loadData();
    }


    @Override
    protected PullToRefreshAdapterViewBase getRefreshView() {
        return mListView;
    }

    @Override
    public void loadData() {
        BaseRequest request;
        int action = -1;
        if (type == TYPE_WTAITE_AUDIT) {
            request = new GetAuditTaskListRequest(MySharepreference.getInstance(getActivity()).getUser().id, mPager.getPage(), mPager.getCount());
            action = GetSendedFileTaskListRequest.GET_SENDED_TASK_LIST_REQUEST;
        } else if (type == TYPE_FILE_SEND) {
            request = new GetSendedFileTaskListRequest(MySharepreference.getInstance(getActivity()).getUser().id, mPager.getPage(), mPager.getCount());
            action = GetSendedFileTaskListRequest.GET_SENDED_TASK_LIST_REQUEST;
        } else if (type == TYPE_WAITE_SOLVED) {
            request = new GetWaiteSolvedTaskListRequest(MySharepreference.getInstance(getActivity()).getUser().id, mPager.getPage(), mPager.getCount());
            action = GET_RECEIVED_FILE_TASK_LIST_REQUEST;
        } else {
            request = new GetSolvedTaskListRequest(MySharepreference.getInstance(getActivity()).getUser().id, mPager.getPage(), mPager.getCount());
            action = GetSolvedTaskListRequest.GET_SOLVED_TASK_LIST_REQUEST;
        }
        httpGetRequest(request.getRequestUrl(), action);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case GetSendedFileTaskListRequest.GET_SENDED_TASK_LIST_REQUEST:
            case GET_RECEIVED_FILE_TASK_LIST_REQUEST:
            case GetSolvedTaskListRequest.GET_SOLVED_TASK_LIST_REQUEST:
                handRequest(json);
                break;
        }
    }

    private void handRequest(String json) {
        List<TaskEntity> list = JSONArray.parseArray(json, TaskEntity.class);
        if (ListUtils.isEmpty(list) && isVisible()) {
            showToast(R.string.no_data_tip);
        }
        appendData(list, TimeDateUtil.time());
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            FileDetailActivity.startActivity(getActivity(), (TaskEntity) mData.get(position - 1), type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshList() {
        if (mListView != null) {
            mListView.setRefreshing();
        }
    }
}
