package com.yishangshuma.bangelvyou.listener;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

import com.yishangshuma.bangelvyou.util.ListUtils;

import java.util.List;

public class PageChangeListener implements OnPageChangeListener{
	
	private List<String> list;
	private TextView pageText;
	public PageChangeListener(List<String> list){
		this.list = list;
	}
	
	public void setTextView(TextView pageText){
		this.pageText = pageText;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		pageText.setText(new StringBuilder().append((position) % ListUtils.getSize(list) + 1).append("/")
                .append(ListUtils.getSize(list)));
	}
}
