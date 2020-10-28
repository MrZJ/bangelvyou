package com.chongqingliangyou.system.listener;

/**
 * 顶部导航按钮监听器
 */


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.chongqingliangyou.system.R;

public class TopBarClickListener implements OnClickListener{

	private Context ctx;
	private float x = 0;
	private float y = 0;
	public TopBarClickListener(Context ctx){
		this.ctx = ctx;
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_top_goback:
			((Activity) ctx).finish();
			break;
		}
	}

}
