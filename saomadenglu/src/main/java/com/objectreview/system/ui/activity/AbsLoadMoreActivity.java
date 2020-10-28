package com.objectreview.system.ui.activity;

import android.text.format.DateUtils;
import android.widget.AbsListView;

import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.objectreview.system.adapter.AbsBaseAdapter;
import com.objectreview.system.entity.Pager;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsLoadMoreActivity<V extends AbsListView, T> extends BaseActivity 
						implements PullToRefreshBase.OnRefreshListener2<V> {

	protected abstract PullToRefreshAdapterViewBase<V> getRefreshView();

	protected abstract void loadData();

	protected Pager mPager = new Pager();

	protected List<T> mData;

	protected AbsBaseAdapter<T> mAdapter;

	public void appendData(final List<T> infos, long start) {

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

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<V> refreshView) {
		String label = DateUtils.formatDateTime(this,
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);

		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		clearData();
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<V> refreshView) {
		String label = DateUtils.formatDateTime(this,
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		loadData();
	}

	public void httpError(int code, Throwable arg3){
		getRefreshView().post(new Runnable() {
			public void run() {
				getRefreshView().onRefreshComplete();
			}
		});
//		super.httpError(code, arg3);
	}
	
	/**
	 * 清除商品列表数据，恢复请求初始值
	 */
	public void clearData(){
		mPager.setPage(0);
		mData = null; 
		mAdapter.setData(null);
		mAdapter.notifyDataSetChanged();
	}

}

