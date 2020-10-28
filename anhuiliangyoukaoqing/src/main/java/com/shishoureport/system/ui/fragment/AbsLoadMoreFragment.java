package com.shishoureport.system.ui.fragment;

/**
 * 下拉上拉fragment基类
 */

import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.shishoureport.system.entity.Pager;
import com.shishoureport.system.ui.adapter.AbsBaseAdapter;
import com.shishoureport.system.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsLoadMoreFragment<V extends AbsListView, T> extends BaseFragment
        implements PullToRefreshBase.OnRefreshListener2<V> {

    protected abstract PullToRefreshAdapterViewBase<V> getRefreshView();

    public abstract void loadData();

    protected Pager mPager = new Pager();

    protected List<T> mData;

    protected AbsBaseAdapter<T> mAdapter;

    public void appendData(final List<T> infos, final long start) {

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

                if (mPager.getPage() == 0) {
                    if (!ListUtils.isEmpty(mData)) {
                        mData.clear();
                    }
                }
                mPager.setPage(mPager.getPage() + 1);

                getRefreshView().post(new Runnable() {

                    public void run() {
                        mData.addAll(infos);
                        mAdapter.setData(mData);
                        mAdapter.notifyDataSetChanged();

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
    public void onPullDownToRefresh(PullToRefreshBase<V> refreshView) {
        String label = DateUtils.formatDateTime(getActivity(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        clearData();
        loadData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<V> refreshView) {
        String label = DateUtils.formatDateTime(getActivity(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        loadData();
    }

    @Override
    public void errorResponseHandler(int code, Throwable arg3, int action) {
        getRefreshView().post(new Runnable() {
            public void run() {
                getRefreshView().onRefreshComplete();
            }
        });
        super.errorResponseHandler(code, arg3, action);
    }

    /**
     * 清空数据
     */
    public void clearData() {
        mPager.setPage(0);
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        if (mAdapter == null) {
            mAdapter = new AbsBaseAdapter<T>() {
                @Override
                public void setData(List<T> infos) {

                }

                @Override
                public int getCount() {
                    return 0;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    return null;
                }
            };
            mAdapter.setData(mData);
        } else {
            mAdapter.setData(mData);
        }
        mAdapter.notifyDataSetChanged();
    }

}
