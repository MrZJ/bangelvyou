package com.shenmai.system.ui.fragment;

import android.text.format.DateUtils;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.shenmai.system.adapter.AbsBaseAdapter;
import com.shenmai.system.entity.Pager;
import com.shenmai.system.utlis.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/7.
 */
public abstract class AbsScrollViewLoadMoreFragment<T> extends BaseFragment
        implements PullToRefreshBase.OnRefreshListener<ScrollView> {

    protected abstract PullToRefreshScrollView getRefreshView();

    protected abstract void loadData();

    protected Pager mPager = new Pager();

    protected List<T> mData;

    protected AbsBaseAdapter<T> mAdapter;

    public void appendData(final List<T> infos, final long start,final boolean isCache) {

        new Thread() {
            public void run() {
                long end = System.currentTimeMillis();
                if (end - start < 500) {

                    try {
                        Thread.sleep((500 + start - end));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (null == infos) {
                    // 异常
                    getRefreshView().post(new Runnable() {

                        public void run() {
                            getRefreshView().onRefreshComplete();
                        }
                    });

                    return;
                }

                if (null == mData) {
                    mData = new ArrayList<T>();
                }

                if(mPager.getPage() == 0){
                    if(!ListUtils.isEmpty(mData)){
                        mData.clear();
                    }
                }
                if(!isCache){
                    mPager.setPage(mPager.getPage() + 1);
                }

                getRefreshView().post(new Runnable() {

                    public void run() {
                        if (infos != null && mData != null && mAdapter != null) {
                            mData.addAll(infos);
                            mAdapter.setData(mData);
                            mAdapter.notifyDataSetChanged();
                        }

                    }
                });

                getRefreshView().postDelayed(new Runnable() {

                    public void run() {

                        getRefreshView().onRefreshComplete();

                    }
                }, 1000);
            }
        }.start();

    }

    @Override
    public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
        String label = DateUtils.formatDateTime(getActivity(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        loadData();
    }

    /**
     * 清空数据
     */
    public void clearData(){
        mPager.setPage(0);
        mData = null;
        mAdapter.setData(mData);
        mAdapter.notifyDataSetChanged();
    }
}
